package com.codementor.mentor.service;

import com.codementor.mentor.dto.MentoringBookingDto;
import com.codementor.mentor.entity.MentoringBooking;
import com.codementor.mentor.entity.MentoringSchedule;
import com.codementor.mentor.enums.MentoringBookingStatus;
import com.codementor.mentor.repository.MentoringBookingRepository;
import com.codementor.mentor.repository.MentoringScheduleRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
class MentoringBookingServiceTest {

    @Autowired
    private MentoringBookingService mentoringBookingService;

    @Autowired
    private MentoringNotificationService mentoringNotificationService;

    @Autowired
    private MentoringScheduleRepository scheduleRepository;

    @Autowired
    private MentoringBookingRepository bookingRepository;

    @Test
    void bookMentoring() throws InterruptedException {
        MentoringSchedule mentoringSchedule = scheduleRepository.findById(2L).orElse(null);
        MentoringBookingDto dto = new MentoringBookingDto(
                2L, 34L, 21L,
                mentoringSchedule.getStartTime(),
                mentoringSchedule.getEndTime(),
                mentoringSchedule.getPrice()
        );

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    mentoringBookingService.bookMentoring(dto);
                } catch (Exception e) {
                    exceptions.add(e); // 예외 기록
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // 예약은 단 하나만 성공해야 함
        List<MentoringBooking> bookings = bookingRepository.findAllByMentoringScheduleId(2L);
        Assertions.assertThat(bookings).hasSize(1);

        // 나머지는 낙관적 락 예외 or 예약 불가 예외
        Assertions.assertThat(exceptions).isNotEmpty();
    }

}