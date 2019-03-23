package co.rule.engine.service;

import co.rule.engine.model.User;
import co.rule.engine.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Created by grahul on 22-03-2019.
 */
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> get(final Long id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> save(final User user) {
    return Optional.ofNullable(userRepository.save(user));
  }

  @Override
  public Optional<User> update(final Long userId, final User user) {
    Optional<User> byId = userRepository.findById(userId);

    if (byId.isPresent()) {
      User updateUser = byId.get();
      updateUser.setName(user.getName());
      return Optional.ofNullable(userRepository.save(updateUser));
    }
    throw new RuntimeException("User not registered with Id : " + userId);
  }

  @Override
  public void delete(final Long userId) {
    if (get(userId).isPresent()) {
      userRepository.deleteById(userId);
    } else {
      throw new RuntimeException("User not registered with Id : " + userId);
    }
  }

  @Override
  public List<User> findAllUsers() {
    return (List<User>) userRepository.findAll();
  }

}
