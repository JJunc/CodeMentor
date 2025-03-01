package com.codementor.member.service;

import com.codementor.member.dto.LoginRequestDto;
import com.codementor.member.dto.LoginResponseDto;
import com.codementor.member.dto.SignUpRequestDto;
import com.codementor.member.dto.mapper.LoginMapper;
import com.codementor.member.dto.mapper.signUpMapper;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoginMapper loginMapper;
    private final signUpMapper signUpMapper;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequestDto dto) {

        // 비밀번호 암호화
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        memberRepository.save(signUpMapper.toEntity(dto));
    }

    public boolean checkUsername(SignUpRequestDto dto) {
        return memberRepository.findByUsername(dto.getUsername()).isPresent();
    }

    public boolean checkEmail(SignUpRequestDto dto) {
        return memberRepository.findByEmail(dto.getEmail()).isPresent();
    }

    public boolean checkPassword(SignUpRequestDto dto) {
        log.info("비밀번호 일치 여부 = {}", dto.getPassword().equals(dto.getConfirmPassword()));
        return dto.getPassword().equals(dto.getConfirmPassword());
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        return loginMapper.toDto(memberRepository.findByUsername(dto.getUsername())
                .filter(m-> passwordEncoder.matches(dto.getPassword(), m.getPassword()))
                .orElse(null));
    }

}
