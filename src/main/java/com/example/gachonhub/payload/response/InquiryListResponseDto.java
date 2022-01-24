package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.inquiry.PostInquiry;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryListResponseDto {

    private int totalPages;
    private int page;
    private List<ListInquiryDto> data;

    public static InquiryListResponseDto fromInquiryList(Page<PostInquiry> inquiries) {
        return InquiryListResponseDto.builder()
                .totalPages(inquiries.getTotalPages())
                .page(inquiries.getNumber())
                .data(inquiries.getContent().stream()
                        .map(ListInquiryDto::new).collect(Collectors.toList()))
                .build();
    }

    @Getter
    public static class ListInquiryDto {
        private Long id;
        private String userId;
        private String title;

        public ListInquiryDto(PostInquiry inquiry) {
            this.id = inquiry.getId();
            this.userId = inquiry.getUserId().getNickname();
            this.title = inquiry.getTitle();
        }
    }
}
