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
package oripa.persistence.foldformat;

import java.util.HashMap;
import java.util.Map;

import oripa.value.OriLine;

/**
 * @author OUCHI Koji
 *
 */
public class AssignmentConverter {

	private final Map<String, OriLine.Type> typeHash;

	public AssignmentConverter() {
		typeHash = new HashMap<String, OriLine.Type>();

		typeHash.put("B", OriLine.Type.CUT);
		typeHash.put("F", OriLine.Type.AUX);
		typeHash.put("M", OriLine.Type.MOUNTAIN);
		typeHash.put("V", OriLine.Type.VALLEY);
		typeHash.put("U", OriLine.Type.UNASSIGNED);
	}

	public String toFOLD(final OriLine.Type type) {
		switch (type) {
		case AUX:
			return "F";
		case CUT:
			return "B";
		case MOUNTAIN:
			return "M";
		case VALLEY:
			return "V";
		case UNASSIGNED:
			return "U";
		default:
			return null;
		}
	}

	public OriLine.Type fromFOLD(final String assignment) {
		return typeHash.get(assignment);
	}

}
