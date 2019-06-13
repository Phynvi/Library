package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLDatabase {
	
	private String database;
	private String username;
	private String password;
	private Connection connection = null;
	private Statement statement;
	private SQLHandler handler;

	protected SQLDatabase(String database, String username, String password, SQLHandler handler) {
		this.database = database;
		this.username = username;
		this.password = password;
		this.handler = handler;
	}

	protected abstract void cycle() throws SQLException;

	protected abstract void ping();

	protected void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
			connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", handler.getAddress(), database), username, password);
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void ping(String table, String variable) {
		try {
			query(String.format("SELECT * FROM `%s` WHERE `%s` = 'null'", table, variable));
		} catch (Exception e) {
			connect();
		}
	}

	protected ResultSet query(String query) throws SQLException {
		try {
			if (query.toLowerCase().startsWith("select")) {
				return statement.executeQuery(query);
			} else {
				statement.executeUpdate(query);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected Connection getConnection() {
		return connection;
	}
}
