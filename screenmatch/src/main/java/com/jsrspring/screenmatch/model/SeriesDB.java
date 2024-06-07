package com.jsrspring.screenmatch.model;

import com.jsrspring.screenmatch.service.ChatGPTApiService;
import com.theokanning.openai.OpenAiHttpException;

import java.util.OptionalDouble;

public class SeriesDB {
    private String title;
    private Integer totalSeasons;
    private Double evaluation;
    private String poster;
    private Category genre;
    private String actors;
    private String synopsis;

    public SeriesDB(Series series) {
        this.title = series.title();
        this.totalSeasons = series.totalSeasons();
        this.evaluation = OptionalDouble.of(Double.parseDouble(series.imdbRating())).orElse(0);
        this.poster = series.poster();
        this.genre = Category.fromString(series.genre().split(",")[0].trim());
        this.actors = series.actors();
        try {
            this.synopsis = ChatGPTApiService.getTranslation(series.plot());
        } catch (OpenAiHttpException exception) {
            exception.printStackTrace();
            this.synopsis = series.plot();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Double evaluation) {
        this.evaluation = evaluation;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public String toString() {
        return "title='" + title + '\'' +
                ", totalSeasons=" + totalSeasons +
                ", evaluation=" + evaluation +
                ", poster='" + poster + '\'' +
                ", genre=" + genre +
                ", actors='" + actors + '\'' +
                ", synopsis='" + synopsis;
    }
}
