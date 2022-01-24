package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.inquiry.PostInquiry;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.ValidationGroups;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryRequestDto {

    @NotNull(groups = {updateGroup.class})
    @Null(groups = {saveGroup.class})
    private Long id;

    @NotNull(groups = {generalGroup.class})
    private String title;

    @NotNull(groups = {generalGroup.class})
    private String content;

    private String imgUrl;

    private Integer password;

    @NotNull(groups = {generalGroup.class})
    private boolean secret;

    public PostInquiry toEntity(User user) {

        return PostInquiry.builder()
                .id(this.id)
                .userId(user)
                .title(this.title)
                .content(this.content)
                .imgUrl(this.imgUrl)
                .password(this.password)
                .secret(this.secret)
                .build();
    }
}
