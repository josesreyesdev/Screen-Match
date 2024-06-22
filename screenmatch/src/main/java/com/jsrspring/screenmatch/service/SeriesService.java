package com.jsrspring.screenmatch.service;

import com.jsrspring.screenmatch.dto.EpisodeDTO;
import com.jsrspring.screenmatch.dto.SeriesDTO;
import com.jsrspring.screenmatch.model.Category;
import com.jsrspring.screenmatch.model.Episode;
import com.jsrspring.screenmatch.model.Series;
import com.jsrspring.screenmatch.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
                map(s -> new SeriesDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getEvaluation(),
                        s.getPoster(), s.getGenre(), s.getActors(), s.getSynopsis())
                ).
                collect(Collectors.toList());
    }

    public SeriesDTO getSeriesById(Long id) {
        return convertSeriesToSeriesDTO2(repository.findSeriesById(id));
    }

    private SeriesDTO convertSeriesToSeriesDTO2(Optional<Series> series) {

        if (series.isPresent()) {
            Series s = series.get();
            return new SeriesDTO(s.getId(), s.getTitle(), s.getTotalSeasons(), s.getEvaluation(),
                    s.getPoster(), s.getGenre(), s.getActors(), s.getSynopsis());
        } else {
            System.out.println("No encontre la serie con ese id");
            return null;
        }
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Series> series = repository.findSeriesById(id);
        if (series.isPresent()) {
            var s = series.get();
            return convertEpisodeToEpisodeDTO(s.getEpisodes());
        } else {
            System.out.println("Temporadas no disponible, intente de nuevo");
            return null;
        }
    }

    public List<EpisodeDTO> getSeasonBySeasonNumber(Long id, Long seasonNumber) {
        var episodes = repository.getSeasonBySeasonNumber(id, seasonNumber);
        return convertEpisodeToEpisodeDTO(episodes);
    }

    private List<EpisodeDTO> convertEpisodeToEpisodeDTO(List<Episode> episodes) {
        return episodes.stream().
                map(e -> new EpisodeDTO(e.getSeason(), e.getTitle(), e.getEpisodeNumber())).
                collect(Collectors.toList());
    }

    public List<SeriesDTO> getSeriesByCategory(String genre) {
        Category category = Category.fromEsp(genre);
        return convertSeriesToSeriesDTO(repository.findByGenre(category));
    }
}
