package com.example.gachonhub.service;

import com.example.gachonhub.domain.file.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    public List<File> convertBytestToFile(List<byte[]> fileList) {

        return fileList.stream().map(
                m -> File.builder().image(m).build()
        ).collect(Collectors.toList());
    }
}
