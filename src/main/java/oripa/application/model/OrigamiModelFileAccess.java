/**
 * ORIPA - Origami Pattern Editor Copyright (C) 2013- ORIPA OSS Project
 * https://github.com/oripa/oripa Copyright (C) 2005-2009 Jun Mitani
 * http://mitani.cs.tsukuba.ac.jp/
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package oripa.application.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import oripa.application.FileAccessService;
import oripa.domain.fold.halfedge.OrigamiModel;
import oripa.persistence.dao.AbstractFileDAO;
import oripa.persistence.dao.DataAccessObject;
import oripa.persistence.filetool.FileTypeProperty;
import oripa.persistence.filetool.FileVersionError;
import oripa.persistence.filetool.WrongDataFormatException;

/**
 * A service object between the {@link DataAccessObject} and the
 * {@link OrigamiModel}.
 *
 * @author OUCHI Koji
 *
 */
public class OrigamiModelFileAccess extends FileAccessService<OrigamiModel> {
	private final AbstractFileDAO<OrigamiModel> dao;

	/**
	 * Constructor
	 */
	public OrigamiModelFileAccess(final AbstractFileDAO<OrigamiModel> dao) {
		this.dao = dao;
	}

	@Override
	protected AbstractFileDAO<OrigamiModel> getFileDAO() {
		return dao;
	}

	@Override
	public final void saveFile(final OrigamiModel document,
			final String path, final FileTypeProperty<OrigamiModel> type)
			throws IOException, IllegalArgumentException {

		if (type == null) {
			dao.save(document, path);
		} else {
			dao.save(document, path, type);
		}
	}

	@Override
	public Optional<OrigamiModel> loadFile(final String filePath) throws FileVersionError, IllegalArgumentException,
			WrongDataFormatException, IOException, FileNotFoundException {
		throw new RuntimeException("Not implemented yet.");
	}
}
