package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	/**
	 * Lists all genres stored in our genre database.
	 *
	 * @return a list of all genres
	 */
	public List<Genre> listGenres() {
		List<Genre> listOfGenres= new ArrayList<>();
		try (Connection conn=DataSourceFactory.getConnection()){
			try(Statement statement=conn.createStatement()){
				try(ResultSet results=statement.executeQuery("select * from genre")){
					while(results.next()) {
						Genre genre = new Genre(results.getInt("idgenre"), results.getString("name"));
						listOfGenres.add(genre);
					}}}
				return listOfGenres;}
					
				 catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException("Listing genres failed", e);}
	}
	
	
	/**
	 * Gets specific genre based on name of genre.
	 *
	 * @return an optional (bonus2) of the genre, or empty if genre not found
	 */
	public Optional<Genre> getGenre(String name) {
		try(Connection connection=DataSourceFactory.getConnection()){
			try(PreparedStatement statement=connection.prepareStatement("select * from genre where name=?")){
				statement.setString(1, name);
				try(ResultSet results=statement.executeQuery()){
					if(results.next()) {
						Genre genre= new Genre(results.getInt("idgenre"), results.getString("name"));
						return Optional.of(genre);
					}
				}
			}
			return Optional.empty();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Getting the genre based on name failed",e);
		}
		
	}

	/**
	 * Adds a genre to our database with a name.
	 */
	public void addGenre(String name) {
		try(Connection connection=DataSourceFactory.getConnection()){
			String query="insert into genre(name) "+ "values(?)";
			try(PreparedStatement statement=connection.prepareStatement(query)){
				statement.setString(1, name);
				statement.executeUpdate();	
				}
			}
		 catch(SQLException e) {
			throw new RuntimeException("Adding a genre failed.", e);
	}}}
