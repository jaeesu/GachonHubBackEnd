package com.example.gachonhub.contest.domain;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.contest.ui.dto.ContestRequestDto;
import com.example.gachonhub.post.domain.Post;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "post_contest")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostContest extends Post {

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private SubCategory categoryId;

    private Integer hit;

    @Setter
    private String image;

    @Builder
    public PostContest(Long id, User userId, String title, String content, SubCategory categoryId, Integer hit, String image) {
        super(id, userId, title, content);
        this.categoryId = categoryId;
        this.hit = hit;
        this.image = image;
    }

    public void updateFromDto(ContestRequestDto dto, SubCategory category, String url) {
        this.setTitle(dto.getTitle());
        this.setContent(dto.getContent());
        this.setCategoryId(category);
        this.setImage(url);
    }
}
