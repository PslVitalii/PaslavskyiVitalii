package com.epam.spring.homework3.controllers;

import com.epam.spring.homework3.model.dto.MovieDto;
import com.epam.spring.homework3.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Movie API")
@AllArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final MovieService movieService;

	@Operation(description = "Save new movie")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public MovieDto saveMovie(@Valid @RequestBody MovieDto movie) {
		movie = movieService.saveMovie(movie);
		return movie;
	}

	@Operation(description = "Delete movie by its id")
	@DeleteMapping("{id}")
	public void deleteMovie(@PathVariable long id) {
		movieService.deleteMovie(id);
	}

	@Operation(description = "Update movie")
	@PutMapping("{id}")
	public void updateMovie(@PathVariable long id, @Valid @RequestBody MovieDto movieDto) {
		movieService.updateMovie(id, movieDto);
	}

	@Operation(description = "Get movie by its id")
	@GetMapping("/{id}")
	public MovieDto getMovieById(@PathVariable long id) {
		return movieService.getMovie(id);
	}

	@Operation(description = "Get movies by name or get all movies")
	@GetMapping
	public List<MovieDto> getMovies(
			@Parameter(name = "name", description = "name of the movie or its part")
			@RequestParam(name = "name", required = false) String name
	) {
		if (name != null) {
			return movieService.getMoviesByName(name);
		}

		return movieService.getAllMovies();
	}

	@PutMapping("/{movieId}/genres/{genreId}")
	public void addGenreToMovie(@PathVariable(name = "movieId") long movieId, @PathVariable(name = "genreId") long genreId) {
		movieService.addGenreToMovie(movieId, genreId);
	}

	@DeleteMapping("/{movieId}/genres/{genreId}")
	public void removeGenreFromMovie(@PathVariable(name = "movieId") long movieId, @PathVariable(name = "genreId") long genreId) {
		movieService.removeGenreFromMovie(movieId, genreId);
	}
}

