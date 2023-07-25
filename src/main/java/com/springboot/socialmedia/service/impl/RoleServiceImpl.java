package com.springboot.socialmedia.service.impl;

import com.springboot.socialmedia.model.ERole;
import com.springboot.socialmedia.model.RoleModel;
import com.springboot.socialmedia.repository.RoleRepository;
import com.springboot.socialmedia.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<RoleModel> findByCode(ERole code) {
        return roleRepository.findByCode(code);
    }
}
