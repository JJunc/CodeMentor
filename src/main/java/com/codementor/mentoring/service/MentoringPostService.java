package com.codementor.mentoring.service;

import com.codementor.exception.MemberNotFoundException;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.mentoring.dto.MentoringPostCreateDto;
import com.codementor.mentoring.dto.MentoringPostDetailDto;
import com.codementor.mentoring.dto.MentoringPostListDto;
import com.codementor.mentoring.dto.mapper.MentoringPostCreateMapper;
import com.codementor.mentoring.dto.mapper.MentoringPostDetailMapper;
import com.codementor.mentoring.dto.mapper.MentoringPostListMapper;
import com.codementor.mentoring.entity.MentoringPost;
import com.codementor.mentoring.repository.MentoringPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class MentoringPostService {

    private final MentoringPostRepository mentoringPostRepository;
    private final MemberRepository memberRepository;
    private final MentoringPostCreateMapper mentoringPostCreateMapper;
    private final MentoringPostListMapper mentoringPostListMapper;
    private final MentoringPostDetailMapper mentoringPostDetailMapper;

    @Transactional
    public Long createMentoringPost(MentoringPostCreateDto dto) {
        Member mentor = memberRepository.findByUsername(dto.getMentorUsername())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));


        // 1. 게시글 저장
        MentoringPost post = mentoringPostCreateMapper.toEntity(dto);
        post.setMentorNickname(mentor.getNickname());
        mentoringPostRepository.save(post);

        return post.getId();
    }

    public List<MentoringPostListDto> getMentoringPostList() {
        return mentoringPostListMapper.toDtoList(mentoringPostRepository.findAll());
    }

    public MentoringPostDetailDto getMentoringPostDetail(Long postId) {
        MentoringPost mentoringPost = mentoringPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 멘토링입니다."));

        MentoringPostDetailDto postDetailDto = mentoringPostDetailMapper.toDto(mentoringPost);

        return postDetailDto;
    }

}
