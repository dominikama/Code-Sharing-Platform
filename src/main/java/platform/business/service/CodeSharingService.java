package platform.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.business.module.Code;
import platform.persistence.CodeSharingRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeSharingService {

    private final CodeSharingRepository codeSharingRepository;

    /**
     * Constructor in which code list and code repository are initialized, data is downloaded from the database
     * and added to code list.
     * @param codeSharingRepository repository responsible for database actions
     *
     */
    @Autowired
    public CodeSharingService(CodeSharingRepository codeSharingRepository) {
        this.codeSharingRepository = codeSharingRepository;
    }

    public Code getCode(String UUID) {
        return codeSharingRepository.getByUuid(UUID);
    }

    public List<Code> getLatest() {
        List<Code> list = findAll();
        List<Code> temp = new ArrayList<>();

        //take first 5 elements from the list
        for (int i = 0; i < list.size() && i < 5; i++) {
            temp.add(list.get(i));
        }
        return temp;
    }

    public List<Code> findAll() {
       List<Code> list = codeSharingRepository.findAll();
       List<Code> temp = new ArrayList<>();

       //check if code snippet has restrictions, if not add it to the list
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getViews() <= 0 && list.get(i).getTime() <= 0) {
                temp.add(list.get(i));
            }
        }

        return temp;
    }

    /**
     * Adding code to the list and saving it in the database.
     * @param code
     */
    public void addCode(Code code) {
        codeSharingRepository.save(code);
    }

    /**
     * Method responsible for checking if a given time restriction passed.
     * @param UUID unique identifier of the code snippet
     * @param dateTime current date
     * @return null if given time restriction has passed
     */
    public Long checkTime(String UUID, LocalDateTime dateTime) {

        //check if time restriction is triggered an delete it if so
       if (getCode(UUID).compareTime(dateTime) < 0 && getCode(UUID).getTriggered()) {
           deleteTriggered(UUID);
           return null;
       }
       return getCode(UUID).compareTime(dateTime);
    }

    /**
     * Method responsible for deleting the code snippet from the database and code list if
     * any of the given restrictions is triggered.
     * @param UUID unique identifier of the code snippet
     */
    public void deleteTriggered(String UUID) {
        Code code = getCode(UUID);
        codeSharingRepository.deleteById(code.getId());
    }

    public void decrement(String UUID) {
        Code code = getCode(UUID);
        code.decrementViews();
        codeSharingRepository.save(code);
    }


    /**
     * Method to clear list and database if needed.
     */
    public void clear() {
        codeSharingRepository.findAll().clear();
        codeSharingRepository.deleteAll();
    }

}
