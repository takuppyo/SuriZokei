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

package oripa.persistence.entity.exporter;

import static oripa.persistence.svg.SVGUtils.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import oripa.domain.fold.halfedge.OriFace;
import oripa.domain.fold.halfedge.OrigamiModel;
import oripa.persistence.filetool.Exporter;
import oripa.persistence.svg.FacesToSvgConverter;

/**
 * @author Bettinelli Jean-Noël
 *
 */
public class OrigamiModelExporterSVG implements Exporter<OrigamiModel> {

	private final FacesToSvgConverter facesToSvgConverter;

	/**
	 * Constructor
	 */
	public OrigamiModelExporterSVG() {
		facesToSvgConverter = new FacesToSvgConverter();
		facesToSvgConverter.setFaceStyles(PATH_STYLE_TRANSLUCENT);
		facesToSvgConverter.setPrecreaseLineStyle(THIN_LINE_STYLE);
	}

	@Override
	public boolean export(final OrigamiModel origamiModel, final String filepath, final Object configObj)
			throws IOException {

		List<OriFace> faces = origamiModel.getFaces();

		facesToSvgConverter.initDomain(faces, origamiModel.getPaperSize());

		try (var fw = new FileWriter(filepath);
				var bw = new BufferedWriter(fw)) {
			bw.write(SVG_START);
			bw.write(facesToSvgConverter.getSvgFaces(faces));
			bw.write(SVG_END_TAG);
		}

		return true;
	}

}
