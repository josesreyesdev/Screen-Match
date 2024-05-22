package com.jsrspring.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

// clasificación
public record Rating(
        @JsonAlias("Source") String source,
        @JsonAlias("Value") String value
) { }