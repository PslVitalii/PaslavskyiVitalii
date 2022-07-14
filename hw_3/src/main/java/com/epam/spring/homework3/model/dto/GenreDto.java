package com.epam.spring.homework3.model.dto;

import com.epam.spring.homework3.model.entity.Movie;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;

	@NotBlank
	private String genre;

	@JsonIgnoreProperties("genres")
	private Set<Movie> movies = new HashSet<>();
}
