package com.epam.spring.homework3.service;

import com.epam.spring.homework3.exceptions.EntityNotFoundException;
import com.epam.spring.homework3.model.dto.MovieDto;
import com.epam.spring.homework3.model.entity.Genre;
import com.epam.spring.homework3.model.entity.Movie;
import com.epam.spring.homework3.persistence.GenreRepository;
import com.epam.spring.homework3.persistence.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
@Transactional
public class MovieService {
	public final ModelMapper modelMapper;
	public final MovieRepository movieRepository;
	public final GenreRepository genreRepository;

	public MovieDto saveMovie(MovieDto movieDto) {
		log.debug("save movie {}", movieDto);
		Movie movie = modelMapper.map(movieDto, Movie.class);
		movieRepository.save(movie);

		return mapMovieToMovieDto(movie);
	}

	public void deleteMovie(long id) {
		log.debug("delete movie by id: {}", id);
		Movie movie = movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Movie.class));
		movieRepository.delete(movie);
	}

	public void updateMovie(long id, MovieDto movieDto) {
		log.debug("update movie with id {}: {}", id, movieDto);
		movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Movie.class));

		Movie movie = modelMapper.map(movieDto, Movie.class);
		movie.setId(id);

		movieRepository.save(movie);
	}

	@Transactional(readOnly = true)
	public MovieDto getMovie(long id) {
		log.debug("get movie by id: {}", id);
		return movieRepository.findById(id)
				.map(this::mapMovieToMovieDto)
				.orElseThrow(() -> new EntityNotFoundException(id, Movie.class));
	}

	@Transactional(readOnly = true)
	public List<MovieDto> getMoviesByName(String name) {
		log.debug("get movie by name: {}", name);
		return movieRepository.findByNameContainingIgnoreCase(name)
				.stream().map(this::mapMovieToMovieDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<MovieDto> getAllMovies() {
		log.info("get all movies");
		return movieRepository.findAll()
				.stream().map(this::mapMovieToMovieDto)
				.collect(Collectors.toList());
	}

	public void addGenreToMovie(long movieId, long genreId) {
		log.debug("add genre {} to movie {}", movieId, genreId);
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new EntityNotFoundException(movieId, Movie.class));
		Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException(genreId, Genre.class));

		movie.getGenres().add(genre);
	}

	public void removeGenreFromMovie(long movieId, long genreId) {
		log.debug("remove genre {} from movie {}", movieId, genreId);
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new EntityNotFoundException(movieId, Movie.class));
		Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new EntityNotFoundException(genreId, Genre.class));

		movie.getGenres().remove(genre);
	}

	public MovieDto mapMovieToMovieDto(Movie movie) {
		return modelMapper.map(movie, MovieDto.class);
	}
}
