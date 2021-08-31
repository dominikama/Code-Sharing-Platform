package platform.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import platform.business.module.Code;
import platform.business.service.CodeSharingService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    CodeSharingService code;

    /**
     * Method which allows to get a code snippet in JSON format.
     * @param UUID unique identifier of the code snippet
     * @param response used to set header
     * @return Code object in JSON format or NOT FOUND
     */
    @RequestMapping(value = "api/code/{uuid}", method = GET)
    @ResponseBody
    public ResponseEntity<?> getCode(@PathVariable("uuid") String UUID, HttpServletResponse response){
        response.setHeader("Content-Type", "application/json");
        try {
            //decrement amount of allowed views
            code.decrement(UUID);

            Map<String, Object> result = new TreeMap<>();

            //put in the map code, date, time and views of the snippet
            result.put("code", code.getCode(UUID).getCode());
            result.put("date", code.getCode(UUID).getDate());
            result.put("time", code.checkTime(UUID, LocalDateTime.now()));
            result.put("views", code.getCode(UUID).getViews());

            //check if any of the restrictions was triggered, if so delete a code snippet from the database
            if (code.getCode(UUID).getTriggered()) {
                code.deleteTriggered(UUID);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Method responsible for creating new code snippet
     * @param codeNew  code snippet in JSON format
     * @return unique identifier of the code snippet
     */
    @ResponseBody
    @PostMapping("api/code/new")
    public Map<String, String> returnNewCode(@RequestBody Code codeNew) {

        Map<String, String> map = new HashMap<>();

        try {
            //set date, uuid, amount of time and triggered for send code snippet
            codeNew.setDate(LocalDateTime.now());
            codeNew.setUuid();
            codeNew.setAmountOfTime(codeNew.getTime());
            codeNew.setTriggered(false);

            //if amount of views or time is less than 0 set it to 0
            if (codeNew.getViews() < 0) {
                codeNew.setViews(0);
            } else if (codeNew.getTime() < 0) {
                codeNew.setTime(0L);
            }

            //add code to the database
            code.addCode(codeNew);

            System.out.println(codeNew.getUuid());

            //return id of the code snippet
            map.put("id", codeNew.getUuid());
            return map;
        } catch (Exception e) {
            throw new FormException("Invalid data!");
        }
    }

    /**
     * Method that allows to get 10 latest code snippets without restrictions in JSON format
     * @return snippets in JSON format
     */
    @ResponseBody
    @GetMapping("api/code/latest")
    public List<Code> latest() {
        return code.getLatest();
    }


}
