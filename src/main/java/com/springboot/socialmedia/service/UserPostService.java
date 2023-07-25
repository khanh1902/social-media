package com.springboot.socialmedia.service;

import com.springboot.socialmedia.model.UserPostModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserPostService {
    UserPostModel post(String message, MultipartFile[] multipartFiles);
    UserPostModel updatePost(Long id, String message, MultipartFile[] multipartFiles);
}
