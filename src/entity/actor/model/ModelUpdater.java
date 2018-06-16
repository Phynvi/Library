package entity.actor.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

import infrastructure.Tick;

public final class ModelUpdater extends Tick {

	private ArrayList<Model> updating = new ArrayList<>();
	private ArrayDeque<Model> queued = new ArrayDeque<>();

	@Override
	public void tick() {
		try {
			if (queued.size() > 0) {
				updating.addAll(queued);
				queued.clear();
			}

			if (updating.size() > 0) {
				Iterator<Model> iterator = updating.iterator();

				updateLoop: while (iterator.hasNext()) {
					Model model = iterator.next();
					if (!model.canBeUpdated()) {
						iterator.remove();

						model.setForUpdating(false);
						continue updateLoop;
					}
					model.update();
				}
				iterator = updating.iterator();
				while (iterator.hasNext()) {
					Model model = iterator.next();
					model.finishUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cancel() {
		//This method is never to be called, but if it is, then nothing should happen
	}

	/**
	 * Allows the specified {@code Model} to be updated by this {@code ModelUpdater}. It will be updated
	 * after it is removed from the queue that the {@code Model} is placed in.
	 * 
	 * @param model
	 *            the model to set to update
	 */
	public void setForUpdating(Model model) {
		if (model == null)
			throw new NullPointerException("A NULLED Model object cannot be set for updating");
		queued.add(model);
	}

}
