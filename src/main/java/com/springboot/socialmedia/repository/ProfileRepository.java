package com.springboot.socialmedia.repository;

import com.springboot.socialmedia.model.ProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileModel, Long> {
}
