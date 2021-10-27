package com.example.vendingmachine.controller;

import com.example.vendingmachine.model.SignUpData;
import com.example.vendingmachine.model.entity.User;
import com.example.vendingmachine.service.SecurityService;
import com.example.vendingmachine.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    /**
     * Create a new USER by POST request, end point is http://hostname:port/user
     *
     * @param signUpData
     * @return user
     */

    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpData signUpData) {
        User user = new User();
        BeanUtils.copyProperties(signUpData, user);

        if(!Stream.of(User.POSSIBLE_ROLES).anyMatch(x -> x.equals(user.getRole())))
            return new ResponseEntity<Object>("Wrong user role.", HttpStatus.NOT_ACCEPTABLE);

        userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    /**
     * Create a new USER by POST request, end point is http://hostname:port/user/login
     * provided by Spring Security
     * @param loginData
     * @return null
     */

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@Valid LoginData loginData) {
        User user = userService.findByUsername(loginData.getUsername());
        if(user == null) {
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        }
        if(!user.getPassword().equals(loginData.getPassword())){
            return new ResponseEntity<Object>("Password mismatch.", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<Object>(user, HttpStatus.OK);
    }*/

    /*** get a USER by ID in GET request, end point is http://hostname:port/user/id
     * @param userId
     * @return
     */

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**get all list of users based on GET request.
     * @return
     */

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.findAllUsers();
        return ResponseEntity.ok(userList);
    }

    /**Update a user based on PUT request.
     * @param userId
     * @param user
     * @return
     */

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId, @RequestBody SignUpData signUpData) {
        //todo solve - cannot authorize after changing name or password
        User loggedInUser = securityService.findLoggedInUser();
        if (loggedInUser == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if (loggedInUser.getId() != userId)
            return new ResponseEntity<Object>("Please use your user ID.", HttpStatus.UNAUTHORIZED);

        User testUser = userService.findByUsername(signUpData.getUsername());
        if(testUser.getId() != loggedInUser.getId())
            return new ResponseEntity<Object>("Username already used.", HttpStatus.NOT_ACCEPTABLE);

        User user = new User();
        BeanUtils.copyProperties(signUpData, user);
        user.setId(userId);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    /**delete a user based on user ID.
     * @param userId
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        User loggedInUser = securityService.findLoggedInUser();

        if (loggedInUser == null)
            return new ResponseEntity<Object>("User does not exist.", HttpStatus.UNAUTHORIZED);
        if (loggedInUser.getId() != userId)
            return new ResponseEntity<Object>("Please use your user ID.", HttpStatus.UNAUTHORIZED);

        //todo check the products if seller is removed
        if(userService.deleteUser(userId)) {
            return ResponseEntity.ok("User '" + userId + "' has been deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}