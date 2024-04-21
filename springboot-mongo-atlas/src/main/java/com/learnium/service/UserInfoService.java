package com.learnium.service;


import com.learnium.model.UserInfo;
import com.learnium.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String addUser(UserInfo userInfo) {
        List<UserInfo> users = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if (!users.isEmpty()) {
            String lastUserId = users.get(0).getId();
            String numericPart = lastUserId.substring(5);
            int newId = Integer.parseInt(numericPart) + 1;
            String newUserId = String.format("USER-%03d", newId);
            userInfo.setId(newUserId);
        } else {
            userInfo.setId("USER-001");
        }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added successfully";
    }

    public UserInfo getUserByUsername(String username) {
        return repository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public List<UserInfo> getAllUsers() {
        return repository.findAll();
    }

    public UserInfo updateUser(UserInfo userInfoRequest) {
        UserInfo existingUser = repository.findById(userInfoRequest.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userInfoRequest.getId()));

        existingUser.setUserName(userInfoRequest.getUserName());
        existingUser.setName(userInfoRequest.getName());
        existingUser.setEmail(userInfoRequest.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userInfoRequest.getPassword()));
        existingUser.setRoles(userInfoRequest.getRoles());

        return repository.save(existingUser);
    }

    public String deleteUser(String userId) {
        UserInfo user = repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        repository.deleteById(userId);
        return userId + " user deleted successfully";
    }


}
