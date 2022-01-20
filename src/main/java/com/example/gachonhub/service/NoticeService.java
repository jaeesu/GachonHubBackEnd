package com.example.gachonhub.service;

import com.example.gachonhub.domain.notice.PostNotice;
import com.example.gachonhub.domain.notice.PostNoticeRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.request.NoticeRequestDto;
import com.example.gachonhub.payload.response.NoticeListResponseDto;
import com.example.gachonhub.payload.response.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final PostNoticeRepository noticeRepository;

    public void saveNoticePost(User user, NoticeRequestDto dto) {
        PostNotice postNotice = dto.toEntity(user);
        noticeRepository.save(postNotice).getId();
    }

    public NoticeListResponseDto findAllNoticePostsByPage(int page) {
        PageRequest request = PageRequest.of(page, 15);
        Page<PostNotice> all = noticeRepository.findAll(request);
        NoticeListResponseDto noticeListResponseDto = NoticeListResponseDto.fromNoticeList(all);
        return noticeListResponseDto;
    }

    public NoticeResponseDto findNoticePost(Long id) {
        PostNotice noticePostById = findNoticePostById(id);
        NoticeResponseDto noticeResponseDto = NoticeResponseDto.fromNotice(noticePostById);
        return noticeResponseDto;
    }

    public void deleteNoticePost(User user, Long id) throws IllegalAccessException {
        PostNotice noticePostById = findNoticePostById(id);
        isCorrectAuthor(user.getId(), noticePostById.getId());
        noticeRepository.deleteById(id);
    }

    public void updateNoticePost(User user, NoticeRequestDto dto) throws IllegalAccessException {
        PostNotice noticePostById = findNoticePostById(dto.getId());
        isCorrectAuthor(user.getId(), noticePostById.getId());
        noticeRepository.save(dto.toEntity(user));
    }

    public PostNotice findNoticePostById(Long id) {
        return noticeRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("해당 번호의 글이 존재하지 않습니다.")
        );
    }

    public void isCorrectAuthor(Long userId, Long postAuthorId) throws IllegalAccessException {
        if (userId != postAuthorId) {
            throw new IllegalAccessException();
        }
    }
}
