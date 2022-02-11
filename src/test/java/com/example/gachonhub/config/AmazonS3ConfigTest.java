package com.example.gachonhub.config;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class AmazonS3ConfigTest {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Test
    void s3ConnectionTest() {
        AccessControlList bucketAcl = amazonS3Client.getBucketAcl(bucket);
        System.out.println(bucketAcl.toString());
    }

    @Test
    void uploadTest() throws IOException {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/test/testImage.jpeg");

        amazonS3Client.putObject(new PutObjectRequest(bucket, file.getName(), file).withCannedAcl(CannedAccessControlList.PublicRead));
        String s = amazonS3Client.getUrl(bucket, file.getName()).toString();
        System.out.println(s);
    }


}