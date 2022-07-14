package com.epam.spring.homework3.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "genre", unique = true)
	private String genre;

	@ManyToMany(mappedBy = "genres", cascade = CascadeType.REMOVE)
	private Set<Movie> movies = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Genre genre1 = (Genre) o;
		return Objects.equals(genre, genre1.genre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(genre);
	}
}
