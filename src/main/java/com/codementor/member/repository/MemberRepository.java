package com.codementor.member.repository;

import com.codementor.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    Optional<Member> findByUsernameAndPassword(String username, String password);

    Optional<Member> findByEmail(String email);
}
