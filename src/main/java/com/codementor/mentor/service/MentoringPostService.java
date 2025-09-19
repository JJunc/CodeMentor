package com.codementor.mentor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MentoringPostService {

//    private final MentoringHashtagRepository mentoringHashtagRepository;
//    private final MemberRepository memberRepository;

//    public void createMentoringPost(MentoringPostCreateDto dto) {
//        // 멘토링 게시글 작성자 확인
//        Member member = memberRepository.findById(dto.getAuthorId())
//                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 멘토입니다."));
//
//
//        // dto에 있는 해시태그 이름으로 Hashtag 테이블에서 조회 후 dto에 저장
//        Set<MentoringHashtag> hashtags = dto.getTags().stream()
//                .map(tagName -> mentoringHashtagRepository.findByName(tagName)
//                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그: " + tagName)))
//                .collect(Collectors.toSet());
//
//        // 멘토링 게시판 테이블에 저장
//
//
//    }
}
