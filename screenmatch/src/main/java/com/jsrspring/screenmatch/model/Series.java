package com.jsrspring.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora las propiedades que no sean incuidas en mi record
public record Series(
        @JsonAlias("Title") String title,
        @JsonAlias("Year") String year,
        @JsonAlias("Rated") String rated,
        @JsonAlias("Released") String released,
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
        @JsonAlias("Ratings") List<Rating> ratings,
        @JsonAlias("Metascore") String metascore,
        @JsonAlias("imdbRating") String imdbRating, // evaluation
        @JsonAlias("imdbVotes") String imdbVotes,
        @JsonAlias("imdbID") String imdbID,
        @JsonAlias("Type") String type,
        @JsonAlias("totalSeasons") String totalSeasons,
        @JsonAlias("Response") boolean response
) {
}
