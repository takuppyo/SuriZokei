package oripa.gui.presenter.creasepattern.copypaste;

import javax.vecmath.Vector2d;

import oripa.domain.creasepattern.CreasePattern;
import oripa.domain.paint.PaintContext;
import oripa.domain.paint.copypaste.PastingOnVertex;
import oripa.domain.paint.copypaste.SelectionOriginHolder;
import oripa.domain.paint.copypaste.ShiftedLineFactory;
import oripa.gui.presenter.creasepattern.AbstractGraphicMouseAction;
import oripa.gui.presenter.creasepattern.CreasePatternViewContext;
import oripa.gui.presenter.creasepattern.EditMode;
import oripa.gui.presenter.creasepattern.geometry.NearestItemFinder;
import oripa.gui.presenter.creasepattern.geometry.NearestVertexFinder;
import oripa.gui.view.creasepattern.ObjectGraphicDrawer;
import oripa.value.OriLine;

public class PasteAction extends AbstractGraphicMouseAction {

	private final SelectionOriginHolder originHolder;
	private final ShiftedLineFactory factory = new ShiftedLineFactory();

	public PasteAction(final SelectionOriginHolder originHolder) {
		this.originHolder = originHolder;

		setEditMode(EditMode.INPUT);
		setNeedSelect(true);

		setActionState(new PastingOnVertex(originHolder));
	}

	@Override
	protected void recoverImpl(final PaintContext context) {
		context.clear(false);

		context.startPasting();

		CreasePattern creasePattern = context.getCreasePattern();

		creasePattern.stream()
				.filter(line -> line.selected)
				.forEach(line -> context.pushLine(line));
	}

	/**
	 * Clear context and mark lines as unselected.
	 */
	@Override
	public void destroy(final PaintContext context) {
		context.clear(true);
		context.finishPasting();
	}

	@Override
	public Vector2d onMove(final CreasePatternViewContext viewContext, final PaintContext paintContext,
			final boolean differentAction) {

		setCandidateVertexOnMove(viewContext, paintContext, differentAction);
		Vector2d closeVertex = paintContext.getCandidateVertexToPick();

		// to get the vertex which disappeared by cutting.
		Vector2d closeVertexOfLines = NearestItemFinder.pickVertexFromPickedLines(viewContext, paintContext);

		if (closeVertex == null) {
			closeVertex = closeVertexOfLines;
		}

		var current = viewContext.getLogicalMousePoint();
		if (closeVertex != null && closeVertexOfLines != null) {
			// get the nearest to current
			closeVertex = NearestVertexFinder.findNearestOf(
					current, closeVertex, closeVertexOfLines);

		}

		if (closeVertex == null) {
			closeVertex = viewContext.getLogicalMousePoint();
		}

		paintContext.setCandidateVertexToPick(closeVertex);

		return closeVertex;
	}

	@Override
	public void onDraw(final ObjectGraphicDrawer drawer, final CreasePatternViewContext viewContext,
			final PaintContext paintContext) {

		super.onDraw(drawer, viewContext, paintContext);

		drawPickCandidateVertex(drawer, viewContext, paintContext);

		Vector2d origin = originHolder.getOrigin(paintContext);

		if (origin == null) {
			return;
		}

		drawer.selectSelectedItemColor();
		drawVertex(drawer, viewContext, paintContext, origin);

		var candidateVertex = paintContext.getCandidateVertexToPick();

		Vector2d offset = candidateVertex == null ? factory.createOffset(origin, viewContext.getLogicalMousePoint())
				: factory.createOffset(origin, candidateVertex);

		drawer.selectAssistLineColor();

		// shift and draw the lines to be pasted.
		for (OriLine l : paintContext.getPickedLines()) {
			var shifted = factory.createShiftedLine(l, offset.x, offset.y);
			drawer.drawLine(shifted);
		}
	}
}
