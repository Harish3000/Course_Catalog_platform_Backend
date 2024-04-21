package com.learnium.controller;

import com.learnium.model.AuthRequest;
import com.learnium.model.UserInfo;
import com.learnium.service.JwtService;
import com.learnium.service.UserInfoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserInfoService service;

    @PostMapping("/register")
    public String addNewUser(@Valid @RequestBody UserInfo userInfo) {
        try {
            service.addUser(userInfo);
            logger.info("New user added successfully");
            return "New user added successfully";
        } catch (Exception e) {
            logger.error("Error occurred while adding new user", e);
            throw e;
        }
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(authRequest.getUsername());
            } else {
                throw new UsernameNotFoundException("invalid user request !");
            }
        } catch (UsernameNotFoundException e) {
            logger.error("Invalid user request", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error occurred while authenticating user", e);
            throw e;
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or  hasAuthority('ROLE_FACULTY')")
    public List<UserInfo> getAllUsers() {
        try {
            return service.getAllUsers();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all users", e);
            throw e;
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public UserInfo updateUser(@Valid @RequestBody UserInfo userInfo) {
        try {
            return service.updateUser(userInfo);
        } catch (Exception e) {
            logger.error("Error occurred while updating user", e);
            throw e;
        }
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUser(@PathVariable String userId) {
        try {
            return service.deleteUser(userId);
        } catch (Exception e) {
            logger.error("Error occurred while deleting user", e);
            throw e;
        }
    }
}