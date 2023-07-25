package com.springboot.socialmedia.service.impl;

import com.springboot.socialmedia.model.RoleModel;
import com.springboot.socialmedia.model.UserModel;
import com.springboot.socialmedia.dto.InforUser;
import com.springboot.socialmedia.repository.ProfileRepository;
import com.springboot.socialmedia.repository.UserRepository;
import com.springboot.socialmedia.security.jwt.JwtUtils;
import com.springboot.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found with email" + email));
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserModel addUser(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public InforUser inforUser(String jwt) {
        String email = jwtUtils.getEmailFromJwt(jwt);
        UserModel user = userRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("User not found with email" + email));
        Set<String> roles = new HashSet<>();
        for (RoleModel role : user.getRoles()){
            roles.add(role.getCode().name());
        }
        return new InforUser(user.getId(), user.getFirstname(), user.getLastname(),
                user.getEmail(), user.getProfile().getImageUrl(), roles);
    }
}
