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
package oripa.persistence.filetool;

import java.util.Arrays;

/**
 * @author Koji
 *
 */
public interface FileTypeProperty<Data> {

	/**
	 *
	 * @return extensions for this file type. Each extension is supposed to be
	 *         without period (.). For example, "jpg" is acceptable but ".jpg"
	 *         is not.
	 */
	String[] getExtensions();

	default boolean extensionsMatch(final String filePath) {
		if (filePath == null) {
			return false;
		}
		return Arrays.asList(getExtensions()).stream()
				.anyMatch(extension -> filePath.endsWith("." + extension));
	}

	/**
	 * @return a text for identifying file type.
	 */
	String getKeyText();

	Integer getOrder();

	Loader<Data> getLoader();

	Exporter<Data> getExporter();
}