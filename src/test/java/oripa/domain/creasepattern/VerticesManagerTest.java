package oripa.domain.creasepattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import javax.vecmath.Vector2d;

import org.junit.jupiter.api.Test;

import oripa.value.OriPoint;

public class VerticesManagerTest {

	@Test
	public void testAddVertex() {
		final double paperSize = 400;

		VerticesManager manager = new VerticesManager(paperSize, -paperSize / 2, -paperSize / 2);

		double interval = manager.getInterval();
		addAndCheckContains(manager, new Vector2d(0, 0));
		addAndCheckContains(manager, new Vector2d(interval, 0));
		addAndCheckContains(manager, new Vector2d(interval, interval));

		addAndCheckContains(manager, new Vector2d(-paperSize / 2 - 0.1, -paperSize / 2 - 0.1));
		addAndCheckContains(manager, new Vector2d(paperSize / 2 + 0.1, paperSize / 2 + 0.1));
	}

	private boolean managerContains(final NearVerticesGettable manager, final Vector2d vertex) {
		Collection<Vector2d> vertices;
		vertices = manager.getVerticesAround(vertex);
		return vertices.contains(vertex);

	}

	private void addAndCheckContains(final VerticesManager manager, final Vector2d target) {
		manager.add(target);

		Collection<Vector2d> vertices;
		vertices = manager.getVerticesAround(target);

		System.out.println("target: " + target);
		for (Vector2d v : vertices) {
			System.out.println(v);
		}

		assertTrue(managerContains(manager, target));

	}

	@Test
	public void testDuplicationManagement() {
		final double paperSize = 400;

		VerticesManager manager = new VerticesManager(paperSize, -paperSize / 2, -paperSize / 2);

		OriPoint p = new OriPoint(10, 10);

		manager.add(p);
		manager.add(p);

		manager.remove(p);
		assertTrue(managerContains(manager, p));

		manager.remove(p);
		assertFalse(managerContains(manager, p));
	}

}
