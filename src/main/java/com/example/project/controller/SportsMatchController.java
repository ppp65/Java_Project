package com.example.project.controller;

import com.example.project.DTO.SportsMatchDTO;
import com.example.project.model.SportsMatch;
import com.example.project.repository.SportsMatchRepository;
import com.example.project.service.SportsMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin
@RestController
@RequestMapping("/matches")
public class SportsMatchController {
    private final SportsMatchService sportsMatchService;

    public SportsMatchController(SportsMatchService sportsMatchService) {
        this.sportsMatchService = sportsMatchService;
    }

    // 기존의 /matches/upcoming 엔드포인트
    @GetMapping("/upcoming")
    public ResponseEntity<List<SportsMatchDTO>> getUpcomingMatches() {
        List<SportsMatch> matches = sportsMatchService.getAllMatches();

        if (matches.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<SportsMatchDTO> matchDTOs = matches.stream()
                .map(SportsMatchDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(matchDTOs);
    }

    // 새로운 /matches 엔드포인트 추가
    @GetMapping
    public ResponseEntity<List<SportsMatchDTO>> getMatches() {
        // 여기서도 마찬가지로 "upcoming" 매치만 필터링하거나, 전체 매치를 반환할 수 있습니다.
        List<SportsMatch> matches = sportsMatchService.getAllMatches();

        if (matches.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<SportsMatchDTO> matchDTOs = matches.stream()
                .map(SportsMatchDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(matchDTOs);
    }
}