package com.springboot.socialmedia.repository;


import com.springboot.socialmedia.model.ERole;
import com.springboot.socialmedia.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByCode(ERole code);
}
