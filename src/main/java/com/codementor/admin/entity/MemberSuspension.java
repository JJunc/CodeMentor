package com.codementor.admin.entity;

import com.codementor.admin.dto.MemberSuspensionDto;
import com.codementor.member.entity.Member;
import com.codementor.member.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSuspension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;  // 정지된 회원

    private String reason;  // 정지 사유
    private LocalDateTime startDate;
    private LocalDateTime endDate;  // null 이면 영구 정지

    public void suspense(MemberSuspensionDto dto) {
        this.reason = dto.getReason();
        this.endDate = dto.getEndDate();
    }

}
