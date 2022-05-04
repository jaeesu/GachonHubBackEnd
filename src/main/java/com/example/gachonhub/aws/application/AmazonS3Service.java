package com.example.gachonhub.aws.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AmazonS3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile)  {
        String hash = convertFileNameToMd5(multipartFile.getOriginalFilename() + LocalDateTime.now());

        return saveToS3(multipartFile, hash);
    }

    public File convertMultiPartFileToFile(MultipartFile multipartFile) {
        try {
            File file = new File(multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    public String saveToS3(MultipartFile file, String hash) {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, hash, file.getInputStream(), null).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, hash).toString();
        } catch (IOException e) {
            return null;
        }
    }

    public void deleteFromS3(String savedName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, savedName));
    }

    public String convertFileNameToMd5(String fileName) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(fileName.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                String hexString = String.format("%02x", b);
                stringBuilder.append(hexString);
            }
            return stringBuilder.toString();
        } catch(NoSuchAlgorithmException e){
            throw new NullPointerException();
        }
    }
}
