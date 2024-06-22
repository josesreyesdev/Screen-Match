package com.jsrspring.screenmatch.dto;

public record EpisodeDTO(
        Integer season,
        String title,
        Integer episodeNumber
) {
}
