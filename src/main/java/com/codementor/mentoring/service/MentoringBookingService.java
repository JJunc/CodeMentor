package com.codementor.mentoring.service;

import com.codementor.exception.MemberNotFoundException;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.mentoring.dto.MentoringBookingDto;
import com.codementor.mentoring.entity.MentoringBooking;
import com.codementor.mentoring.entity.MentoringSchedule;
import com.codementor.mentoring.enums.MentoringBookingStatus;
import com.codementor.mentoring.enums.MentoringScheduleStatus;
import com.codementor.mentoring.repository.MentoringBookingRepository;
import com.codementor.mentoring.repository.MentoringScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentoringBookingService {

    private final MentoringBookingRepository mentoringBookingRepository;
    private final MentoringScheduleRepository mentoringScheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void bookMentoring(MentoringBookingDto mentoringBookingDto) {
        // 멘토, 멘티 조회
        Member mentor = memberRepository.findById(mentoringBookingDto.getMentorId())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        Member mentee = memberRepository.findById(mentoringBookingDto.getMenteeId())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        // 멘토링 스케줄 조회
        MentoringSchedule mentoringSchedule = mentoringScheduleRepository.findById(mentoringBookingDto.getMentoringScheduleId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 멘토링입니다."));

        // 멘토링 예약 조회
        MentoringBooking mentoringBookingExist =
                mentoringBookingRepository.findByMentoringScheduleId(mentoringBookingDto.getMentoringScheduleId())
                .orElse(null);

        if (mentoringBookingExist != null) {
            throw new IllegalStateException("이미 예약된 멘토링 입니다.");
        }

        // 멘토링 예약
        MentoringBooking mentoringBooking = MentoringBooking.builder()
                .mentoringScheduleId(mentoringBookingDto.getMentoringScheduleId())
                .mentorId(mentoringBookingDto.getMentorId())
                .menteeId(mentee.getId())
                .price(mentoringBookingDto.getPrice())
                .startTime(mentoringBookingDto.getStartTime())
                .endTime(mentoringBookingDto.getEndTime())
                .status(MentoringBookingStatus.WAITING)
                .build();

        mentoringBookingRepository.save(mentoringBooking);

        mentoringScheduleRepository.bookSchedule(mentoringSchedule.getId(), MentoringScheduleStatus.BOOKED);
    }
}
