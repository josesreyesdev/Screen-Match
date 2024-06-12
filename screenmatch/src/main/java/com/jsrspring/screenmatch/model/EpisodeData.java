package com.jsrspring.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(
        @JsonAlias("Title") String title,
        @JsonAlias("Year") String year,
        @JsonAlias("Rated") String rated,
        @JsonAlias("Released") String released, // fecha de lanzamiento
        @JsonAlias("SeasonData") String season,
        @JsonAlias("EpisodeData") Integer episode, // num EpisodeData
        @JsonAlias("Runtime") String runtime,
        @JsonAlias("Genre") String genre,
        @JsonAlias("Director") String director,
        @JsonAlias("Writer") String writer,
        @JsonAlias("Actors") String actors,
        @JsonAlias("Plot") String plot,
        @JsonAlias("Language") String language,
        @JsonAlias("Country") String country,
        @JsonAlias("Awards") String awards,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Ratings") List<RatingData> ratingData,
        @JsonAlias("Metascore") String metascore,
        @JsonAlias("imdbRating") String evaluation, // evaluacion
        @JsonAlias("imdbVotes") String imdbVotes,
        @JsonAlias("imdbID") String imdbID,
        @JsonAlias("seriesID") String seriesID,
        @JsonAlias("Type") String type,
        @JsonAlias("Response") String response
) {
}
