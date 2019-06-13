package sql;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public abstract class SQLDatabase<T> {

	protected ConnectionSource connection;
	protected Dao<T, String> dao;
	
	public SQLDatabase(String host, String table, String username, String password, Class<T> daoKlass) {
		try {
			this.connection = new JdbcConnectionSource(String.format("jdbc:mysql://%s/%s", host, table), username, password);
			this.dao = DaoManager.createDao(connection, daoKlass);
			TableUtils.createTableIfNotExists(connection, daoKlass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void cycle();
}
