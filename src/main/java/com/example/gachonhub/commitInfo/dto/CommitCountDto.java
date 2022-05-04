package com.example.gachonhub.commitInfo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommitCountDto {

    private String date;
    private Long count;

//    @QueryProjection
    public CommitCountDto(String date, Long count) {
        this.date = date;
//        this.date = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        this.count = count;
    }
}
