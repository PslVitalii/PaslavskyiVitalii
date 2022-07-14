package com.epam.spring.homework3.persistence;

import com.epam.spring.homework3.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
	Optional<Genre> findByGenre(String genre);
}
