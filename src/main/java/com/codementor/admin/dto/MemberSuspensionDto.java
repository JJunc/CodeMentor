package com.codementor.admin.dto;

import com.codementor.admin.entity.MemberSuspension;
import com.codementor.member.entity.Member;
import com.codementor.member.enums.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSuspensionDto {

    private Long memberId;
    @NotBlank(message = "정지 사유를 입력해주세요.")
    private String reason;
    private MemberStatus memberStatus;
    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate;
}
