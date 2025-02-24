package com.codementor.member.service;

import com.codementor.member.dto.LoginRequestDto;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.dto.SignUpRequestDto;
import com.codementor.member.dto.mapper.LoginMapper;
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
    private final LoginMapper loginMapper;
    private final signUpMapper signUpMapper;

    public void signUp(SignUpRequestDto dto) {

        Optional<Member> memberUsername = memberRepository.findByUsername(dto.getUsername());
        Optional<Member> memberEmail = memberRepository.findByEmail(dto.getEmail());

        if (memberUsername.isPresent() || memberEmail.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

        memberRepository.save(signUpMapper.toEntity(dto));
    }

    public LoginResponseDto login(LoginRequestDto dto) {

        Optional<Member> findMember = memberRepository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());

        return loginMapper.toDto(findMember.get());
    }

}
