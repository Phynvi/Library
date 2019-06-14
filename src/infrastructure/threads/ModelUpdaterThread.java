package infrastructure.threads;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

import entity.actor.model.Model;
import infrastructure.Core;
import infrastructure.CoreThread;

public final class ModelUpdaterThread extends CoreThread {

	private ArrayList<Model> updating = new ArrayList<>();
	private ArrayDeque<Model> queued = new ArrayDeque<>();

	private boolean running;

	public ModelUpdaterThread() {
		super("Model Updater", Thread.MAX_PRIORITY, false);
	}

	public synchronized void setForUpdating(Model model) {
		this.queued.add(model);
		if (!running) {
			Core.submitThread(this);
		}
	}

	@Override
	public void run() {
		this.running = true;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.running = false;
	}
}