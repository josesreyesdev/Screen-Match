package com.jsrspring.screenmatch.repository;

import com.jsrspring.screenmatch.model.Category;
import com.jsrspring.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> { // nombre de la entidad y el tipo del identificador
    Optional<Series> findByTitleContainsIgnoreCase(String seriesName);

    List<Series> findTop5ByOrderByEvaluationDesc();

    List<Series> findByGenre(Category category);
}
