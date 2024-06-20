package com.jsrspring.screenmatch.dto;

import com.jsrspring.screenmatch.model.Category;

public record SeriesDTO(
        String title,
        Integer totalSeasons,
        Double evaluation,
        String poster,
        Category genre,
        String actors,
        String synopsis
) {
}
