package com.codementor.mentoring.service;

import com.codementor.mentoring.dto.MentoringScheduleCreate;
import com.codementor.mentoring.dto.MentoringScheduleResponse;
import com.codementor.mentoring.dto.mapper.MentoringScheduleCreateMapper;
import com.codementor.mentoring.entity.MentoringSchedule;
import com.codementor.mentoring.enums.MentoringScheduleStatus;
import com.codementor.mentoring.repository.MentoringScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentoringScheduleService {
    private final MentoringScheduleRepository mentoringScheduleRepository;
    private final MentoringScheduleCreateMapper mentoringScheduleCreateMapper;

    public void createMentoringSchedule(MentoringScheduleCreate mentoringScheduleCreate) {
        mentoringScheduleRepository.save(mentoringScheduleCreateMapper.toEntity(mentoringScheduleCreate));
    }

    public List<MentoringScheduleResponse> getDailySchedules(Long mentorId, LocalDate date) {
        return mentoringScheduleRepository.findByMentorIdAndDate(mentorId, date).stream()
                .map(s -> new MentoringScheduleResponse(
                        s.getStartTime().toLocalTime().toString(),
                        s.getEndTime().toLocalTime().toString(),
                        s.getStatus() == MentoringScheduleStatus.AVAILABLE
                ))
                .collect(Collectors.toList());
    }
}
