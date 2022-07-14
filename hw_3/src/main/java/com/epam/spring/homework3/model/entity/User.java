package com.epam.spring.homework3.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;

	@Column(unique = true)
	private String email;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	private boolean enable;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(email, user.email) && role == user.role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, role);
	}
}
