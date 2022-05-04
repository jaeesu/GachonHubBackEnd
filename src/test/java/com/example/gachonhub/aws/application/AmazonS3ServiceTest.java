package com.example.gachonhub.aws.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.gachonhub.aws.application.AmazonS3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
class AmazonS3ServiceTest {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Autowired
    private AmazonS3Service s3Service;

    @Test
    void uploadTest() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/test/testImage.jpeg");
        int idx = file.getName().lastIndexOf(".");
        String origin = file.getName().substring(0, idx);
        String content = file.getName().substring(idx);

        System.out.println(origin);
        System.out.println(content);

        String hash = s3Service.convertFileNameToMd5(origin + LocalDateTime.now());
        //똑같은 이름은 덮어쓴다. 파일명이 이상하면 제대로 안 됨.

        amazonS3Client.putObject(new PutObjectRequest(bucket, hash + content, file).withCannedAcl(CannedAccessControlList.PublicRead));
        String s = amazonS3Client.getUrl(bucket, hash + content).toString();
        System.out.println(s);
    }

}