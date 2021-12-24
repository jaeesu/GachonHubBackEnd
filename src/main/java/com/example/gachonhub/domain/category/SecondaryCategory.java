package com.example.gachonhub.domain.category;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecondaryCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "secondary_category_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "primary_category_id")
    private PrimaryCategory primaryCategory;

    @Column(name = "category_name")
    private String name;
}
