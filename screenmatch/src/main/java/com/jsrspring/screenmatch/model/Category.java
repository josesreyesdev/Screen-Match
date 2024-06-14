package com.jsrspring.screenmatch.model;

public enum Category {
    ACTION("Action", "Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crimen");

    private final String categoryOmdb;
    private final String categoryEsp;

    Category(String categoryOmdb, String categoryEsp) {
        this.categoryOmdb = categoryOmdb;
        this.categoryEsp = categoryEsp;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.categoryOmdb.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    public static Category fromEsp(String text) {
        for (Category category : Category.values()) {
            if (category.categoryEsp.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
