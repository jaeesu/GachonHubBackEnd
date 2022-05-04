package com.example.gachonhub.notice.ui.dto;

import com.example.gachonhub.notice.domain.PostNotice;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeListResponseDto {

    private int totalPages;
    private int page;
    private List<ListNoticeDto> data;

    public static NoticeListResponseDto fromNoticeList(Page<PostNotice> notices) {
        return NoticeListResponseDto.builder()
                .totalPages(notices.getTotalPages())
                .page(notices.getNumber())
                .data(notices.getContent().stream()
                        .map(ListNoticeDto::new).collect(Collectors.toList()))
                .build();
    }

    @Getter
    public static class ListNoticeDto {
        private Long id;
        private String user;
        private String title;

        public ListNoticeDto(PostNotice notice) {
            this.id = notice.getId();
            this.user = notice.getUserId().getNickname();
            this.title = notice.getTitle();
        }
    }
}
