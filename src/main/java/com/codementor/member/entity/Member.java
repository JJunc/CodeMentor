package com.codementor.member.entity;

import com.codementor.admin.entity.MemberSuspension;
import com.codementor.member.dto.MemberEditNicknameDto;
import com.codementor.member.dto.MemberEditPasswordDto;
import com.codementor.member.dto.MemberEmailUpdateDto;
import com.codementor.member.enums.MemberRole;
import com.codementor.member.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    // 사용자가 아이디를 아이디를 변경하는 경우가 존재하기 때문에 자동키 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.MEMBER;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void updateNickname(MemberEditNicknameDto dto) {
        this.nickname = dto.getNickname();
    }

    public void updateEmail(MemberEmailUpdateDto dto) {
        this.email = dto.getEmail();
    }
    public void updatePassword(MemberEditPasswordDto dto) {
        this.password = dto.getPassword();
    }

    public void updateStatus(MemberStatus status) {}

}
