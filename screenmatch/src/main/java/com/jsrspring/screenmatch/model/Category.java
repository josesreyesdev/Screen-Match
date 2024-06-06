package com.jsrspring.screenmatch.model;

public enum Category {
    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),
    ADVENTURE("Adventure"),
    BIOGRAPHY("Biography"),
    ANIMATION("Animation"),
    SHORT("Short"),
    FAMILY("Family");

    private String categoryOmdb;

    Category(String categoryOmdb) {
        this.categoryOmdb = categoryOmdb;
    }

}
