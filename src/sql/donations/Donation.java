package sql.donations;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DatabaseTable(tableName = "payments")
public class Donation {
	
	@DatabaseField(columnName="id", id = true)
	private int id;
	
	@DatabaseField(columnName="item_name")
	private String itemName;
	
	@DatabaseField(columnName="item_number")
	private int productId;
	
	@DatabaseField(columnName="status")
	private String status;
	
	@DatabaseField(columnName="amount")
	private int donatedAmount;
	
	@DatabaseField(columnName="quantity")
	private int quantity;
	
	@DatabaseField(columnName="player_name")
	private String playerName;
	
	@DatabaseField(columnName="claimed")
	private boolean claimed;
}
