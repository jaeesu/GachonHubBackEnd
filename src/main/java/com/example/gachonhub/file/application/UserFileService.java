package com.example.gachonhub.file.application;

import com.example.gachonhub.file.domain.UserFile;
import com.example.gachonhub.file.domain.UserFileRepository;
import com.example.gachonhub.aws.application.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFileService {

    private final UserFileRepository fileRepository;
    private final AmazonS3Service s3Service;

//    public void deleteUserFileByQuestionId(Long id) {
////        UserFile userFile = fileRepository.deleteByPostQuestionId_Id(id);
//
//        s3Service.deleteFromS3(userFile.getImageUrl().substring(userFile.getImageUrl().lastIndexOf("/")));
//    }

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
