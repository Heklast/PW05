package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDao {

	/**
	 * Lists movies in our movie database
	 *
	 * @return list of movies
	 */
	public List<Movie> listMovies() {
		List<Movie> listOfMovies= new ArrayList<>();
		try (Connection conn=DataSourceFactory.getConnection()){
			try(Statement statement=conn.createStatement()){
				try(ResultSet results=statement.executeQuery("SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre")){
					while(results.next()) {
						Genre genre = new Genre(
						        results.getInt("idgenre"),
						        results.getString("name")
						    );
						Movie movie=new Movie(results.getInt("idmovie"),results.getString("title"),
								results.getDate("release_date").toLocalDate(),
								genre,
								results.getInt("duration"),
								results.getString("director"),
								results.getString("summary"));
								listOfMovies.add(movie);
					}
				}
			} return listOfMovies;} 
		catch (SQLException e1) {
				e1.printStackTrace();
				throw new RuntimeException("Failed to list movies",e1);
			}
	}
	
	/**
	 * Lists movies in our movie database of a certain genre, genreName
	 *
	 * @return list of movies by genre
	 */
	public List<Movie> listMoviesByGenre(String genreName) {
		List<Movie> listOfMoviesByGenre=new ArrayList<>();
		try(Connection connection=DataSourceFactory.getConnection()){
			try(PreparedStatement statement=connection.prepareStatement("SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre WHERE genre.name = ?")){
				statement.setString(1, genreName);
			 try(ResultSet results=statement.executeQuery()){
				 while(results.next()) {
					 Genre genre = new Genre(
						        results.getInt("idgenre"),
						        results.getString("name")
						    );
					 Movie movie=new Movie(results.getInt("idmovie"),results.getString("title"),
								results.getDate("release_date").toLocalDate(),
								genre,
								results.getInt("duration"),
								results.getString("director"),
								results.getString("summary"));
								listOfMoviesByGenre.add(movie);
				 }
			 }
			
			 } return listOfMoviesByGenre;
		} catch(SQLException e) {
			throw new RuntimeException("Failed to list movies by genre", e);
		}
	}

	/**
	 * Adds a movie to our movie database
	 *
	 * @return the movie added
	 */
	public Movie addMovie(Movie movie) {
		try(Connection connection=DataSourceFactory.getConnection()){
			String query="INSERT INTO movie(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)";
			try(PreparedStatement statement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
				GenreDao genreDao = new GenreDao();
				int idGenre = genreDao.getGenre(movie.getGenre().getName())
					    .orElseThrow(() -> new IllegalArgumentException(
					        "Unknown genre: " + movie.getGenre().getName()
					    ))
					    .getId();
				
				statement.setString(1,movie.getTitle());
				statement.setDate(2, Date.valueOf(movie.getReleaseDate()));
				statement.setInt(3,idGenre);
				statement.setInt(4, movie.getDuration());
				statement.setString(5, movie.getDirector());
				statement.setString(6, movie.getSummary());
				statement.executeUpdate();
				try (ResultSet ids = statement.getGeneratedKeys()) {
				if(ids.next()) {
					return new Movie(ids.getInt(1), movie.getTitle(), movie.getReleaseDate(), movie.getGenre(),movie.getDuration(),movie.getDirector(),movie.getSummary());
				}}
			} return new Movie();
					
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to add movie", e);
		}
		
	}
}
