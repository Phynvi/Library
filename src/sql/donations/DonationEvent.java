package sql.donations;

import event.Event;
import lombok.Builder;

@Builder
public class DonationEvent extends Event {

	private String username;
	private int productId;
	private int productAmount;
	private double dollarAmmount;
}
