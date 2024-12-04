package com.example.project.repository;

import com.example.project.model.SportsTranslate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportsTranslateRepository extends JpaRepository<SportsTranslate, Integer> {
}
