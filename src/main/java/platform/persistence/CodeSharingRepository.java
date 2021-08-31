package platform.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import platform.business.module.Code;

import java.util.List;

/**
 * Database repository
 */
@Repository
public interface CodeSharingRepository extends CrudRepository<Code, Integer> {
    List<Code> findAll();
    Code getByUuid(String uuid);

}
