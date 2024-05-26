package com.jsrspring.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SeasonAndEpisode {
    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double evaluation;
    private LocalDate releaseDate;

    public SeasonAndEpisode(String season, Episode e) {
        this.season = Integer.valueOf(season);
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

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Double evaluation) {
        this.evaluation = evaluation;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title=" + title +
                ", episodeNumber=" + episodeNumber +
                ", evaluation=" + evaluation +
                ", releaseDate=" + releaseDate ;
    }
}
