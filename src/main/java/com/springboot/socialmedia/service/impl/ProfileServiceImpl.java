package com.springboot.socialmedia.service.impl;

import com.springboot.socialmedia.model.ProfileModel;
import com.springboot.socialmedia.repository.ProfileRepository;
import com.springboot.socialmedia.service.ProfileService;
import com.springboot.socialmedia.utils.awsUtil.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AmazonClient amazonClient;

    @Override
    public ProfileModel updateAvatar(Long id, MultipartFile file) {
        ProfileModel oldProfile = profileRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Profile does not found!")
        );
        String imageUrl = amazonClient.uploadFile(file);
        if(imageUrl != null) oldProfile.setImageUrl(imageUrl);
        return profileRepository.save(oldProfile);
    }

    @Override
    public ProfileModel updateInfor(Long id, ProfileModel profile) {
        ProfileModel oldProfile = profileRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Profile does not found!")
        );
        if(!profile.getSex().isEmpty()) oldProfile.setSex(profile.getSex());
        if(!profile.getHobby().isEmpty()) oldProfile.setHobby(profile.getHobby());
        if(!profile.getAddress().isEmpty()) oldProfile.setAddress(profile.getAddress());
        if(!profile.getDateOfBirth().isEmpty()) oldProfile.setDateOfBirth(profile.getDateOfBirth());
        return profileRepository.save(oldProfile);
    }
}
