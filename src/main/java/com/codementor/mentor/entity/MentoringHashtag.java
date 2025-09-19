package com.codementor.mentor.entity;

import com.codementor.mentor.enums.MentoringHashtagName;
import jakarta.persistence.*;

@Entity
public class MentoringHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tagName;

}
