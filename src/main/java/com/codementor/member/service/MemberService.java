package com.codementor.member.service;

import com.codementor.member.dto.MemberLoginRequestDto;
import com.codementor.member.dto.MemberSignUpRequestDto;
import com.codementor.member.dto.mapper.signUpMapper;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final signUpMapper signUpMapper;

    public void signUp(MemberSignUpRequestDto dto) {

        Optional<Member> memberUsername = memberRepository.findByUsername(dto.getUsername());
        Optional<Member> memberEmail = memberRepository.findByEmail(dto.getEmail());

        if (memberUsername.isPresent() || memberEmail.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        memberRepository.save(signUpMapper.toEntity(dto));
    }

    public boolean login(MemberLoginRequestDto dto) {

        Optional<Member> memberUsername = memberRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());

        if(memberUsername.isEmpty()) {
            return false;
        }

        return true;
    }

}
