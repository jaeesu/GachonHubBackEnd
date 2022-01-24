package com.example.gachonhub.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ImageFileUtil {

    public byte[] convertImageToByte(MultipartFile file) throws IOException {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new IOException(); //error handling
        }
    }

    public List<byte[]> convertImageToByte(List<MultipartFile> fileList) throws IOException {
        try {
            List<byte[]> byteList = new ArrayList<>();
            for (MultipartFile f : fileList) {
                byteList.add(f.getBytes());
            }
            return byteList;
        } catch (IOException e) {
            throw new IOException(); //error handling
        }
    }
}
