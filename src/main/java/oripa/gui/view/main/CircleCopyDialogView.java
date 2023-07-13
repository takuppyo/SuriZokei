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
package oripa.gui.view.main;

import java.util.function.Supplier;

import oripa.gui.view.DialogView;

/**
 * @author OUCHI Koji
 *
 */
public interface CircleCopyDialogView extends DialogView {

	void showWrongCopyCountMessage();

	/**
	 *
	 * @param listener
	 *            returns true if the action completes, which suggests this view
	 *            to dispose.
	 */
	void setOKButtonListener(final Supplier<Boolean> listener);

	int getCopyCount();

	void setCopyCount(int copyCount);

	double getAngleDegree();

	void setAngleDegree(double angleDegree);

	double getCenterY();

	void setCenterY(double centerY);

	double getCenterX();

	void setCenterX(double centerX);
}
