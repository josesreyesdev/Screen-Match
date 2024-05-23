package com.jsrspring.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Season(
        @JsonAlias("Title") String title,
        @JsonAlias("Season") String season, // num de temporada
        @JsonAlias("totalSeasons") Integer totalSeasons,
        @JsonAlias("Episodes") List<Episode> episodes,
        @JsonAlias("Response") String response
) {
}
