package sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SQLHandler extends Thread {

	private List<SQLDatabase<?>> databases;
	
	public SQLHandler() {
		this.databases = new ArrayList<>();
	}
	
	@Override
	public void run() {
		while(true) {
			for(SQLDatabase<?> db : databases) {
				db.cycle();
			}
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void shutdown() {
		databases.forEach(d -> {
			try {
				d.connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public List<SQLDatabase<?>> getDatabases() {
		return databases;
	}
}
