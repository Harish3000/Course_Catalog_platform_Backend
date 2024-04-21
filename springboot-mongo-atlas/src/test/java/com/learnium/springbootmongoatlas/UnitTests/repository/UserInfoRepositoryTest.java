package com.learnium.springbootmongoatlas.UnitTests.repository;

import com.learnium.model.UserInfo;
import com.learnium.repository.UserInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    private UserInfo userInfo;

    @BeforeEach
    void setUp() {
        userInfo = new UserInfo();
        userInfo.setUserName("testUser");
        userInfoRepository.save(userInfo);
    }

    @AfterEach
    void tearDown() {
        userInfoRepository.deleteAll();
    }

    @Test
    void whenValidUserName_thenUserInfoShouldExist() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findByUserName("testUser");
        assertTrue(foundUserInfo.isPresent());
    }

    @Test
    void whenInvalidUserName_thenUserInfoShouldNotExist() {
        Optional<UserInfo> foundUserInfo = userInfoRepository.findByUserName("invalidUser");
        assertFalse(foundUserInfo.isPresent());
    }
}