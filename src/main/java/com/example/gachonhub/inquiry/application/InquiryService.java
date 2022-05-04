package com.example.gachonhub.inquiry.application;

import com.example.gachonhub.inquiry.domain.PostInquiry;
import com.example.gachonhub.inquiry.domain.PostInquiryRepository;
import com.example.gachonhub.aws.application.AmazonS3Service;
import com.example.gachonhub.file.application.UserFileService;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.inquiry.ui.dto.InquiryRequestDto;
import com.example.gachonhub.inquiry.ui.dto.InquiryListResponseDto;
import com.example.gachonhub.inquiry.ui.dto.InquiryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.example.gachonhub.common.exception.ErrorUtil.NOT_CORRECT_USER_ID;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;

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

    public void deletePost(User user, Long id)  {
        PostInquiry postInquiry = findInquiryPostById(id);
        isCorrectAuthor(user.getId(), postInquiry.getUserId().getId());
        s3Service.deleteFromS3(postInquiry.getImgUrl());
        inquiryRepository.deleteById(id);
    }

    public void updatePost(User user, InquiryRequestDto dto) {
        PostInquiry postInquiry = findInquiryPostById(dto.getId());
        isCorrectAuthor(user.getId(), postInquiry.getUserId().getId());
        s3Service.deleteFromS3(postInquiry.getImgUrl());
        savePost(user, dto);
    }

    public PostInquiry findInquiryPostById(Long id) {
        return inquiryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }

    public void isCorrectAuthor(Long userId, Long postAuthorId) {
        if (!userId.equals(postAuthorId)) {
            throw new NotAccessUserException(NOT_CORRECT_USER_ID);
        }
    }
}
