package sql;

import java.util.ArrayList;
import java.util.List;

public class SQLHandler extends Thread {

	private String address;
	private List<SQLDatabase> databases;

	public SQLHandler(String address, String username, String password) {
		this.address = address; 
		this.databases = new ArrayList<>();
	}
	
	@Override
	public void run() {
		while(true) {
			for (SQLDatabase d : databases) {
				try {
					if (d.getConnection() == null) {
						d.connect();
					} else {
						d.ping();
					}
					d.cycle();
					Thread.sleep(3000L);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public List<SQLDatabase> getDatabases() {
		return databases;
	}

	public String getAddress() {
		return address;
	}
}
