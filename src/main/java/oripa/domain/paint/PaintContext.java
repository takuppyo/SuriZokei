package oripa.domain.paint;

import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector2d;

import oripa.domain.cptool.Painter;
import oripa.value.OriLine;

/**
 * This interface holds current state of GUI interaction for paint inputting.
 *
 * @author Koji
 *
 */
public interface PaintContext extends CreasePatternHolder {

	// =================================================================================
	// Properties used by action state
	// =================================================================================

	// ---------------------------------------------------------------
	// State of input instruction

	/**
	 *
	 * @return true if user is trying to paste selected lines
	 */
	public abstract boolean isPasting();

	/**
	 * notify the painting algorithm that the user started pasting.
	 */
	public abstract void startPasting();

	/**
	 * notify the painting algorithm that the user finished pasting.
	 */
	public abstract void finishPasting();

	// ---------------------------------------------------------------
	// Values Picked by User

	/**
	 * remove all lines and all vertices in this context.
	 *
	 * @param unselect
	 *            true if the removed lines should be marked as unselected.
	 */
	public abstract void clear(boolean unselect);

	/**
	 *
	 * @return unmodifiable list of lines which user picked.
	 */
	public abstract List<OriLine> getPickedLines();

	/**
	 *
	 * @return unmodifiable list of vertices which user picked.
	 */
	public abstract List<Vector2d> getPickedVertices();

	/**
	 *
	 * @param index
	 * @return a line at specified position in the order of user selection
	 */
	public abstract OriLine getLine(int index);

	/**
	 *
	 * @param index
	 * @return a vertex at specified position in the order of user selection
	 */
	public abstract Vector2d getVertex(int index);

	/**
	 *
	 * @param picked
	 *            line to be stored as the latest
	 */
	public abstract void pushLine(OriLine picked);

	/**
	 * pop the last pushed line and mark it unselected.
	 *
	 * @return popped line. null if no line is pushed.
	 */
	public abstract OriLine popLine();

	/**
	 *
	 * @param picked
	 *            vertex to be stored as the latest
	 */
	public abstract void pushVertex(Vector2d picked);

	/**
	 * pop the last pushed vertex.
	 *
	 * @return popped vertex. null if no vertex is pushed.
	 */
	public abstract Vector2d popVertex();

	/**
	 * performs the same as {@link List#remove(Object o)}.
	 *
	 * @param line
	 * @return
	 */
	public abstract boolean removeLine(OriLine line);

	/**
	 *
	 * @return the latest vertex
	 */
	public abstract Vector2d peekVertex();

	/**
	 *
	 * @return the latest line
	 */
	public abstract OriLine peekLine();

	/**
	 *
	 * @return count of lines in this context
	 */
	public abstract int getLineCount();

	/**
	 *
	 * @return count of vertices in this context
	 */
	public abstract int getVertexCount();

	// ---------------------------------------------------------------
	// Misc

	public abstract Painter getPainter();

	void setLineTypeOfNewLines(OriLine.Type lineType);

	OriLine.Type getLineTypeOfNewLines();

	public abstract void setCandidateLineToPick(OriLine pickCandidateL);

	public abstract OriLine getCandidateLineToPick();

	public abstract void setCandidateVertexToPick(Vector2d pickCandidateV);

	public abstract Vector2d getCandidateVertexToPick();

	public abstract CreasePatternUndoer creasePatternUndo();

	public abstract void setAngleStep(AngleStep step);

	public abstract AngleStep getAngleStep();

	public abstract void setSnapPoints(Collection<Vector2d> points);

	public abstract Collection<Vector2d> getSnapPoints();

	/**
	 * sets division number of grid. should update grid points for
	 * {@link #getGrids()}.
	 *
	 * @param divNum
	 */
	public abstract void setGridDivNum(int divNum);

	public abstract int getGridDivNum();

	public abstract void updateGrids();

	/**
	 * gets current grids.
	 *
	 * @return
	 */
	public abstract Collection<Vector2d> getGrids();

	public void setCircleCopyParameter(CircleCopyParameter p);

	public CircleCopyParameter getCircleCopyParameter();

	public void setArrayCopyParameter(ArrayCopyParameter p);

	public ArrayCopyParameter getArrayCopyParameter();

	public double getPointEps();
}