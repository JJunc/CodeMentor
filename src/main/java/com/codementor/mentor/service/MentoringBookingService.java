package com.codementor.mentor.service;

import com.codementor.exception.MemberNotFoundException;
import com.codementor.member.entity.Member;
import com.codementor.member.repository.MemberRepository;
import com.codementor.mentor.dto.MentoringBookingDto;
import com.codementor.mentor.dto.MentoringNotificationDto;
import com.codementor.mentor.entity.MentoringBooking;
import com.codementor.mentor.entity.MentoringSchedule;
import com.codementor.mentor.enums.MentoringBookingStatus;
import com.codementor.mentor.enums.MentoringNotificationType;
import com.codementor.mentor.repository.MentoringBookingRepository;
import com.codementor.mentor.repository.MentoringScheduleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MentoringBookingService {

    private final MentoringBookingRepository mentoringBookingRepository;
    private final MentoringScheduleRepository mentoringScheduleRepository;
    private final MemberRepository memberRepository;
    private final MentoringNotificationService mentoringNotificationService;

    @Transactional
    public void bookMentoring(MentoringBookingDto mentoringBookingDto) {
        // 멘토, 멘티 조회
        Member mentor = memberRepository.findById(mentoringBookingDto.getMentorId())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        Member mentee = memberRepository.findById(mentoringBookingDto.getMenteeId())
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        // 멘토링 스케줄 조회
        MentoringSchedule mentoringSchedule = mentoringScheduleRepository.findByIdPL(mentoringBookingDto.getMentoringScheduleId())
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

        // 멘토 알림
        mentoringNotificationService.sendNotification(
                MentoringNotificationDto.builder()
                        .receiverId(mentoringBookingDto.getMentorId())
                        .bookingId(mentoringBooking.getId())
                        .type(MentoringNotificationType.BOOKED)
                        .message("새로운 멘토링 예약이 들어왔습니다.")
                        .build()
        );

        // 멘티 알림
        mentoringNotificationService.sendNotification(
                MentoringNotificationDto.builder()
                        .receiverId(mentoringBookingDto.getMenteeId())
                        .bookingId(mentoringBooking.getId())
                        .type(MentoringNotificationType.BOOKED)
                        .message("멘토링이 예약 됐습니다.")
                        .build()
        );
    }
}
