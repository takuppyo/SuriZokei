
/*
 * ORIPA - Origami Pattern Editor
 * Copyright (C) 2013-     ORIPA OSS Project  https://github.com/oripa/oripa
 * Copyright (C) 2005-2009 Jun Mitani         http://mitani.cs.tsukuba.ac.jp/
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package oripa.persistence.entity.exporter;

import static oripa.persistence.svg.SVGUtils.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import oripa.domain.fold.halfedge.OriFace;
import oripa.domain.fold.halfedge.OrigamiModel;
import oripa.domain.fold.origeom.OverlapRelation;
import oripa.persistence.entity.FoldedModelEntity;
import oripa.persistence.filetool.Exporter;
import oripa.persistence.svg.FacesToSvgConverter;

/**
 * @author OUCHI Koji / BETTINELLI Jean-Noel
 */
public class FoldedModelExporterSVG implements Exporter<FoldedModelEntity> {

	private final FacesToSvgConverter facesToSvgConverter;
	private final boolean faceOrderFlip;

	public FoldedModelExporterSVG(final boolean faceOrderFlip) {
		facesToSvgConverter = new FacesToSvgConverter();
		this.faceOrderFlip = faceOrderFlip;
	}

	@Override
	public boolean export(final FoldedModelEntity foldedModel, final String filepath, final Object configObj)
			throws IOException {
		OrigamiModel origamiModel = foldedModel.getOrigamiModel();
		OverlapRelation overlapRelation = foldedModel.getOverlapRelation();

		FaceSorter faceSorter = new FaceSorter(origamiModel.getFaces(), overlapRelation);

		List<OriFace> faces = faceSorter.sortFaces(faceOrderFlip);

		facesToSvgConverter.initDomain(faces, origamiModel.getPaperSize());

		var config = configObj == null ? new FoldedModelSVGConfig()
				: (FoldedModelSVGConfig) configObj;

		configure(config);

		try (var fw = new FileWriter(filepath);
				var bw = new BufferedWriter(fw)) {
			bw.write(SVG_START);
			bw.write(GRADIENTS_DEFINITION);
			bw.write(facesToSvgConverter.getSvgFaces(faces));
			bw.write(SVG_END_TAG);
		}
		return true;
	}

	private void configure(final FoldedModelSVGConfig config) {
		double faceStrokeWidth = config.getFaceStrokeWidth();
		double precreaseStrokeWidth = config.getPrecreaseStrokeWidth();

		if (faceOrderFlip) {
			facesToSvgConverter.setFaceStyles(getBackPathStyle(faceStrokeWidth).toString(),
					getFrontPathStyle(faceStrokeWidth).toString());
		} else {
			facesToSvgConverter.setFaceStyles(getFrontPathStyle(faceStrokeWidth).toString(),
					getBackPathStyle(faceStrokeWidth).toString());
		}
		facesToSvgConverter.setPrecreaseLineStyle(
				getPrecreasePathStyle(precreaseStrokeWidth).toString());
	}
}
