package com.epam.spring.homework3.config.security;

import com.epam.spring.homework3.model.entity.User;
import com.epam.spring.homework3.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	private final String exceptionMessage = "User with email %s not found";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format(exceptionMessage, username)));

		return new UserDetailsImpl(user);
	}
}
