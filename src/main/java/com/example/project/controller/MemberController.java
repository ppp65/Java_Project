package com.example.project.controller;

import com.example.project.member.MemberDTO;
import com.example.project.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    //생성자 주입
    private final MemberService memberService;
    //회원가입 정보 DB 전송
    @PostMapping("/member/save")
    public String saveMember(@ModelAttribute MemberDTO memberDTO) {
        if(memberDTO.getMemberPassword().equals(memberDTO.getMemberPasswordConfirm())) {
            memberService.save(memberDTO);
        }
        else {
            return null;
        }
        return null;
    }
    //로그인 정보 DB 전송
    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult != null) {
            //login 성공
            session.setAttribute("loginId", loginResult.getMemberId());
            return null;
        }
        else{
            //login 실패
            return null;
        }
    }
}
