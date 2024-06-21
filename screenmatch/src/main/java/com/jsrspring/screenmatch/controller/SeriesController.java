package com.jsrspring.screenmatch.controller;

import com.jsrspring.screenmatch.dto.SeriesDTO;
import com.jsrspring.screenmatch.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeriesController {

    @Autowired
    private SeriesService service;

    @GetMapping("/series")
    public List<SeriesDTO> getSeries() {
        return service.getSeries();
    }

    @GetMapping("/series/top5")
    public List<SeriesDTO> getTop5Series() {
        return service.getTop5Series();
    }


}