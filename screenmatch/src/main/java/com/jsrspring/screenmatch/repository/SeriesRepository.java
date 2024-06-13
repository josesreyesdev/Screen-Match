package com.jsrspring.screenmatch.repository;

import com.jsrspring.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> { }// nombre de la entidad y el tipo del identificador
