package co.rule.engine.handler;

import co.rule.engine.model.User;
import co.rule.engine.service.UserService;
import co.rule.engine.util.ErrorHandler;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by grahul on 22-03-2019.
 */
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserHandler {

  //TODO : Controller    GET /api/v1/users?filter=parent_id
  //TODO GetPortfolioByUsers:   GET /api/v1/users/portfolio_allocation
  //TODO GetPortfolioByUsers:   GET /api/v1/users/10/portfolios/5/assets/
  //TODO GetAllUsers:   GET /api/v1/users
  //TODO GetSingleUser: GET /api/v1/users/1
  //TODO CreateUser:    POST /api/v1/users
  //TODO UpdateUser:    PUT /api/v1/users/1
  //TODO DeleteUser:    DELETE /api/v1/users/1

  private final UserService userService;

  public UserHandler(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<User>> listAllUsers() {
    List<User> users = userService.findAllUsers();
    if (users.isEmpty()) {
      // return new ResponseEntity(HttpStatus.NO_CONTENT);
      // You many decide to return HttpStatus.NOT_FOUND
    }
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getUser(@PathVariable Long userId) {
    Optional<User> user = userService.get(userId);
    if (user.isPresent()) {
      return ResponseEntity
          .ok()
          .body(user.get());
    }
    log.info("User with id {} not found from database.", userId);
    return new ResponseEntity<>(new ErrorHandler("User not found with id : " + userId),
        HttpStatus.NOT_FOUND);
  }

  @PostMapping()
  public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder componentsBuilder) {
    Optional<User> save = userService.save(user);
    if (save.isPresent()) {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setLocation(componentsBuilder.path("/api/v1/users/{id}").buildAndExpand(save.get().getId()).toUri());
      return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }
    return new ResponseEntity<>(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @PutMapping("/{userId}")
  public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
    try {
      Optional<User> update = userService.update(userId, user);
      if (update.isPresent()) {
        return ResponseEntity
            .ok()
            .body(update.get());
      }
    } catch (RuntimeException e) {
      log.error("Unable to update. User with id {} not found.", userId);
      return new ResponseEntity(new ErrorHandler("Unable to update. User with id " + userId + " not found."),
          HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
    try {
      log.info("Fetching & Deleting User with id {}", userId);
      userService.delete(userId);
    } catch (RuntimeException e) {
      log.error("Unable to delete. User with id {} not found.", userId);
      return new ResponseEntity(new ErrorHandler("Unable to delete. User with id " + userId + " not found."),
          HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
