package com.jsrspring.screenmatch.repository;

import com.jsrspring.screenmatch.model.Category;
import com.jsrspring.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> { // nombre de la entidad y el tipo del identificador
    Optional<Series> findByTitleContainsIgnoreCase(String seriesName);

    List<Series> findTop5ByOrderByEvaluationDesc();

    List<Series> findByGenre(Category category);

    //List<Series> findByTotalSeasonsLessThanEqualAndEvaluationGreaterThanEqual(int totalSeason, Double evaluation);
    //native queries
    /*@Query(
            value = "SELECT * FROM series WHERE series.total_seasons <= 6 AND series.evaluation >= 7.8",
            nativeQuery = true
    )
    List<Series> seriesBySeasonsAndEvaluation(); */

    //queries en el lenguaje usando las
    @Query(value =
            "SELECT s FROM Series s WHERE s.totalSeasons <= :totalSeason And s.evaluation >= :evaluation"
    )
    List<Series> seriesBySeasonsAndEvaluation(int totalSeason, Double evaluation);
}
