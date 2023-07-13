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

package oripa.resource;

import java.util.ListResourceBundle;

//Old data. Not in use.
public class StringResource_en extends ListResourceBundle {

	static final Object[][] strings = {
			{
					StringID.Main.TITLE_ID,
					"ORIPA " + Version.ORIPA_VERSION
							+ ": Origami Pattern Editor" },
			{ StringID.Main.FILE_ID, "File" },
			{ StringID.Main.EDIT_ID, "Edit" },
			{ StringID.Main.HELP_ID, "Help" },
			{ StringID.Main.NEW_ID, "New" },
			{ StringID.Main.OPEN_ID, "Open" },
			{ StringID.Main.SAVE_ID, "Save" },
			{ StringID.Main.SAVE_AS_ID, "Save As ..." },
			{ StringID.Main.SAVE_AS_IMAGE_ID, "Save As Image ..." },
			{ StringID.Main.EXPORT_FOLD_ID, "Export (FOLD)" },
			{ StringID.Main.EXPORT_DXF_ID, "Export (DXF)" },
			{ StringID.Main.EXPORT_CP_ID, "Export (CP)" },
			{ StringID.Main.EXPORT_SVG_ID, "Export (SVG)" },
			{ StringID.EDIT_CONTOUR_ID, "Edit Contour" },
			{ StringID.Main.PROPERTY_ID, "Property" },
			{ StringID.Main.EXIT_ID, "Exit" },
			{ StringID.Main.UNDO_ID, "Undo" },
			{ StringID.Main.ABOUT_ID, "About" },
			{ StringID.COPY_PASTE_ID, "Copy and Paste" },

//			{ StringID.Default.FILE_NAME_ID, "NoTitle" },
			{ StringID.Main.DIALOG_TITLE_SAVE_ID, "Save" },
			{ StringID.Error.SAVE_FAILED_ID, "Failed to save." },
			{ StringID.Error.LOAD_FAILED_ID, "Failed to load." },
			{ StringID.Warning.SAME_FILE_EXISTS_ID,
					"Same name file exists. Over write?" },
			{ StringID.Main.ORIPA_FILE_ID, "ORIPA file" },
			{ StringID.Main.PICTURE_FILE_ID, "Picture file" },
			{ StringID.UI.INPUT_LINE_ID, "Input Line" },
			{ StringID.UI.SELECT_ID, "Select" },
			{ StringID.UI.DELETE_LINE_ID, "Delete Line" },
			{ StringID.UI.SHOW_GRID_ID, "Show Grid" },
			{ StringID.CHANGE_LINE_TYPE_ID, "Change Line Type" },
			{ StringID.ADD_VERTEX_ID, "Add Vertex" },
			{ StringID.DELETE_VERTEX_ID, "Delete Vertex" },
			{ StringID.UI.MEASURE_ID, "Measure" },
			{ StringID.UI.FOLD_ID, "Fold..." },
			{ StringID.UI.GRID_SIZE_CHANGE_ID, "Set" },
			{ StringID.UI.SHOW_VERTICES_ID, "Show Vertices" },
			{ StringID.UI.EDIT_MODE_ID, "Edit Mode" },
			{ StringID.UI.LINE_INPUT_MODE_ID, "Line Input Mode" },
			{ StringID.UI.LENGTH_ID, "Length" },
			{ StringID.UI.ANGLE_ID, "Angle" },
			{ StringID.UI.GRID_DIVIDE_NUM_ID, "Div Num" },

			{
					StringID.Warning.FOLD_FAILED_DUPLICATION_ID,
					"Failed to fold. Try again by deleting duplicating segments?" },
			{ StringID.Warning.FOLD_FAILED_WRONG_STRUCTURE_ID,
					"Failed to fold. It seems the pattern has basic problems." },

			{ StringID.ModelUI.DISPLAY_ID, "Display" },
			{ StringID.ModelUI.EXPORT_DXF_ID, "Export Model Line(DXF)" },
			{ StringID.ModelUI.INVERT_ID, "Invert" },
			{ StringID.ModelUI.SLIDE_FACES_ID, "Slide Faces" },
			{ StringID.ModelUI.DIRECTION_BASIC_ID,
					"    L: Rot R:Move Wheel:Zoom " },
			{ StringID.ModelUI.DISPLAY_TYPE_ID, "Drawing type" },
			{ StringID.ModelUI.FILL_ALPHA_ID, "Fill Transmission" },
			{ StringID.ModelUI.DRAW_LINES_ID, "Draw Lines" },
			{ StringID.ModelUI.TITLE_ID, "Expected Folded Origami" }
	};

	@Override
	protected Object[][] getContents() {
		return strings;
	}
}
