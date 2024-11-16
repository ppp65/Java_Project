package com.example.project.repository;
import com.example.project.model.SportsMainLogo;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface MainLogoRepository extends CrudRepository<SportsMainLogo, Long> {

    @Query("SELECT m.logoFileName FROM SportsMainLogo m WHERE m.id = 1")
    String findLatestLogoFileName();
}
