/**
 * ORIPA - Origami Pattern Editor
 * Copyright (C) 2005-2009 Jun Mitani http://mitani.cs.tsukuba.ac.jp/

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

package oripa.file;

/**
 * data class for persistent init data
 *
 */
public class InitData {
	private String lastUsedFile = ""; // dead property, remaining for
										// compatibility reasons with older init
										// files
	private String[] MRUFiles = new String[0];

	private boolean zeroLineWidth = false;

	// actually unassigned line is also controlled by this property.
	private boolean mvLineVisible = true;

	private boolean auxLineVisible = true;
	private boolean vertexVisible = true;

	public InitData() {
	}

	/**
	 *
	 * @param s
	 *            set array of most recently used file paths
	 */
	public void setMRUFiles(final String[] s) {
		MRUFiles = s;
	}

	/**
	 *
	 * @return array of most recently used file paths
	 */
	public String[] getMRUFiles() {
		return MRUFiles;
	}

	/**
	 *
	 * @param s
	 *            path string to file
	 */
	public void setLastUsedFile(final String s) {
		lastUsedFile = s;
	}

	/**
	 *
	 * @return last used file
	 */
	public String getLastUsedFile() {
		return lastUsedFile;
	}

	/**
	 *
	 * @param zeroLineWidth
	 *            sets zeroLineWidth
	 */
	public void setZeroLineWidth(final boolean zeroLineWidth) {
		this.zeroLineWidth = zeroLineWidth;
	}

	/**
	 *
	 * @return zeroLineWidth
	 */
	public boolean isZeroLineWidth() {
		return zeroLineWidth;
	}

	/**
	 * @return mvLineVisible
	 */
	public boolean isMvLineVisible() {
		return mvLineVisible;
	}

	/**
	 * @param mvLineVisible
	 *            Sets mvLineVisible
	 */
	public void setMvLineVisible(final boolean mvLineVisible) {
		this.mvLineVisible = mvLineVisible;
	}

	/**
	 * @return auxLineVisible
	 */
	public boolean isAuxLineVisible() {
		return auxLineVisible;
	}

	/**
	 * @param auxLineVisible
	 *            Sets auxLineVisible
	 */
	public void setAuxLineVisible(final boolean auxLineVisible) {
		this.auxLineVisible = auxLineVisible;
	}

	/**
	 * @return vertexVisible
	 */
	public boolean isVertexVisible() {
		return vertexVisible;
	}

	/**
	 * @param vertexVisible
	 *            Sets vertexVisible
	 */
	public void setVertexVisible(final boolean vertexVisible) {
		this.vertexVisible = vertexVisible;
	}

}
