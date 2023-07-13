/**
 * ORIPA - Origami Pattern Editor
 * Copyright (C) 2013-     ORIPA OSS Project  https://github.com/oripa/oripa
 * Copyright (C) 2005-2009 Jun Mitani         http://mitani.cs.tsukuba.ac.jp/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package oripa.gui.bind.state;

import java.util.function.Supplier;

import oripa.appstate.ApplicationState;
import oripa.gui.presenter.creasepattern.EditMode;
import oripa.gui.presenter.creasepattern.GraphicMouseAction;
import oripa.gui.presenter.creasepattern.MouseActionSetter;
import oripa.gui.presenter.creasepattern.MouseActionSetterFactory;

/**
 * @author OUCHI Koji
 *
 */
public class BindingObjectFactoryFacade {
	private final PaintBoundStateFactory stateFactory;
	private final MouseActionSetterFactory setterFactory;

	public BindingObjectFactoryFacade(final PaintBoundStateFactory stateFactory,
			final MouseActionSetterFactory setterFactory) {
		this.stateFactory = stateFactory;
		this.setterFactory = setterFactory;
	}

	public ApplicationState<EditMode> createState(
			final String id,
			final Supplier<Boolean> errorDetecter,
			final Runnable errorHandler) {
		return stateFactory.create(id, errorDetecter, errorHandler);
	}

	public ApplicationState<EditMode> createState(
			final String id) {
		return stateFactory.create(id, null, null);
	}

	public MouseActionSetterFactory getSetterFactory() {
		return setterFactory;
	}

	public MouseActionSetter createActionSetter(final GraphicMouseAction action) {
		return setterFactory.create(action);
	}
}
