package oripa.gui.bind.state;

import java.util.function.Supplier;

import oripa.appstate.ApplicationState;
import oripa.appstate.StateManager;
import oripa.gui.presenter.creasepattern.EditMode;
import oripa.gui.presenter.creasepattern.GraphicMouseAction;
import oripa.gui.presenter.creasepattern.MouseActionSetterFactory;
import oripa.gui.viewchange.ChangeViewSetting;

/**
 * Helper class
 *
 * @author Koji
 *
 */
class LocalPaintBoundStateFactory {

	private final StateManager<EditMode> stateManager;
	private final MouseActionSetterFactory setterFactory;

	private final Runnable[] basicActions;

	/**
	 *
	 * @param stateManager
	 * @param basicActions
	 *            Actions for all created states.
	 */
	public LocalPaintBoundStateFactory(
			final StateManager<EditMode> stateManager,
			final MouseActionSetterFactory setterFactory,
			final Runnable[] basicActions) {
		this.stateManager = stateManager;
		this.setterFactory = setterFactory;
		this.basicActions = basicActions;
	}

	/**
	 * Create a state with error handler.
	 *
	 * @param mouseAction
	 * @param errorDetecter
	 *            should detect whether the application is ready to perform the
	 *            action of the new state or not. This should return true if an
	 *            error occurs. This can be null if no error check is needed.
	 * @param errorHandler
	 *            should handle error the {@code errorDetecter} detected. This
	 *            can be null if no error check is needed.
	 * @param changeHint
	 *            event handler to change a hint of painting.
	 * @param actions
	 *            Additional actions.
	 * @return
	 */
	public ApplicationState<EditMode> create(
			final GraphicMouseAction mouseAction,
			final Supplier<Boolean> errorDetecter,
			final Runnable errorHandler,
			final ChangeViewSetting changeHint,
			final Runnable[] actions) {

		PaintBoundState state = new PaintBoundState(
				stateManager, errorDetecter, errorHandler,
				mouseAction.getEditMode(), setterFactory.create(mouseAction), changeHint, basicActions);

		state.addActions(actions);
		// state.setErrorListeners(errorDetecter, errorHandler);

		return state;
	}

	/**
	 *
	 * Create a state.
	 *
	 * @param mouseAction
	 * @param changeHint
	 *            event handler to change a hint of painting.
	 * @param actions
	 *            Additional actions.
	 * @return
	 */
	public ApplicationState<EditMode> create(
			final GraphicMouseAction mouseAction,
			final ChangeViewSetting changeHint,
			final Runnable[] actions) {

		ApplicationState<EditMode> state = new PaintBoundState(
				stateManager, mouseAction.getEditMode(), setterFactory.create(mouseAction), changeHint,
				basicActions);

		state.addActions(actions);

		return state;

	}

}
