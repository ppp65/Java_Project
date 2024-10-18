package com.example.project.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final memberRepository memberRepository;
    public void save(MemberDTO memberDTO) {
        //DTO객체 -> Entity객체
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
    }
    public MemberDTO login(MemberDTO memberDTO) {
        //ID로 정보 조회
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());
        //일치 여부
        if (byMemberId.isPresent()) {
            //조회 결과 O
            MemberEntity memberEntity = byMemberId.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                //비밀번호가 일치
                return MemberDTO.toMemberDTO(memberEntity);
            }
            else {
                //비밀번호 불일치
                return null;
            }
        }
        else {
            //조회 결과 X
            return null;
        }
    }
}
