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
package oripa.domain.paint.byvalue;

import javax.vecmath.Vector2d;

import oripa.domain.paint.PaintContext;
import oripa.domain.paint.core.ValidatablePaintCommand;

/**
 * @author OUCHI Koji
 *
 */
public class AngleMeasureCommand extends ValidatablePaintCommand {
	private final PaintContext context;
	private final ByValueContext valueSetting;

	public AngleMeasureCommand(final PaintContext context, final ByValueContext valueSetting) {
		this.context = context;
		this.valueSetting = valueSetting;
	}

	@Override
	public void execute() {
		final int correctVertexCount = 3;
		final int correctLineCount = 0;
		validateCounts(context, correctVertexCount, correctLineCount);

		Vector2d first = context.getVertex(0);
		Vector2d second = context.getVertex(1);
		Vector2d third = context.getVertex(2);

		Vector2d dir1 = new Vector2d(third);
		Vector2d dir2 = new Vector2d(first);
		dir1.sub(second);
		dir2.sub(second);

		double deg_angle = Math.toDegrees(dir1.angle(dir2));

		valueSetting.setAngle(deg_angle);

		context.clear(false);
	}
}
