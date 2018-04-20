package util.loot;

import java.util.Collection;

import event.Event;

public class LootableItemDropEvent extends Event {

	private final DropTableHolder holder;
	private final Collection<? extends LootableItem> items;

	public LootableItemDropEvent(DropTableHolder holder, Collection<? extends LootableItem> items) {
		this.holder = holder;
		this.items = items;
	}

	public DropTableHolder getHolder() {
		return holder;
	}

	public Collection<? extends LootableItem> getItems() {
		return items;
	}

}
