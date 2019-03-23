package co.rule.engine.service;

import co.rule.engine.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Created by grahul on 22-03-2019.
 */
public interface UserService {

  Optional<User> get(final Long id);

  Optional<User> save(final User user);

  Optional<User> update(final Long userId, final User user);

  void delete(final Long id);

  List<User> findAllUsers();

}
