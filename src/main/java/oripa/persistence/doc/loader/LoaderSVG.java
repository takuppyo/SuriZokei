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
package oripa.persistence.doc.loader;

import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.vecmath.Vector2d;

import oripa.doc.Doc;
import oripa.domain.cptool.ElementRemover;
import oripa.domain.cptool.Painter;
import oripa.domain.creasepattern.CreasePattern;
import oripa.domain.creasepattern.CreasePatternFactory;
import oripa.domain.fold.halfedge.OriVertex;
import oripa.domain.fold.halfedge.OrigamiModel;
import oripa.domain.fold.halfedge.OrigamiModelFactory;
import oripa.geom.GeomUtil;
import oripa.gui.presenter.foldability.FoldabilityScreenPresenter;
import oripa.value.OriLine;

/**
 * @author yamag
 *
 */
public class LoaderSVG implements DocLoader {
	private final double pointEps = GeomUtil.pointEps();
	private final ElementRemover elementRemover = new ElementRemover();
	private Collection<OriLine> overlappingLines;
	private Collection<OriVertex> violatingVertices;

	@Override
	public Doc load(final String filePath) {
		var lines = new ArrayList<OriLine>();
		Collection<OriVertex> checkVertex;

		try (var r = new FileReader(filePath)) {
			StreamTokenizer st = new StreamTokenizer(r);
			st.resetSyntax();
			st.wordChars('0', '9');
			st.wordChars('.', '.');
			st.wordChars('0', '\u00FF');
			st.wordChars('-', '-');
			st.wordChars('!', '!');
			st.wordChars('/', '/');
			st.whitespaceChars(' ', ' ');
			st.whitespaceChars('\t', '\t');
			st.whitespaceChars('\n', '\n');
			st.whitespaceChars('\r', '\r');
			st.whitespaceChars('"', '"');
			st.whitespaceChars(',', ',');
			st.whitespaceChars(';', ';');
			st.whitespaceChars(':', ':');

			int token;
			OriLine line;
			String t = "0";
			while (st.nextToken() != StreamTokenizer.TT_EOF) {
				line = new OriLine();
				lines.add(line);
//				System.out.println(st.sval);
				if (st.sval.equals("stroke")) {
					token = st.nextToken();

					switch (st.sval) {

					case "black":
						t = "2";
						break;
					case "red":
						t = "2";
						break;
					case "blue":
						t = "3";
						break;
					default:
						t = "0";
						break;
					}
//					System.out.println(t);
				}
//				line.setType(OriLine.Type.CUT);

				if ((st.sval).equals("M")) {

					try {
//						System.out.println(t);
						var lineType = OriLine.Type.fromInt(Integer.parseInt(t));
//						System.out.println("line type " + lineType);
						switch (lineType) {
						case CUT:
						case MOUNTAIN:
						case VALLEY:
							line.setType(lineType);
//							System.out.println("line type " + line.getType());
							break;
						default:
							line.setType(OriLine.Type.AUX);
							break;
						}
					} catch (IllegalArgumentException e) {
						line.setType(OriLine.Type.AUX);
					}

//					System.out.println("line type " + line.getType());
					token = st.nextToken();
					line.p0.x = Double.parseDouble(st.sval);
//					System.out.println(line.p0.x);
					token = st.nextToken();
					line.p0.y = Double.parseDouble(st.sval);
//					System.out.println(line.p0.y);
					token = st.nextToken();
					token = st.nextToken();
					line.p1.x = Double.parseDouble(st.sval);
//					System.out.println(line.p1.x);
					token = st.nextToken();
					line.p1.y = Double.parseDouble(st.sval);
//					System.out.println(line.p1.y);
//					System.out.println("meked line");
				}
			}
			System.out.println("end");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*削除処理の名残
		CreasePatternFactory factory = new CreasePatternFactory();
		CreasePattern creasePattern = factory
				.createCreasePattern(lines);
		Doc doc = new Doc();
		doc.setCreasePattern(creasePattern);
		
		Vector2d lt = doc.getPaperDomain().getLeftTop();
		Vector2d lb = doc.getPaperDomain().getLeftBottom();
		Vector2d rt = doc.getPaperDomain().getRightTop();
		Vector2d rb = doc.getPaperDomain().getRightBottom();
		
		var left = new OriLine(lt, lb, OriLine.Type.CUT);
		var top = new OriLine(rt, lt, OriLine.Type.CUT);
		var right = new OriLine(rt, rb, OriLine.Type.CUT);
		var bottom = new OriLine(rb, lb, OriLine.Type.CUT);*/

		CreasePatternFactory factory = new CreasePatternFactory();
		CreasePattern creasePattern = factory
				.createCreasePatternSVG(lines);

		Vector2d lt = creasePattern.getPaperDomain().getLeftTop();
		Vector2d lb = creasePattern.getPaperDomain().getLeftBottom();
		Vector2d rt = creasePattern.getPaperDomain().getRightTop();
		Vector2d rb = creasePattern.getPaperDomain().getRightBottom();

		creasePattern.clear();

		var left = new OriLine(lt, lb, OriLine.Type.CUT);
		var top = new OriLine(rt, lt, OriLine.Type.CUT);
		var right = new OriLine(rt, rb, OriLine.Type.CUT);
		var bottom = new OriLine(rb, lb, OriLine.Type.CUT);
		var painter = new Painter(creasePattern, pointEps);

		painter.addLines(lines);
		painter.addLine(left);
		painter.addLine(right);
		painter.addLine(top);
		painter.addLine(bottom);

//		creasePattern.add(left);
//		creasePattern.add(right);
//		creasePattern.add(top);
//		creasePattern.add(bottom);

		creasePattern = painter.getCreasePattern();

		Doc doc = new Doc();
		doc.setCreasePattern(creasePattern);

		OrigamiModelFactory modelFactory = new OrigamiModelFactory();
		OrigamiModel origamiModel = modelFactory.createOrigamiModel(
				creasePattern,
				pointEps);

		var screenPresenter = new FoldabilityScreenPresenter(
//				view.getFoldabilityScreenView(),
				origamiModel,
				creasePattern,
				pointEps);
		screenPresenter.setModel_SVG();
		violatingVertices = screenPresenter.getViolatingVertices();
//		System.out.println(violatingVertices);
//		violatingVertices.toArray();
		checkVertex = violatingVertices.stream()
				.distinct()
				.collect(Collectors.toList());
		System.out.println(checkVertex);
		System.out.println("size:" + checkVertex.size());
		System.out.println("class:" + checkVertex.getClass());

		checkVertex.forEach(v -> System.out.println(v.edgeCount()));
//		System.out.println(doc.getCreasePattern().toString());

		return doc;

	}
}
