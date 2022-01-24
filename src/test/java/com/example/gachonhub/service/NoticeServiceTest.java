package com.example.gachonhub.service;

import com.example.gachonhub.domain.notice.PostNoticeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

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
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> noticeService.findNoticePostById(1L));

        //then
        assertThat(exception.getMessage()).isEqualTo("해당 번호의 글이 존재하지 않습니다.");
    }

}