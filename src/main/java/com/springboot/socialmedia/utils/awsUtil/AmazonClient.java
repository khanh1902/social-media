package com.springboot.socialmedia.utils.awsUtil;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AmazonClient {
    String uploadFile(MultipartFile multipartFile); // upload 1 file
    String uploadFiles(MultipartFile[] multipartFiles); // upload nhieu file
    void deleteFile(String fileUrl); // delete 1 file
    void deleteFiles(String fileUrls); //
}
