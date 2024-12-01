package com.example.project.service;

import com.example.project.model.SportsMatch;
import com.example.project.model.SportsMatchId;
import com.example.project.repository.SportsMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SportsMatchService {

    @Autowired
    private SportsMatchRepository sportsMatchRepository;

    // 모든 경기 데이터를 가져오는 메서드 (id가 null인 경우 제외)
    public List<SportsMatch> getAllMatches() {
        List<SportsMatch> matches = sportsMatchRepository.findAll();

        matches.forEach(match -> {
            if (match.getId() == null) {
                System.err.println("sportsMatchId가 없습니다: " + match);
            } else {
                System.out.println("매칭된 데이터: " + match.getId());
            }
        });

        // id가 null이 아닌 경우만 필터링
        return matches.stream()
                .filter(match -> match.getId() != null)
                .collect(Collectors.toList());
    }

    public SportsMatch getMatchById(LocalDate matchDate, String matchTime, String stadium) {
        SportsMatchId id = new SportsMatchId(matchDate, matchTime, stadium);
        return sportsMatchRepository.findById(id).orElse(null);
    }
}