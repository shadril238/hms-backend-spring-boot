package com.shadril.communityportalservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "community_posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "post_title", nullable = false, length = 250)
    private String postTitle;

    @Column(name = "post_content", nullable = false)
    @Lob
    private String postContent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
