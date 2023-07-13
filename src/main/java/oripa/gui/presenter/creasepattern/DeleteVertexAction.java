package oripa.gui.presenter.creasepattern;

import oripa.domain.paint.PaintContext;
import oripa.domain.paint.deletevertex.DeletingVertex;
//import oripa.domain.paint.deletevertex.DeletingVertex;
import oripa.gui.view.creasepattern.ObjectGraphicDrawer;

public class DeleteVertexAction extends AbstractGraphicMouseAction {

	public DeleteVertexAction() {
		setEditMode(EditMode.VERTEX);

		setActionState(new DeletingVertex());

	}

	@Override
	public void onDraw(final ObjectGraphicDrawer drawer, final CreasePatternViewContext viewContext,
			final PaintContext paintContext) {

		super.onDraw(drawer, viewContext, paintContext);

		drawPickCandidateVertex(drawer, viewContext, paintContext);
	}
}
