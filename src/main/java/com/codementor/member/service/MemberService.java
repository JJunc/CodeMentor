package com.codementor.member.service;

import com.codementor.member.dto.*;
import com.codementor.member.dto.mapper.LoginMapper;
import com.codementor.member.dto.mapper.MemberUpdateMapper;
import com.codementor.member.dto.mapper.MyPageMapper;
import com.codementor.member.dto.mapper.signUpMapper;
import com.codementor.member.entity.Member;
import com.codementor.member.enums.CheckEmail;
import com.codementor.member.enums.CheckPassword;
import com.codementor.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LoginMapper loginMapper;
    private final signUpMapper signUpMapper;
    private final MemberUpdateMapper memberUpdateMapper;
    private final MyPageMapper myPageMapper;
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

    public boolean checkEmail(MemberUpdateDto dto) {
        return memberRepository.findByEmail(dto.getEmail()).isPresent();
    }

    public CheckPassword checkPassword(String username, MemberUpdateDto dto) {
        Member member = memberRepository.findByUsername(username).orElse(null);
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            return CheckPassword.CURRENT_PASSWORD_MISMATCH;
        }
        return CheckPassword.SUCCESS;
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        return loginMapper.toDto(memberRepository.findByUsername(dto.getUsername())
                .filter(m -> passwordEncoder.matches(dto.getPassword(), m.getPassword()))
                .orElse(null));
    }

    public MemberUpdateDto memberToUpdateDto(String username) {
        return memberUpdateMapper.toDto(memberRepository.findByUsername(username).orElse(null));
    }

    public MyPageDto memberToMyPageDto(String username) {
        return myPageMapper.toDto(memberRepository.findByUsername(username).orElse(null));
    }

    @Transactional
    public CheckEmail editEmail(String username, MemberEmailUpdateDto dto) {
        Member member = memberRepository.findByUsername(username).orElse(null);
        log.info("회원 존재 여부 = {}", member.toString());

        // 이메일 중복검사
        if (member.getEmail().equals(dto.getEmail())) {
            log.info("이메일 중복");
            return CheckEmail.DUPLICATED_EMAIL;
        }

        log.info("이메일 변경");
        member.updateEmail(dto);

        return CheckEmail.SUCCESS;
    }

    @Transactional
    public List<CheckPassword> editPassword(String username, MemberUpdateDto dto) {
        Member member = memberRepository.findByUsername(username).orElse(null);
        List<CheckPassword> checkPasswords = new ArrayList<>();

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            checkPasswords.add(CheckPassword.CURRENT_PASSWORD_MISMATCH);
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 검증
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            checkPasswords.add(CheckPassword.PASSWORDS_DO_NOT_MATCH);
        }

        // 모든 검증 후 에러가 하나라도 있으면 리턴
        if (!checkPasswords.isEmpty()) {
            return checkPasswords;
        }

        // 비밀번호 변경 로직 실행
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        log.info("암호화 된 비밀번호 = {}", dto.getPassword());
        member.updatePassword(dto);
        checkPasswords.add(CheckPassword.SUCCESS);
        return checkPasswords;
    }

}
