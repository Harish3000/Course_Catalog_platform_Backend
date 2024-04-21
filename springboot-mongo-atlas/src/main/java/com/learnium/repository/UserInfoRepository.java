package com.learnium.repository;


import com.learnium.model.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserInfoRepository extends MongoRepository<UserInfo, String> {
    Optional<UserInfo> findByUserName(String username);

}
