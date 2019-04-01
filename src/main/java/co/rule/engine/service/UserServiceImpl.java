package co.rule.engine.service;

import co.rule.engine.model.User;
import co.rule.engine.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by grahul on 22-03-2019.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final NotificationService notificationService;

  private final ObjectMapper mapper;

  public UserServiceImpl(UserRepository userRepository, NotificationService notificationService, ObjectMapper mapper) {
    this.userRepository = userRepository;
    this.notificationService = notificationService;
    this.mapper = mapper;
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

    //TODO : Send to SQS
    try {
      notificationService.sendMessage(mapper.writeValueAsString(byId.get()));
    } catch (Exception e) {
      log.error("Sorry, something went wrong while sending message to SQS : {} ", e.getMessage());
    }

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
