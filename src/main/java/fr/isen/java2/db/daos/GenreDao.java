package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

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
					throw new RuntimeException("oops", e);}
	}

	public Genre getGenre(String name) {
		try(Connection connection=DataSourceFactory.getConnection()){
			try(PreparedStatement statement=connection.prepareStatement("select * from genre where name=?")){
				statement.setString(1, name);
				try(ResultSet results=statement.executeQuery()){
					if(results.next()) {
						return new Genre(results.getInt("idgenre"), results.getString("name"));
					}
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("oops",e);
		}
		
	}

	public void addGenre(String name) {
		try(Connection connection=DataSourceFactory.getConnection()){
			String query="insert into genre(name) "+ "values(?)";
			try(PreparedStatement statement=connection.prepareStatement(query)){
				statement.setString(1, name);
				statement.executeUpdate();	
				}
			}
		 catch(SQLException e) {
			throw new RuntimeException("oops", e);
	}}}
