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
package oripa.gui.presenter.creasepattern.enlarge;

import java.util.Collection;

import javax.vecmath.Vector2d;

import oripa.geom.RectangleDomain;
import oripa.value.OriLine;

/**
 * @author OUCHI Koji
 *
 */
interface Enlarger {

	Vector2d createOriginOfEnlargement(RectangleDomain originalDomain, Vector2d mouseStartPoint);

	RectangleDomain createEnlargedDomain(Vector2d mousePoint, Vector2d originOfEnlargement,
			Vector2d mouseStartPoint);

	Collection<OriLine> createEnlargedLines(Vector2d mousePoint, Vector2d originOfEnlargement,
			Vector2d mouseStartPoint, Collection<OriLine> pickedLines);

}