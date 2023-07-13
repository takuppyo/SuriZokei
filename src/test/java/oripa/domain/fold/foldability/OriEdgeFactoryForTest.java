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
package oripa.domain.fold.foldability;

import static org.mockito.Mockito.*;

import oripa.domain.fold.halfedge.OriEdge;
import oripa.domain.fold.halfedge.OriVertex;
import oripa.value.OriLine;

/**
 * @author OUCHI Koji
 *
 */
class OriEdgeFactoryForTest {
	public static OriEdge createEdgeSpy(final double x0, final double y0,
			final double x1, final double y1,
			final OriLine.Type type) {

		var sv = new OriVertex(x0, y0);
		var ev = new OriVertex(x1, y1);

		return createEdgeSpy(sv, ev, type);
	}

	public static OriEdge createEdgeSpy(final OriVertex sv, final OriVertex ev,
			final OriLine.Type type) {

		var spy = spy(new OriEdge(sv, ev, type.toInt()));

		return spy;
	}
}
