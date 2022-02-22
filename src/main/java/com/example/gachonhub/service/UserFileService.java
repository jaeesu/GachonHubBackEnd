package com.example.gachonhub.service;

import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.file.UserFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFileService {

    private final AmazonS3Service s3Service;


    public List<UserFile> uploadMultiPartToUserFile(List<MultipartFile> files) {
        if (files.isEmpty()) return new ArrayList<>();
        List<UserFile> userFiles = files.stream()
                .map(m -> uploadMultiPartToUserFile(m))
                .collect(Collectors.toList());
        return userFiles;
    }

    public UserFile uploadMultiPartToUserFile(MultipartFile file)  {
        if (file.isEmpty()) return null;
        UserFile userFile = UserFile.builder()
                .realName(file.getOriginalFilename())
                .imageUrl(s3Service.uploadFile(file))
                .build();

        return userFile;
    }
}
