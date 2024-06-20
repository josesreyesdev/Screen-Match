package com.jsrspring.screenmatch.controller;

import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeriesController {

    @Autowired
    private SeriesRepository repository;

    @GetMapping("/series")
    public List<Series> getSeries() {
        return repository.findAll();
    }
}
