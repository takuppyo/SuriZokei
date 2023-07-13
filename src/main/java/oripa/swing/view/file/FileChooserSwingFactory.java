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
package oripa.swing.view.file;

import java.util.Collection;

import oripa.gui.view.file.FileChooserFactory;
import oripa.gui.view.file.FileFilterProperty;
import oripa.gui.view.file.LoadingFileChooserView;
import oripa.gui.view.file.SavingFileChooserView;

/**
 * @author OUCHI Koji
 *
 */
public class FileChooserSwingFactory implements FileChooserFactory {

	@Override
	public SavingFileChooserView createForSaving(final String path,
			final Collection<FileFilterProperty> filterProperties) {
		return new SavingFileChooser(path, filterProperties);
	}

	@Override
	public LoadingFileChooserView createForLoading(final String path,
			final Collection<FileFilterProperty> filterProperties) {
		return new LoadingFileChooser(path, filterProperties);
	}

}
