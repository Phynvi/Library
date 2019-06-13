package sql.donations;

import java.sql.ResultSet;
import java.sql.SQLException;

import sql.SQLDatabase;
import sql.SQLHandler;

public class DonationHandler extends SQLDatabase {

	public DonationHandler(String database, String username, String password, SQLHandler handler) {
		super(database, username, password, handler);
	}

	@Override
	public void cycle() throws SQLException {
		ResultSet results = query(String.format("SELECT * FROM `payments`"));
		if (results == null) {
			return;
		}
		while (results.next()) {	
			String username = results.getString("username");
			int productId = results.getInt("productid");
			DonationEvent event = new DonationEvent(username, productId);
			event.call();
			if(event.isConsumed()) {
				query(String.format("DELETE FROM `payments` WHERE `username`='%s' LIMIT 1", username));
			}
		}
	}

	@Override
	public void ping() {
		super.ping("payments", "username");
	}
}
