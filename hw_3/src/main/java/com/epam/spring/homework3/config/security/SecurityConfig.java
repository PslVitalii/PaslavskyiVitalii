package com.epam.spring.homework3.config.security;

import com.epam.spring.homework3.config.security.jwt.JwtTokenVerifierFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final JwtTokenVerifierFilter jwtTokenFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager getAuthenticationManager() throws Exception {
		return this.authenticationManager();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();

		http.authorizeRequests()
				.antMatchers("/v3/api-docs", "/swagger-ui.html", "/swagger-ui/api-docs/swagger-config", "/swagger-ui/**").permitAll()
				.antMatchers("/api/auth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/movies/**", "/api/genres/**").permitAll()
				.antMatchers("/api/movies/**", "/api/genres/**").hasAuthority("ADMIN")
				.anyRequest().authenticated();

		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token"));
		configuration.setExposedHeaders(List.of("x-auth-token"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}