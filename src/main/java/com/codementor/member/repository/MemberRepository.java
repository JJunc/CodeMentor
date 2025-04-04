package com.codementor.member.repository;

import com.codementor.member.dto.MemberSearchDto;
import com.codementor.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByUsername(String username);


    Optional<Member> findByUsernameAndPassword(String username, String password);

    Optional<Member> findByEmail(String email);

    Page<Member> findByUsername(String username, Pageable pageable);
    Page<Member> findByEmail(String email,  Pageable pageable);
    Page<Member> findByNickname(String nickname,  Pageable pageable);
    Page<Member> findByCreatedAt(LocalDateTime createdAt, Pageable pageable);

    Member findByid(Long id);


}
