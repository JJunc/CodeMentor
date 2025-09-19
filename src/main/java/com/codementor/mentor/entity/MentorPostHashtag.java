package com.codementor.mentor.entity;

import jakarta.persistence.*;

@Entity
public class MentorPostHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int mentoringPostId;

    @Column(nullable = false)
    private int mentoringHashtagId;

}
