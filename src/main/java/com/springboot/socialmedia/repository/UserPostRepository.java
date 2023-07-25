package com.springboot.socialmedia.repository;

import com.springboot.socialmedia.model.UserPostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPostRepository extends JpaRepository<UserPostModel, Long> {
}
