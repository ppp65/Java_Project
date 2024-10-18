package com.example.project.member;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface memberRepository extends JpaRepository<MemberEntity, Long> {
    //ID로 회원 정보 조회
    Optional<MemberEntity> findByMemberId(String memberId);
}