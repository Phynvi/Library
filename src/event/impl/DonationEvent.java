package event.impl;

import event.Event;
import lombok.Getter;
import sql.donations.Donation;

@Getter
public class DonationEvent extends Event {

	private Donation donation;
	
	public DonationEvent(Donation donation) {
		this.donation = donation;
	}
}
