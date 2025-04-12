package com.codementor.admin.service;

import com.codementor.admin.dto.MemberSuspensionDto;
import com.codementor.admin.dto.mapper.MemberSuspensionMapper;
import com.codementor.admin.entity.MemberSuspension;
import com.codementor.admin.repository.MemberSuspensionRepository;
import com.codementor.member.entity.Member;
import com.codementor.member.enums.MemberStatus;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.PostUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final MemberSuspensionRepository memberSuspensionRepository;
    private final MemberRepository memberRepository;
    private final MemberSuspensionMapper memberSuspensionMapper;

    @Transactional
    public void suspenseMember(MemberSuspensionDto dto) {
        // 1. 회원(Member) 조회
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalStateException("해당 회원이 존재하지 않습니다."));

        log.info("변경할 회원 상태: {}", dto.getMemberStatus().getDescription());
        log.info("정지 회원 번호: {}", member.getId());

        // 2. 기존 MemberSuspension 조회 (기존 데이터가 있는 경우 방지)
        MemberSuspension memberSuspension = memberSuspensionRepository.findByMemberId(dto.getMemberId()).orElse(null);

        if (memberSuspension == null) {
            memberSuspension = memberSuspensionMapper.toEntity(dto);

            member.setStatus(dto.getMemberStatus());

            memberSuspensionRepository.save(memberSuspension);
        } else {
            member.setStatus(dto.getMemberStatus());

            memberSuspension.suspense(dto);
        }

        log.info("멤버 활동 상태 = {}", member.getStatus());
    }


}
