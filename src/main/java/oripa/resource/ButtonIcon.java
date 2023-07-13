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
package oripa.resource;

/**
 * Each enum value provides the paths of two icons that are in the style of
 * "icon/filename.gif" for default icon and "icon/filename_p.gif" for selected
 * state icon. If you would like to add a button with icon, draw the icons, put
 * them at icon directory, and name them like "foo.gif" and "foo_p.gif".
 *
 * @author OUCHI Koji
 *
 */
public enum ButtonIcon implements ButtonIconResource {
	SELECT("select"),
	ENLARGE("enlarge"),
	DIRECT_V("segment"),
	ON_V("line"),
	PERPENDICULAR_BISECTOR("pbisector"),
	BISECTOR("bisector"),
	TRIANGLE("incenter"),
	VERTICAL("vertical"),
	SYMMETRIC("symmetry"),
	MIRROR("mirror"),
	BY_VALUE("by_value"),
	ANGLE_SNAP("angle"),
	SUGGESTION("suggestion");

	private static final String DIRECTORY = "icon/";

	private final String fileName;

	private ButtonIcon(final String fileName) {
		this.fileName = fileName;
	}

	private String getPath() {
		return getPath("");
	}

	private String getPath(final String fileNameSuffix) {
		return DIRECTORY + fileName + fileNameSuffix + ".gif";
	}

	@Override
	public String getIconResourcePath() {
		return getPath();
	}

	@Override
	public String getSelectedIconResourcePath() {
		return getPath("_p");
	}
}
