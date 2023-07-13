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

import java.io.File;
import java.util.Collection;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import oripa.gui.view.FrameView;
import oripa.gui.view.file.FileFilterProperty;
import oripa.gui.view.file.SavingFileChooserView;
import oripa.resource.ResourceHolder;
import oripa.resource.ResourceKey;
import oripa.resource.StringID;
import oripa.swing.view.util.Dialogs;

/**
 * @author OUCHI Koji
 *
 */
public class SavingFileChooser extends JFileChooser implements SavingFileChooserView {

	private final ResourceHolder resources = ResourceHolder.getInstance();

	public SavingFileChooser(final String path, final Collection<FileFilterProperty> filterProperties) {
		super(path);

		if (path != null) {
			File file = new File(path);
			this.setSelectedFile(file);
		}

		filterProperties.forEach(property -> {
			addChoosableFileFilter(new FileNameExtensionFilter(property.getDescription(), property.getExtensions()));
		});

		setAcceptAllFileFilterUsed(false);
	}

	@Override
	public boolean showDialog(final FrameView parent) {
		return showSaveDialog((JFrame) parent) == JFileChooser.APPROVE_OPTION;
	}

	@Override
	public String[] getSelectedFilterExtensions() {
		return ((FileNameExtensionFilter) this.getFileFilter()).getExtensions();
	}

	@Override
	public String getSelectedFilterDescription() {
		return ((FileNameExtensionFilter) this.getFileFilter()).getDescription();
	}

	@Override
	public boolean showOverwriteConfirmMessage() {
		return Dialogs.showYesNoConfirmDialog(this,
				resources.getString(ResourceKey.WARNING, StringID.Warning.SAVE_TITLE_ID),
				resources.getString(ResourceKey.WARNING, StringID.Warning.SAME_FILE_EXISTS_ID));
	}

	@Override
	public void showErrorMessage(final Exception e) {
		Dialogs.showErrorDialog(this,
				resources.getString(ResourceKey.ERROR, StringID.Error.SAVE_FAILED_ID), e);
	}

}
