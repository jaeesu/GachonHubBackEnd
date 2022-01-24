package com.example.gachonhub.service;

import com.example.gachonhub.domain.inquiry.PostInquiry;
import com.example.gachonhub.domain.inquiry.PostInquiryRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.request.InquiryRequestDto;
import com.example.gachonhub.payload.response.InquiryListResponseDto;
import com.example.gachonhub.payload.response.InquiryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final PostInquiryRepository inquiryRepository;

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
        PostInquiry postInquiry = dto.toEntity(user);
        inquiryRepository.save(postInquiry);
    }

    public void deletePost(User user, Long id) throws IllegalAccessException {
        PostInquiry postInquiry = findInquiryPostById(id);
        isCorrectAuthor(user.getId(), postInquiry.getUserId().getId());
        inquiryRepository.deleteById(id);
    }

    public void updatePost(User user, InquiryRequestDto dto) throws IllegalAccessException {
        PostInquiry postInquiry = findInquiryPostById(dto.getId());
        isCorrectAuthor(user.getId(), postInquiry.getUserId().getId());
        inquiryRepository.save(dto.toEntity(user));
    }

    public PostInquiry findInquiryPostById(Long id) {
        return inquiryRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public void isCorrectAuthor(Long userId, Long postAuthorId) throws IllegalAccessException {
        if (userId != postAuthorId) {
            throw new IllegalAccessException();
        }
    }
}
