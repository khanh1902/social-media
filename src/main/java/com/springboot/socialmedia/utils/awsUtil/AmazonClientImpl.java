package com.springboot.socialmedia.utils.awsUtil;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.FilenameUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Service
public class AmazonClientImpl implements AmazonClient {
    private AmazonS3 amazonS3;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("ap-southeast-1")
                .build();
    }

    // AWS s3 yêu cầu tham số kiểu File, nhưng dữ liệu truyền lên là MultipartFile nên phải chuyển sang File
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertFile);
        fos.write(file.getBytes());
        fos.close();
        return convertFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        String fileExtension = FilenameUtils.getExtension(multiPart.getOriginalFilename());
        return new Date().getTime() + "." + fileExtension;
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        // PublicRead: bất kì ai nếu có đường dẫn của file là có thể truy cập file đó
    }

    // tách các url lấy keyname
    public ArrayList<String> splits (String imageUrl){
        ArrayList<String> fileNames = new ArrayList<>(); // luu danh sach key sau khi tach chuoi
        String[] str = imageUrl.split("," ); // tach rieng tung url
        for(String s : str){
            String[] fileName = s.split("/"); // lay key cua tung chuoi sau khi tach
            fileNames.add(fileName[fileName.length -1]);
        }
        return fileNames;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) {
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            String fileUrl = endpointUrl + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadFiles(MultipartFile[] multipartFiles) {
        try {
            StringBuilder imageUrls = new StringBuilder();
            for (MultipartFile multipartFile : multipartFiles) {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile);
                String imageUrl = endpointUrl + "/" + fileName;
                uploadFileTos3bucket(fileName, file);
                file.delete();
                imageUrls.append(imageUrl).append(", ");
            }
            return imageUrls.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) { // fileUrl lay tu database
        amazonS3.deleteObject(bucketName, splits(fileUrl).get(0));
    }

    @Override
    public void deleteFiles(String fileUrls) {
        ArrayList<String> keys = splits(fileUrls);
        for(String key: keys){
            amazonS3.deleteObject(bucketName, key);
        }
    }
}
