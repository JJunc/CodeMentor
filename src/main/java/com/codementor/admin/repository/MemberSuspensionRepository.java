package com.codementor.admin.repository;

import com.codementor.admin.entity.MemberSuspension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberSuspensionRepository extends JpaRepository<MemberSuspension, Long> {

    Optional<MemberSuspension> findByMemberId(Long memberId);
}
