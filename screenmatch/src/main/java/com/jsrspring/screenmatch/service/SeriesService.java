package com.jsrspring.screenmatch.service;

import com.jsrspring.screenmatch.dto.SeriesDTO;
import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository repository;

    public List<SeriesDTO> getSeries() {
        return convertSeriesToSeriesDTO(repository.findAll());
    }

    public List<SeriesDTO> getTop5Series() {
        return convertSeriesToSeriesDTO(repository.findTop5ByOrderByEvaluationDesc());
    }

    public List<SeriesDTO> getLatestReleasesSeries() {
        return convertSeriesToSeriesDTO(repository.getLatestReleases());
    }

    private List<SeriesDTO> convertSeriesToSeriesDTO(List<Series> series) {
        return series.stream().
                map(s -> new SeriesDTO(s.getTitle(), s.getTotalSeasons(), s.getEvaluation(),
                        s.getPoster(), s.getGenre(), s.getActors(), s.getSynopsis())
                ).
                collect(Collectors.toList());
    }

}
