package com.jsrspring.screenmatch.repository;

import com.jsrspring.screenmatch.model.SeriesDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<SeriesDB, Long> { }// nombre de la entidad y el tipo del identificador
