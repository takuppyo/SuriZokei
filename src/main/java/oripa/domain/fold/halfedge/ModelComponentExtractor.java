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
package oripa.domain.fold.halfedge;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import oripa.domain.fold.origeom.OriGeomUtil;
import oripa.value.OriLine;

/**
 * @author OUCHI Koji
 *
 */
public class ModelComponentExtractor {

	public List<OriVertex> extractByBoundary(final List<OriVertex> wholeVertices,
			final OriFace boundaryFace, final double eps) {

		return wholeVertices.stream()
				.filter(vertex -> boundaryFace.isOnFaceInclusively(vertex.getPosition(), eps))
				.collect(Collectors.toList());
	}

	public List<OriLine> extractByBoundary(final Collection<OriLine> wholePrecreases,
			final OriFace boundaryFace, final double eps) {

		return wholePrecreases.stream()
				.filter(p -> OriGeomUtil.isSegmentIncludedInFace(boundaryFace, p, eps))
				.collect(Collectors.toList());
	}

}
