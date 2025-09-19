package com.codementor.mentor.dto;

import com.codementor.mentor.enums.MentorPosition;
import com.codementor.mentor.enums.MentoringHashtagName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class MentoringPostCreateDto {

    private Long authorId;

    private String authorNickname;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String content;

    @Positive
    private int durationMinutes;

    @Positive
    private BigDecimal price;

    private Set<MentoringHashtagName> tags = new HashSet<>();


}
