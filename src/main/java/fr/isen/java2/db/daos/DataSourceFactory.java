package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceFactory {

	private static final String JDBC_URL = "jdbc:sqlite:sqlite.db";

	private DataSourceFactory() {
		// This is a static class that should not be instantiated.
		// Here's a way to remember it when this class will have 2K lines and you come
		// back to it in 2 years
		throw new IllegalStateException("This is a static class that should not be instantiated");
	}

	/**
	 * @return a connection to the SQLite Database
	 * 
	 */
	 public static Connection getConnection() {
	        try {
	            return DriverManager.getConnection(JDBC_URL);
	        } catch (SQLException e) {
	            throw new RuntimeException("Unable to get DB connection", e);
	        }
	    }
}