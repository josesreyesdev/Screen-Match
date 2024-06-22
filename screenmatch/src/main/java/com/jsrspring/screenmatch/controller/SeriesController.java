package com.jsrspring.screenmatch.controller;

import com.jsrspring.screenmatch.dto.EpisodeDTO;
import com.jsrspring.screenmatch.dto.SeriesDTO;
import com.jsrspring.screenmatch.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/series")
public class SeriesController {

    @Autowired
    private SeriesService service;

    @GetMapping()
    public List<SeriesDTO> getSeries() {
        return service.getSeries();
    }

    @GetMapping("/top5")
    public List<SeriesDTO> getTop5Series() {
        return service.getTop5Series();
    }

    @GetMapping("lanzamientos")
    public List<SeriesDTO> getLatestReleases() {
        return service.getLatestReleasesSeries();
    }

    @GetMapping("/{id}")
    public SeriesDTO getSeriesById(@PathVariable Long id) {
        return service.getSeriesById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodeDTO> getAllSeasons(@PathVariable Long id) {
        return service.getAllSeasons(id);
    }
}