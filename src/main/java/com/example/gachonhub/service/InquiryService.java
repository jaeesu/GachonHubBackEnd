package com.example.gachonhub.service;

import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.inquiry.PostInquiry;
import com.example.gachonhub.domain.inquiry.PostInquiryRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.InquiryRequestDto;
import com.example.gachonhub.payload.response.InquiryListResponseDto;
import com.example.gachonhub.payload.response.InquiryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final PostInquiryRepository inquiryRepository;
    private final UserFileService fileService;
    private final AmazonS3Service s3Service;

    public InquiryListResponseDto findAllByPage(int page) {
        Page<PostInquiry> inquiries = inquiryRepository.findAll(PageRequest.of(page, 15));
        InquiryListResponseDto inquiryListResponseDto = InquiryListResponseDto.fromInquiryList(inquiries);
        return inquiryListResponseDto;
    }

    public InquiryResponseDto findById(Long id) {
        //비밀글 여부 확인, 비밀번호
        PostInquiry postInquiry = findInquiryPostById(id);
        InquiryResponseDto inquiryResponseDto = InquiryResponseDto.fromInquiry(postInquiry);
        return inquiryResponseDto;
    }

    public void savePost(User user, InquiryRequestDto dto) {
        String fileUrl = s3Service.uploadFile(dto.getFile());
        PostInquiry postInquiry = dto.toEntity(user, fileUrl);
        inquiryRepository.save(postInquiry);
    }

    public void deletePost(User user, Long id) throws IllegalAccessException {
        PostInquiry postInquiry = findInquiryPostById(id);
        isCorrectAuthor(user.getId(), postInquiry.getUserId().getId());
        s3Service.deleteFromS3(postInquiry.getImgUrl());
        inquiryRepository.deleteById(id);
    }

    public void updatePost(User user, InquiryRequestDto dto) throws IllegalAccessException, IOException {
        PostInquiry postInquiry = findInquiryPostById(dto.getId());
        isCorrectAuthor(user.getId(), postInquiry.getUserId().getId());
        s3Service.deleteFromS3(postInquiry.getImgUrl());
        savePost(user, dto);
    }

    public PostInquiry findInquiryPostById(Long id) {
        return inquiryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }

    public void isCorrectAuthor(Long userId, Long postAuthorId) throws IllegalAccessException {
        if (userId != postAuthorId) {
            throw new IllegalAccessException();
        }
    }
}
