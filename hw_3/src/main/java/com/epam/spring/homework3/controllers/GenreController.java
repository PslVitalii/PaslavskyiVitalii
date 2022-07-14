package com.epam.spring.homework3.controllers;

import com.epam.spring.homework3.model.dto.GenreDto;
import com.epam.spring.homework3.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Genre API")
@AllArgsConstructor
@RestController
@RequestMapping("/api/genres")
public class GenreController {
	private final GenreService genreService;

	@Operation(description = "Save genre")
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public GenreDto saveGenre(@Valid @RequestBody GenreDto genre) {
		return genreService.saveGenre(genre);
	}

	@Operation(description = "Delete genre")
	@DeleteMapping("{id}")
	public void deleteGenre(@PathVariable long id) {
		genreService.deleteGenre(id);
	}

	@Operation(description = "Update genre")
	@PutMapping("{id}")
	public void updateGenre(@PathVariable long id, @Valid @RequestBody GenreDto genre) {
		genreService.updateGenre(id, genre);
	}

	@Operation(description = "Get all available genres")
	@GetMapping
	public List<GenreDto> getAllGenres() {
		return genreService.getAllGenres();
	}

	@Operation(description = "Get genre by its id")
	@GetMapping("/{id}")
	public GenreDto getGenre(@PathVariable long id) {
		return genreService.getGenre(id);
	}
}
