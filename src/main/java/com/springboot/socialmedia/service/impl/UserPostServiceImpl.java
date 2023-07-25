package com.springboot.socialmedia.service.impl;

import com.springboot.socialmedia.model.UserPostModel;
import com.springboot.socialmedia.repository.UserPostRepository;
import com.springboot.socialmedia.service.UserPostService;
import com.springboot.socialmedia.utils.authUtil.GetInforUtil;
import com.springboot.socialmedia.utils.awsUtil.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service
public class UserPostServiceImpl implements UserPostService {
    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private GetInforUtil getInforUtil;

    @Override
    public UserPostModel post(String message, MultipartFile[] multipartFiles) {
        UserPostModel userPost = new UserPostModel();
        userPost.setUsers(getInforUtil.getUser());
        userPost.setMessage(message);
        userPost.setImageUrl(amazonClient.uploadFiles(multipartFiles));

        return userPostRepository.save(userPost);
    }

    @Override
    public UserPostModel updatePost(Long id, String message, MultipartFile[] multipartFiles) {
        UserPostModel userPost = userPostRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Post does not found!"));
        Date modifiedDate = new Date();
        userPost.setModifiedDate(modifiedDate);
        if (message != null) userPost.setMessage(message);
        // add new fileUrls
        if (multipartFiles != null) {
            //delete old fileUrls
//            amazonClient.deleteFiles(userPost.getImageUrl());
            userPost.setImageUrl(amazonClient.uploadFiles(multipartFiles));
        }
        return userPostRepository.save(userPost);
    }
}
