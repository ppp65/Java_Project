package com.example.project.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SPORTS_MAIN_LOGO")
public class SportsMainLogo {
    @Id
    private Long id; // Always 1 to ensure single entry

    @Column(name = "LOGO_FILE_NAME")
    private String logoFileName;

    @Column(name = "LAST_CHANGED")
    private LocalDate lastChanged;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogoFileName() {
        return logoFileName;
    }

    public void setLogoFileName(String logoFileName) {
        this.logoFileName = logoFileName;
    }

    public LocalDate getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(LocalDate lastChanged) {
        this.lastChanged = lastChanged;
    }
}
