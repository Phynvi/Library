package sql.donations;

import event.Event;

public class DonationEvent extends Event {

	public String username;
	public int productId;
	
	public DonationEvent(String username, int productId) {
		this.username = username;
		this.productId = productId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getProductId() {
		return productId;
	}
}
