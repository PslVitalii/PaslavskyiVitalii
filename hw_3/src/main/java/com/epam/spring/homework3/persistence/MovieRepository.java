package com.epam.spring.homework3.persistence;

import com.epam.spring.homework3.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	List<Movie> findByNameContainingIgnoreCase(String name);
}
