package com.epam.spring.homework3.service;

import com.epam.spring.homework3.exceptions.EntityNotFoundException;
import com.epam.spring.homework3.model.dto.MovieDto;
import com.epam.spring.homework3.model.entity.Genre;
import com.epam.spring.homework3.model.entity.Movie;
import com.epam.spring.homework3.persistence.GenreRepository;
import com.epam.spring.homework3.persistence.MovieRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    ModelMapper modelMapper;

    @Mock
    MovieRepository movieRepository;

    @Mock
    GenreRepository genreRepository;

    @InjectMocks
    MovieService movieService;

    @Test
    void testSaveMovie() {
        // given
        String movieName = "Test";
        MovieDto movieDto = getMovieDto(movieName);
        Movie movie = getMovie(0, movieName);

        // when
        when(modelMapper.map(movieDto, Movie.class)).thenReturn(movie);
        when(modelMapper.map(movie, MovieDto.class)).thenReturn(getMovieDto(movieName));
        MovieDto result = movieService.saveMovie(movieDto);

        // then
        verify(modelMapper).map(movieDto, Movie.class);
        verify(movieRepository).save(movie);
        assertThat(result.getName(), equalTo(movieName));
    }

    @Test
    void testDeleteMovie() {
        // given
        long id = 1;
        String movieName = "Test";
        Movie movie = getMovie(id, movieName);

        // when
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        movieService.deleteMovie(id);

        // then
        verify(movieRepository).findById(id);
        verify(movieRepository).delete(movie);
    }

    @Test
    void testDeleteMovieThrowsExceptionWhenMovieNotFound() {
        // given
        long id = 1;

        // when
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> movieService.deleteMovie(id));
    }

    @Test
    void testUpdateMovie() {
        // given
        long id = 1;
        String movieName = "test";
        MovieDto movieDto = getMovieDto(movieName);
        Movie movie = getMovie(0, movieName);

        // when
        when(movieRepository.findById(id)).thenReturn(Optional.of(getMovie(id, movieName)));
        when(modelMapper.map(movieDto, Movie.class)).thenReturn(movie);
        movieService.updateMovie(id, movieDto);

        // then
        verify(movieRepository).findById(id);
        verify(modelMapper).map(movieDto, Movie.class);
        verify(movieRepository).save(movie);
        assertThat(movie.getId(), equalTo(id));
    }

    @Test
    void testUpdateMovieThrowsExceptionWhenMovieNotFound() {
        // given
        long id = 1;
        String movieName = "test";
        MovieDto movieDto = getMovieDto(movieName);

        // when
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> movieService.updateMovie(id, movieDto));
    }

    @Test
    void testGetMovieById() {
        // given
        long id = 1;
        String movieName = "test";
        Movie movie = getMovie(id, movieName);
        MovieDto movieDto = getMovieDto(movieName);

        // when
        when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        when(modelMapper.map(movie, MovieDto.class)).thenReturn(movieDto);
        MovieDto result = movieService.getMovie(id);

        // then
        verify(movieRepository).findById(id);
        verify(modelMapper).map(movie, MovieDto.class);
        assertThat(result.getName(), equalTo(movieName));
    }

    @Test
    void testGetMovieByIdThrowsExceptionWhenMovieNotFound() {
        // given
        long id = 1;

        // when
        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> movieService.getMovie(id));
    }

    @Test
    void testGetMoviesByName() {
        // given
        String movieName = "test";
        List<Movie> movies = List.of(getMovie(1, "test"), getMovie(2, "test1"));

        // when
        when(movieRepository.findByNameContainingIgnoreCase(movieName)).thenReturn(movies);
        for (var movie : movies) {
            when(modelMapper.map(movie, MovieDto.class)).thenReturn(getMovieDto(movie.getName()));
        }
        List<MovieDto> resultList = movieService.getMoviesByName(movieName);

        //then
        verify(movieRepository).findByNameContainingIgnoreCase(movieName);
        verify(modelMapper, times(movies.size())).map(ArgumentMatchers.any(Movie.class), eq(MovieDto.class));
        assertThat(resultList, hasSize(movies.size()));
    }

    @Test
    void testGetAllMovies() {
        // given
        List<Movie> movies = List.of(getMovie(1, "test"), getMovie(2, "test1"));

        // when
        when(movieRepository.findAll()).thenReturn(movies);
        for (var movie : movies) {
            when(modelMapper.map(movie, MovieDto.class)).thenReturn(getMovieDto(movie.getName()));
        }
        List<MovieDto> resultList = movieService.getAllMovies();

        //then
        verify(movieRepository).findAll();
        verify(modelMapper, times(movies.size())).map(ArgumentMatchers.any(Movie.class), eq(MovieDto.class));
        assertThat(resultList, hasSize(movies.size()));
    }

    @Test
    void testAddGenreToMovie() {
        // given
        long movieId = 1;
        long genreId = 2;
        Movie movie = getMovie(movieId, "test");
        Genre genre = getGenre(genreId, "test");

        // when
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        movieService.addGenreToMovie(movieId, genreId);

        // then
        verify(movieRepository).findById(movieId);
        verify(genreRepository).findById(genreId);
        assertThat(movie.getGenres(), Matchers.contains(genre));
    }

    @Test
    void testAddGenreToMovieThrowsExceptionWhenMovieNotFound() {
        // given
        long movieId = 1;
        long genreId = 2;

        // when
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> movieService.removeGenreFromMovie(movieId, genreId));
    }

    @Test
    void testAddGenreToMovieThrowsExceptionWhenGenreNotFound() {
        // given
        long movieId = 1;
        long genreId = 2;
        Movie movie = getMovie(movieId, "test");

        // when
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> movieService.addGenreToMovie(movieId, genreId));
    }

    @Test
    void testRemoveGenreFromMovie() {
        // given
        long movieId = 1;
        long genreId = 2;
        Genre genre = getGenre(genreId, "test");
        Movie movie = getMovie(movieId, "test");
        movie.getGenres().add(genre);

        // when
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));
        movieService.removeGenreFromMovie(movieId, genreId);

        // then
        verify(movieRepository).findById(movieId);
        verify(genreRepository).findById(genreId);
        assertFalse(movie.getGenres().contains(genre));
    }

    @Test
    void testRemoveGenreFromMovieThrowsExceptionWhenMovieNotFound() {
        // given
        long movieId = 1;
        long genreId = 2;

        // when
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> movieService.removeGenreFromMovie(movieId, genreId));
    }

    @Test
    void testRemoveGenreFromMovieThrowsExceptionWhenGenreNotFound() {
        // given
        long movieId = 1;
        long genreId = 2;
        Movie movie = getMovie(movieId, "test");

        // when
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> movieService.removeGenreFromMovie(movieId, genreId));
    }

    @Test
    void mapMovieToMovieDto() {
        // given
        String movieName = "test";
        Movie movie = getMovie(0, movieName);
        MovieDto movieDto = getMovieDto(movieName);

        // when
        when(modelMapper.map(movie, MovieDto.class)).thenReturn(movieDto);
        MovieDto result = movieService.mapMovieToMovieDto(movie);

        // then
        verify(modelMapper).map(movie, MovieDto.class);
        assertThat(result.getName(), equalTo(movieName));
    }

    private Movie getMovie(long id, String name) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setName(name);

        return movie;
    }

    private MovieDto getMovieDto(String name) {
        MovieDto movieDto = new MovieDto();
        movieDto.setName(name);

        return movieDto;
    }

    private Genre getGenre(long id, String genreName) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setGenre(genreName);

        return genre;
    }
}