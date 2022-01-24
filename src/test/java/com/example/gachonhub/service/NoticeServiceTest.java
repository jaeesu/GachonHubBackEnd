package com.example.gachonhub.service;

import com.example.gachonhub.domain.notice.PostNoticeRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_CONTENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private PostNoticeRepository noticeRepository;

    @Test
    @DisplayName("입력된 글 번호가 존재하지 않을 때")
    void notValidPostNumber(){
        //given
        given(noticeRepository.findById(any())).willReturn(Optional.ofNullable(null));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> noticeService.findNoticePostById(1L));

        //then
        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND_CONTENT_ID);
    }

}