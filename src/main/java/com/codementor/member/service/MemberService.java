package com.codementor.member.service;

import com.codementor.admin.dto.MemberUpdateDto;
import com.codementor.exception.InvalidPasswordException;
import com.codementor.exception.LoginFailedException;
import com.codementor.exception.MemberNotFoundException;
import com.codementor.member.dto.MemberSearchDto;
import com.codementor.member.dto.mapper.MemberSearchMapper;
import com.codementor.admin.entity.MemberSuspension;
import com.codementor.admin.repository.MemberSuspensionRepository;
import com.codementor.member.dto.*;
import com.codementor.member.dto.mapper.*;
import com.codementor.member.entity.Member;
import com.codementor.member.enums.CheckPassword;
import com.codementor.member.enums.MemberRole;
import com.codementor.member.enums.MemberStatus;
import com.codementor.member.repository.MemberRepository;
import com.codementor.post.dto.PostListDto;
import com.codementor.post.dto.mapper.PostListMapper;
import com.codementor.post.entity.Post;
import com.codementor.post.enums.PostCategory;
import com.codementor.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberSuspensionRepository memberSuspensionRepository;
    private final PostRepository postRepository;
    private final PostListMapper postListMapper;
    private final LoginMapper loginMapper;
    private final signUpMapper signUpMapper;
    private final MemberListMapper memberListMapper;
    private final MemberSearchMapper memberSearchMapper;
    private final MemberUpdateMapper memberUpdateMapper;
    private final MyPageMapper myPageMapper;
    private final PasswordEncoder passwordEncoder;


    public Page<MemberListDto> getAllMembers(Pageable pageable) {
        return memberRepository.findAll(pageable).map(m -> memberListMapper.toDto(m));
    }

    public Page<MemberListDto> searchMembers(MemberSearchDto memberSearchDto, Pageable pageable) {
        // SearchType에 따라 조건을 다르게 처리
        Page<Member> members;
        log.info("검색조건 {}", memberSearchDto.getSearchType());

        switch (memberSearchDto.getSearchType()) {
            case EMAIL:
                members = memberRepository.findByEmail(memberSearchDto.getKeyword(), pageable);
                break;
            case USERNAME:
                members = memberRepository.findByUsername(memberSearchDto.getKeyword(), pageable);
                break;
            case NICKNAME:
                members = memberRepository.findByNickname(memberSearchDto.getKeyword(), pageable);
                break;
            default:
                members = memberRepository.findAll(pageable);  // 기본적으로 전체 검색
                break;
        }

        return members.map(memberListMapper::toDto);  // 반환 시 DTO로 변환
    }


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

    public List<CheckPassword> checkPassword(String username, MemberEditPasswordDto dto) {
        Member member = memberRepository.findByUsername(username).orElse(null);
        List<CheckPassword> checkPasswords = new ArrayList<>();

        if (!passwordEncoder.matches(dto.getCurrentPassword(), member.getPassword())) {
            checkPasswords.add(CheckPassword.CURRENT_PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            checkPasswords.add(CheckPassword.SAME_AS_OLD);
        }

        return checkPasswords;
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        Member member = memberRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new LoginFailedException("아이디 또는 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new LoginFailedException("비밀번호가 틀렸습니다.");
        }

        log.info("member role = {}", member.getRole());
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUsername(member.getUsername());
        loginResponseDto.setUsername(member.getUsername());
        loginResponseDto.setRole(member.getRole());

        if (member.getStatus() != MemberStatus.ACTIVE) {
            MemberSuspension memberSuspension = memberSuspensionRepository.findByMemberId(member.getId())
                    .orElseThrow(() -> new IllegalStateException("정지 정보가 없습니다."));
            loginResponseDto.setStartDate(memberSuspension.getStartDate());
            loginResponseDto.setEndDate(memberSuspension.getEndDate());
            loginResponseDto.setReason(memberSuspension.getReason());
        }

        return loginResponseDto;
    }

    public MemberEditPasswordDto memberToUpdateDto(String username) {
        return memberUpdateMapper.toDto(memberRepository.findByUsername(username).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다.")));
    }

    public MyPageDto memberToMyPageDto(String username) {
         return myPageMapper.toDto(memberRepository.findByUsername(username).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다.")));
    }

    @Transactional
    public boolean editNickname(String username, MemberEditNicknameDto dto) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        log.info("회원 존재 여부 = {}", member.toString());

        if (member.getNickname().equals(dto.getNickname())) {
            return false;
        }

        member.updateNickname(dto);
        postRepository.updateAuthorNickname(username, dto.getNickname());
        return true;
    }

    @Transactional
    public boolean editEmail(String username, MemberEmailUpdateDto dto) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        // 이메일 중복검사
        if (member.getEmail().equals(dto.getEmail())) {
            log.info("이메일 중복");
            return false;
        }

        log.info("이메일 변경");
        member.updateEmail(dto);

        return true;
    }

    @Transactional
    public List<CheckPassword> editPassword(String username, MemberEditPasswordDto dto) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));
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

    public Page<PostListDto> getMyPostList(String username, PostCategory category, Pageable pageable) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Page<Post> postPage = postRepository.findByCategoryAndAuthor(category, username, pageable);
        return postPage.map(post -> postListMapper.toDto(post));
    }

    @Transactional
    public void updateMemberRole(MemberRoleDto dto) {
        Member member = memberRepository.findByUsername(dto.getUsername()).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        String nickname = dto.getNickname();

        if (member.getRole() != MemberRole.MENTOR && dto.getMemberRole() == MemberRole.MENTOR) {
            // 멘토로 변경 시 [멘토] 접두사 추가
            if (!nickname.startsWith("[멘토] ")) {
                dto.setNickname("[멘토] " + nickname);
            }
        } else if (dto.getMemberRole() == MemberRole.MEMBER) {
            // 일반 회원으로 변경 시 [멘토] 접두사 제거
            dto.setNickname(nickname.replace("[멘토] ", ""));
        }

        member.updateRole(dto);
    }

    @Transactional
    public void deleteMember(String username, String password) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()
                -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        member.deletedMember();
    }


}
