package oripa.domain.creasepattern;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.vecmath.Vector2d;

/**
 * For a fast access to vertex
 *
 * @author koji
 *
 */
class VerticesManager implements NearVerticesGettable {

	/*
	 * divides paper equally in order to localize access to vertices
	 */
	public static final int divNum = 32;

	private final double interval;
//	private double paperCenter;
	private final double paperLeft, paperTop;

	/**
	 * the index of divided paper area. A given point is converted to the index
	 * it should belongs to.
	 *
	 * @author Koji
	 *
	 */
	private class AreaPosition {
		public int x, y;

		/**
		 * doubles point to index
		 */
		public AreaPosition(final Vector2d v) {
			x = toDiv(v.x, paperLeft);
			y = toDiv(v.y, paperTop);
		}
	}

	/**
	 * Computes a index on one axis.
	 *
	 * @param p
	 * @return
	 */
	private int toDiv(final double p, final double p0) {
		int div = (int) ((p - p0) / interval);

		if (div < 0) {
			return 0;
		}

		if (div >= divNum) {
			return divNum - 1;
		}

		return div;
	}

	/**
	 * [div_x][div_y] is a vertices in the divided area.
	 */
	@SuppressWarnings("unchecked")
	private final Set<Vector2d>[][] vertices = new Set[divNum][divNum];
	/**
	 * count existence of same values.
	 */
	private final Map<Vector2d, Integer> counts = new ConcurrentHashMap<>();

	/**
	 * Constructor to initialize fields.
	 *
	 * @param paperSize
	 *            paper size in double.
	 * @param paperLeft
	 *            the smaller x coordinate of the corners of the rectangle sheet
	 *            of paper
	 * @param paperTop
	 *            the smaller y coordinate of the corners of the rectangle sheet
	 *            of paper
	 */
	public VerticesManager(final double paperSize, final double paperLeft, final double paperTop) {
		interval = paperSize / divNum;
		this.paperLeft = paperLeft;
		this.paperTop = paperTop;

		// allocate memory for each area
		for (int x = 0; x < divNum; x++) {
			for (int y = 0; y < divNum; y++) {
				vertices[x][y] = Collections.newSetFromMap(
						new ConcurrentHashMap<Vector2d, Boolean>());
			}
		}

	}

	double getInterval() {
		return interval;
	}

	/**
	 * remove all vertices.
	 */
	public void clear() {
		for (int x = 0; x < divNum; x++) {
			for (int y = 0; y < divNum; y++) {
				vertices[x][y].clear();
			}
		}
		counts.clear();
	}

	/**
	 * return vertices in the specified area.
	 *
	 * @param ap
	 *            index of area.
	 * @return vertices in the specified area.
	 */
	private Set<Vector2d> getVertices(final AreaPosition ap) {
		return vertices[ap.x][ap.y];
	}

	/**
	 * add given vertex to appropriate area.
	 *
	 * @param v
	 *            vertex to be managed by this class.
	 */
	public void add(final Vector2d v) {

		Set<Vector2d> vertices = getVertices(new AreaPosition(v));

		// v is a new value
		if (vertices.add(v)) {
			counts.put(v, 1);
			return;
		}

		// count duplication.
		Integer count = counts.get(v);
		counts.put(v, count + 1);

	}

	/*
	 * (non Javadoc)
	 *
	 * @see
	 * oripa.domain.creasepattern.NearVerticesGettable#getAround(javax.vecmath.
	 * Vector2d)
	 */
	@Override
	public Collection<Vector2d> getVerticesAround(final Vector2d v) {
		AreaPosition ap = new AreaPosition(v);
		return getVertices(ap);
	}

	/**
	 * remove the given vertex from this class.
	 *
	 * @param v
	 */
	public void remove(final Vector2d v) {
		AreaPosition ap = new AreaPosition(v);
		Integer count = counts.get(v);

		// should never happen.
		if (count <= 0) {
			throw new IllegalStateException("Nothing to remove");
		}

		// No longer same vertices exist.s
		if (count == 1) {
			getVertices(ap).remove(v);
			counts.remove(v);
			return;
		}

		// decrement existence.
		counts.put(v, count - 1);
	}

	/*
	 * (non Javadoc)
	 *
	 * @see oripa.domain.creasepattern.NearVerticesGettable#getArea(double,
	 * double, double)
	 */
	@Override
	public Collection<Collection<Vector2d>> getVerticesInArea(
			final double x, final double y, final double distance) {

		Collection<Collection<Vector2d>> result = new LinkedList<>();

		int leftDiv = toDiv(x - distance, paperLeft);
		int rightDiv = toDiv(x + distance, paperLeft);
		int topDiv = toDiv(y - distance, paperTop);
		int bottomDiv = toDiv(y + distance, paperTop);

		for (int xDiv = leftDiv; xDiv <= rightDiv; xDiv++) {
			for (int yDiv = topDiv; yDiv <= bottomDiv; yDiv++) {
				result.add(vertices[xDiv][yDiv]);
			}
		}

		return result;
	}

	public boolean isEmpty() {
		for (Set<Vector2d>[] vertexSets : vertices) {
			for (Set<Vector2d> vertexSet : vertexSets) {
				if (!vertexSet.isEmpty()) {
					return false;
				}
			}
		}

		return true;
	}
}
