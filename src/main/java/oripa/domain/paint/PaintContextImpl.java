package oripa.domain.paint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Vector2d;

import oripa.domain.cptool.Painter;
import oripa.domain.creasepattern.CreasePattern;
import oripa.geom.GeomUtil;
import oripa.geom.RectangleDomain;
import oripa.value.OriLine;

class PaintContextImpl implements PaintContext {

	private CreasePattern creasePattern;
	private final CreasePatternUndoer undoer = new CreasePatternUndoerImpl(this);

	private final LinkedList<Vector2d> pickedVertices = new LinkedList<>();

	private final LinkedList<OriLine> pickedLines = new LinkedList<>();
	private boolean isPasting = false;

	private Vector2d candidateVertexToPick = new Vector2d();
	private OriLine candidateLineToPick = new OriLine();

	private OriLine.Type lineTypeOfNewLines;

	private AngleStep angleStep;

	private int gridDivNum;
	private ArrayList<Vector2d> gridPoints;

	private Collection<Vector2d> snapPoints = new ArrayList<Vector2d>();

	private CircleCopyParameter circleCopyParameter;
	private ArrayCopyParameter arrayCopyParameter;

	private final double pointEps = GeomUtil.pointEps();

	public PaintContextImpl() {
	}

	@Override
	public boolean isPasting() {
		return isPasting;
	}

	@Override
	public void startPasting() {
		this.isPasting = true;
	}

	@Override
	public void finishPasting() {
		this.isPasting = false;
	}

	@Override
	public void clear(final boolean unselect) {

		if (unselect) {
			pickedLines.stream().forEach(l -> l.selected = false);
		}

		pickedLines.clear();
		pickedVertices.clear();

		candidateLineToPick = null;
		candidateVertexToPick = null;

		snapPoints.clear();
	}

	@Override
	public List<Vector2d> getPickedVertices() {
		return Collections.unmodifiableList(pickedVertices);
	}

	@Override
	public List<OriLine> getPickedLines() {
		return Collections.unmodifiableList(pickedLines);
	}

	@Override
	public OriLine getLine(final int index) {
		return pickedLines.get(index);
	}

	@Override
	public Vector2d getVertex(final int index) {
		return pickedVertices.get(index);
	}

	@Override
	public void pushVertex(final Vector2d picked) {
		pickedVertices.addLast(picked);
	}

	@Override
	public void pushLine(final OriLine picked) {
		// picked.selected = true;
		pickedLines.addLast(picked);
	}

	@Override
	public Vector2d popVertex() {
		if (pickedVertices.isEmpty()) {
			return null;
		}

		return pickedVertices.removeLast();
	}

	@Override
	public OriLine popLine() {
		if (pickedLines.isEmpty()) {
			return null;
		}

		OriLine line = pickedLines.removeLast();
		line.selected = false;
		return line;
	}

	@Override
	public boolean removeLine(final OriLine line) {

		return pickedLines.remove(line);
	}

	@Override
	public Vector2d peekVertex() {
		return pickedVertices.peekLast();
	}

	@Override
	public OriLine peekLine() {
		return pickedLines.peekLast();
	}

	@Override
	public int getLineCount() {
		return pickedLines.size();
	}

	@Override
	public int getVertexCount() {
		return pickedVertices.size();
	}

	@Override
	public Vector2d getCandidateVertexToPick() {
		return candidateVertexToPick;
	}

	@Override
	public void setCandidateVertexToPick(final Vector2d candidate) {
		this.candidateVertexToPick = candidate;
	}

	@Override
	public OriLine getCandidateLineToPick() {
		return candidateLineToPick;
	}

	@Override
	public void setCandidateLineToPick(final OriLine candidate) {
		this.candidateLineToPick = candidate;
	}

	@Override
	public void setLineTypeOfNewLines(final OriLine.Type lineType) {
		lineTypeOfNewLines = lineType;
	}

	@Override
	public OriLine.Type getLineTypeOfNewLines() {
		return lineTypeOfNewLines;
	}

	@Override
	public Painter getPainter() {
		return new Painter(creasePattern, pointEps);
	}

	@Override
	public CreasePatternUndoer creasePatternUndo() {
		return undoer;
	}

	@Override
	public void setCreasePattern(final CreasePattern aCreasePattern) {
		creasePattern = aCreasePattern;
	}

	@Override
	public CreasePattern getCreasePattern() {
		return creasePattern;
	}

	@Override
	public RectangleDomain getPaperDomain() {
		return creasePattern.getPaperDomain();
	}

	@Override
	public void setAngleStep(final AngleStep step) {
		angleStep = step;
	}

	@Override
	public AngleStep getAngleStep() {
		return angleStep;
	}

	@Override
	public void setSnapPoints(final Collection<Vector2d> points) {
		snapPoints = points;
	}

	@Override
	public Collection<Vector2d> getSnapPoints() {
		return snapPoints;
	}

	@Override
	public void updateGrids() {
		gridPoints = new ArrayList<>();
		double paperSize = getCreasePattern().getPaperSize();

		double step = paperSize / gridDivNum;
		for (int ix = 0; ix < gridDivNum + 1; ix++) {
			for (int iy = 0; iy < gridDivNum + 1; iy++) {
				var paperDomain = getPaperDomain();
				double x = paperDomain.getLeft() + step * ix;
				double y = paperDomain.getTop() + step * iy;

				gridPoints.add(new Vector2d(x, y));
			}
		}
	}

	@Override
	public int getGridDivNum() {
		return gridDivNum;
	}

	@Override
	public void setGridDivNum(final int divNum) {
		gridDivNum = divNum;
		updateGrids();
	}

	@Override
	public Collection<Vector2d> getGrids() {
		return gridPoints;
	}

	@Override
	public String toString() {
		return "PaintContext: #line=" + pickedLines.size() +
				", #vertex=" + pickedVertices.size();
	}

	@Override
	public CircleCopyParameter getCircleCopyParameter() {
		return circleCopyParameter;
	}

	@Override
	public void setCircleCopyParameter(final CircleCopyParameter circleCopyParameter) {
		this.circleCopyParameter = circleCopyParameter;
	}

	@Override
	public void setArrayCopyParameter(final ArrayCopyParameter p) {
		arrayCopyParameter = p;
	}

	@Override
	public ArrayCopyParameter getArrayCopyParameter() {
		return arrayCopyParameter;
	}

	@Override
	public double getPointEps() {
		return pointEps;
	}

}
