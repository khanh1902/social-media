package com.springboot.socialmedia.service;

import com.springboot.socialmedia.model.UserModel;
import com.springboot.socialmedia.dto.InforUser;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserModel findByEmail(String email);
    Boolean existsByEmail(String email);
    UserModel addUser(UserModel userModel);
    InforUser inforUser(String jwt);
}
