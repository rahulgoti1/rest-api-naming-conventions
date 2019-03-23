package co.rule.engine.repository;

import co.rule.engine.model.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by grahul on 22-03-2019.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByName(final @Param("name") String name);

}
