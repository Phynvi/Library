package sql.donations;

import java.sql.SQLException;

import sql.SQLDatabase;

public class DonationDatabase extends SQLDatabase<Donation> {

	public DonationDatabase(String host, String table, String username, String password) {
		super(host, table, username, password, Donation.class);
	}

	@Override
	public void cycle() {
		try {
			for(Donation d : dao.queryBuilder().where().eq("claimed", false).and().eq("status", "completed").query()) {
				DonationEvent event = new DonationEvent(d.player_name, d.id);
				event.call();
				if(event.isConsumed()) {
					d.claimed = true;
					dao.update(d);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
