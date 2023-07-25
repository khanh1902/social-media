package com.springboot.socialmedia.service;

import com.springboot.socialmedia.model.ProfileModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProfileService {
    ProfileModel updateAvatar(Long id, MultipartFile multipartFile);
    ProfileModel updateInfor(Long id, ProfileModel profileModel);
}
