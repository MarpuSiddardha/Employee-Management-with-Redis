package com.siddardha.redisDemonstration.Controller;

import com.siddardha.redisDemonstration.DTO.UserRequest;
import com.siddardha.redisDemonstration.Mapper.UserMapper;
import com.siddardha.redisDemonstration.Model.User;
import com.siddardha.redisDemonstration.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest userRequest) {
         User savedUser = userService.createUser(userRequest);
         return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.mapToResponse(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserRequest userRequest) {
       String token = userService.loginUser(userRequest);
       return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<?>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users.stream().map(UserMapper::mapToResponse).toList());
    }
}
