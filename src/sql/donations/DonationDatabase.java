package sql.donations;

import java.sql.SQLException;

import event.impl.DonationEvent;
import sql.SQLDatabase;

public class DonationDatabase extends SQLDatabase<Donation> {

	public DonationDatabase(String host, String table, String username, String password) {
		super(host, table, username, password, Donation.class);
	}

	@Override
	public void cycle() {
		try {
			for(Donation d : dao.queryBuilder().where().eq("claimed", false).and().eq("status", "completed").query()) {
				DonationEvent event = new DonationEvent(d);
				event.call();
				if(event.isConsumed()) {
					d.setClaimed(true);
					dao.update(d);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
