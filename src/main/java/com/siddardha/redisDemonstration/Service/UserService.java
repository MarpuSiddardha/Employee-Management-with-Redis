package com.siddardha.redisDemonstration.Service;

import com.siddardha.redisDemonstration.DTO.UserRequest;
import com.siddardha.redisDemonstration.Exception.UserAlreadyExistsException;
import com.siddardha.redisDemonstration.Exception.UserNotFoundException;
import com.siddardha.redisDemonstration.Model.Role;
import com.siddardha.redisDemonstration.Model.User;
import com.siddardha.redisDemonstration.Repository.RoleRepository;
import com.siddardha.redisDemonstration.Repository.UserRepository;
import com.siddardha.redisDemonstration.Util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User createUser(@Valid UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        User user  = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });
        user.getRoles().add(defaultRole);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String loginUser(@Valid UserRequest userRequest) {
        User existingUser = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        if(passwordEncoder.matches(userRequest.getPassword(), existingUser.getPassword())) {
            return jwtUtil.generateToken(existingUser.getUsername());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
