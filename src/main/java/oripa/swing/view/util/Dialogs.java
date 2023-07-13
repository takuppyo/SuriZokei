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
package oripa.swing.view.util;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * @author OUCHI Koji
 *
 */
public class Dialogs {
	public static void showErrorDialog(final Component parent, final String title,
			final Exception ex) {
		JOptionPane.showMessageDialog(parent,
				ex.getClass().getName() + ": " + ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 *
	 * @param parent
	 * @param title
	 * @param description
	 * @return true if user selected "yes".
	 */
	public static boolean showYesNoConfirmDialog(final Component parent, final String title, final String description) {
		return JOptionPane.showConfirmDialog(parent, description, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
	}
}
