package com.jsrspring.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double evaluation;
    private LocalDate releaseDate;
    @ManyToOne
    private Series series;

    public Episode() {}

    public Episode(String season, EpisodeData e) {
        this.season = Optional.ofNullable(season)
                .map(Integer::valueOf)
                .orElse(-1);//Integer.valueOf(season);
        this.title = e.title();
        this.episodeNumber = e.episode();

        try {
            this.evaluation = Double.valueOf(e.evaluation());
        } catch (NumberFormatException exception) {
            this.evaluation = 0.0;
        }

        try {
            this.releaseDate = LocalDate.parse(e.released());
        } catch (DateTimeParseException exception) {
            this.releaseDate = null;
        }
    }

    public Series getSeries() {
        return series;
    }

    public Integer getSeason() {
        return season;
    }

    public String getTitle() {
        return title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public Double getEvaluation() {
        return evaluation;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title=" + title +
                ", episodeNumber=" + episodeNumber +
                ", evaluation=" + evaluation +
                ", releaseDate=" + releaseDate;
    }
}
