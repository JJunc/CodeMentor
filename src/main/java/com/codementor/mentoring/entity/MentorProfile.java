package com.codementor.mentoring.entity;

import com.codementor.mentoring.enums.MentorCareer;
import com.codementor.mentoring.enums.MentorPosition;
import jakarta.persistence.*;

@Entity
public class MentorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long mentorId;

    @Column(nullable = false, unique = true)
    private String mentorNickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentorPosition mentorPosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentorCareer mentorCareer;
}
