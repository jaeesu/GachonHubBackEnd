package com.example.gachonhub.notice.application;

import com.example.gachonhub.notice.domain.PostNotice;
import com.example.gachonhub.notice.domain.PostNoticeRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.notice.ui.dto.NoticeRequestDto;
import com.example.gachonhub.notice.ui.dto.NoticeListResponseDto;
import com.example.gachonhub.notice.ui.dto.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import static com.example.gachonhub.common.exception.ErrorUtil.NOT_CORRECT_USER_ID;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;

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

    public void deleteNoticePost(User user, Long id) {
        PostNotice noticePostById = findNoticePostById(id);
        isCorrectAuthor(user.getId(), noticePostById.getUserId().getId());
        noticeRepository.deleteById(id);
    }

    public void updateNoticePost(User user, NoticeRequestDto dto)  {
        PostNotice noticePostById = findNoticePostById(dto.getId());
        isCorrectAuthor(user.getId(), noticePostById.getUserId().getId());
        noticeRepository.save(dto.toEntity(user));
    }

    public PostNotice findNoticePostById(Long id) {
        return noticeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)
        );
    }

    public void isCorrectAuthor(Long userId, Long postAuthorId) {
        if (!userId.equals(postAuthorId)) {
            throw new NotAccessUserException(NOT_CORRECT_USER_ID);
        }
    }
}
