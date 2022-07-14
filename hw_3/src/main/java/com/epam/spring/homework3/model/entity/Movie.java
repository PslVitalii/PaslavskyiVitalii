package com.epam.spring.homework3.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;
	private String overview;

	@Column(nullable = false)
	private Long duration;

	@Column(nullable = false)
	private LocalDate releaseDate;

	private String posterImage;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> previewImages;

	private String trailerUrl;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Genre> genres = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> directors = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> actors = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Movie movie = (Movie) o;
		return Objects.equals(name, movie.name) && Objects.equals(duration, movie.duration) && Objects.equals(releaseDate, movie.releaseDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, duration, releaseDate);
	}
}
