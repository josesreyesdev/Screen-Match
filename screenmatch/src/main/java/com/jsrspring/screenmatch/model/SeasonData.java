package com.jsrspring.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonData(
        @JsonAlias("Title") String title,
        @JsonAlias("SeasonData") String season, // num de temporada
        @JsonAlias("totalSeasons") Integer totalSeasons,
        @JsonAlias("Episodes") List<EpisodeData> episodeData,
        @JsonAlias("Response") String response
) {
}
