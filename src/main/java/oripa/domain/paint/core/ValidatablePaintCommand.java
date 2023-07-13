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
package oripa.domain.paint.core;

import java.util.function.Supplier;

import oripa.domain.paint.PaintContext;
import oripa.util.Command;

/**
 * @author OUCHI Koji
 *
 */
public abstract class ValidatablePaintCommand implements Command {
	protected void validateCounts(final PaintContext context,
			final int correctVertexCount, final int correctLineCount) {
		if (context.getVertexCount() != correctVertexCount || context.getLineCount() != correctLineCount) {
			throw new IllegalStateException(
					String.format("wrong state. There should be %d pickedVertices and %d pickedLines",
							correctVertexCount, correctLineCount));
		}
	}

	protected void validateThat(final Supplier<Boolean> isValid, final String errorMessage) {
		if (!isValid.get()) {
			throw new IllegalStateException(errorMessage);
		}
	}
}
