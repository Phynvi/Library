package entity.actor.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

import infrastructure.Tick;

public final class ModelUpdater extends Tick {
	private ArrayList<Model> updating = new ArrayList<>();
	private ArrayDeque<Model> queued = new ArrayDeque<>();

	public void tick() {
		try {
			if (this.queued.size() > 0) {
				this.updating.addAll(this.queued);
				this.queued.clear();
			}

			if (this.updating.size() > 0) {
				Iterator<Model> iterator = this.updating.iterator();

				Model model;
				while (iterator.hasNext()) {
					model = (Model) iterator.next();
					if (model == null)
						continue;
					if (!model.canBeUpdated()) {
						iterator.remove();
						model.setForUpdating(false);
						model.finishUpdate();
					} else {
						model.update();
					}
				}

				iterator = this.updating.iterator();

				while (iterator.hasNext()) {
					model = (Model) iterator.next();
					model.finishUpdate();
				}
			}
		} catch (Exception var3) {
			var3.printStackTrace();
		}

	}

	public void cancel() {}

	public void setForUpdating(Model model) {
		this.queued.add(model);
		if (!this.isQueued())
			this.queue(30);
	}
}