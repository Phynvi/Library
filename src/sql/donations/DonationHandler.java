package sql.donations;

import event.EventListener;
import event.EventManager;
import event.EventMethod;

public class DonationHandler implements EventListener, Runnable {

	private final EventManager manager;
	
	public DonationHandler(EventManager manager) {
		this.manager = manager;
		this.manager.registerEventListener(this);
	}
	
	@EventMethod
	public void handleDonation(DonationEvent event) {
		
	}

	@Override
	public void run() {
		
	}
}
