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
package oripa.domain.paint.symmetric;

import javax.vecmath.Vector2d;

import oripa.domain.cptool.Painter;
import oripa.domain.paint.PaintContext;
import oripa.domain.paint.core.ValidatablePaintCommand;

/**
 * @author OUCHI Koji
 *
 */
public class SymmetricLineAdderCommand extends ValidatablePaintCommand {
	private final PaintContext context;
	private final boolean doWalk;

	public SymmetricLineAdderCommand(final PaintContext context, final boolean doWalk) {
		this.context = context;
		this.doWalk = doWalk;
	}

	@Override
	public void execute() {
		final int correctVertexCount = 3;
		final int correctLineCount = 0;
		validateCounts(context, correctVertexCount, correctLineCount);

		Vector2d first = context.getVertex(0);
		Vector2d second = context.getVertex(1);
		Vector2d third = context.getVertex(2);

		context.clear(false);

		context.creasePatternUndo().pushUndoInfo();

		Painter painter = context.getPainter();

		if (doWalk) {
			painter.addSymmetricLineAutoWalk(
					first, second, third, context.getLineTypeOfNewLines());
		} else {
			painter.addSymmetricLine(
					first, second, third, context.getLineTypeOfNewLines());
		}
	}

}
