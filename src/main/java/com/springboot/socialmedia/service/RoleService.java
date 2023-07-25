package com.springboot.socialmedia.service;

import com.springboot.socialmedia.model.ERole;
import com.springboot.socialmedia.model.RoleModel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RoleService {
    Optional<RoleModel> findByCode(ERole code);

}
