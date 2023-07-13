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

package oripa.domain.fold.subface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.vecmath.Vector2d;

import oripa.domain.fold.halfedge.OriFace;
import oripa.domain.fold.origeom.OverlapRelation;
import oripa.domain.fold.stackcond.StackConditionOf3Faces;
import oripa.domain.fold.stackcond.StackConditionOf4Faces;

public class SubFace {

	private final OriFace outline;
	/**
	 * faces containing this subface.
	 */
	private final List<OriFace> parentFaces = new ArrayList<>();

	private final List<StackConditionOf4Faces> condition4s = new ArrayList<>();
	private final List<StackConditionOf3Faces> condition3s = new ArrayList<>();

	private List<OriFace> modelFaces;

	/**
	 *
	 * @param outline
	 *            A face object describing the shape of this subface.
	 */
	public SubFace(final OriFace outline) {
		this.outline = outline;
	}

	/**
	 * Creates all possible local layer orders. All parent faces should be added
	 * to this subface before this method is called.
	 *
	 * @param modelFaces
	 *            all faces of inputted model.
	 * @param overlapRelation
	 *            overlap relation matrix.
	 * @param parallel
	 *            {@code true} if the algorithm should try parallelization.
	 * @return a list of possible local layer orders. {@code null} if order is
	 *         uniquely determined by overlap relation.
	 */
	public List<List<OriFace>> createLocalLayerOrders(final List<OriFace> modelFaces,
			final OverlapRelation overlapRelation, final boolean parallel) {
		return solveLocalLayerOrders(modelFaces, overlapRelation, parallel);
	}

	private List<List<OriFace>> solveLocalLayerOrders(final List<OriFace> modelFaces,
			final OverlapRelation overlapRelation,
			final boolean parallel) {

		this.modelFaces = modelFaces;

		// Exit if the order is already settled
		if (isLocalLayerOrderDeterminedByGlobal(overlapRelation)) {
			return null;
		}

		// A list of orders of faces where the faces include this subface. Each
		// order is correct on this subface but it can be wrong on other
		// subfaces.
		var localLayerOrders = Collections.synchronizedList(new ArrayList<List<OriFace>>());
		var localLayerOrder = new ArrayList<OriFace>();
		var alreadyInLocalLayerOrder = new boolean[modelFaces.size()];
		var indexOnOrdering = new HashMap<OriFace, Integer>();
		var stackConditionAggregate = new StackConditionAggregate();

		for (int i = 0; i < parentFaces.size(); i++) {
			localLayerOrder.add(null);
		}

		stackConditionAggregate.prepareConditionsOf2Faces(parentFaces, overlapRelation);
		stackConditionAggregate.prepareConditionsOf3Faces(parentFaces, overlapRelation, condition3s);
		stackConditionAggregate.prepareConditionsOf4Faces(parentFaces, overlapRelation, condition4s);

		for (OriFace f : parentFaces) {
			alreadyInLocalLayerOrder[f.getFaceID()] = false;
			indexOnOrdering.put(f, -1);
		}

		// Heuristic: a face with many stack conditions of 2 faces should be at
		// some place with a large index on local layer order.
		// Trying such face in early stage reduces failures at deep positions of
		// the search tree.
		// (earlier failure is better.)
		var candidateFaces = parentFaces.stream()
				.sorted(Comparator.comparing(stackConditionAggregate::getCountOfConditionsOf2Faces).reversed())
				.collect(Collectors.toList());

		// From the bottom
		sort(candidateFaces,
				localLayerOrders,
				localLayerOrder,
				alreadyInLocalLayerOrder,
				indexOnOrdering,
				stackConditionAggregate,
				0,
				parallel);

		return localLayerOrders;
	}

	private boolean isLocalLayerOrderDeterminedByGlobal(final OverlapRelation overlapRelation) {
		int parentFaceCount = parentFaces.size();
		for (int i = 0; i < parentFaceCount; i++) {
			for (int j = i + 1; j < parentFaceCount; j++) {
				if (overlapRelation.isUndefined(parentFaces.get(i).getFaceID(),
						parentFaces.get(j).getFaceID())) {
					return false;
				}
			}
		}

		return true;
	}

	private void sort(
			final List<OriFace> candidateFaces,
			final List<List<OriFace>> localLayerOrders,
			final List<OriFace> localLayerOrder,
			final boolean[] alreadyInLocalLayerOrder,
			final Map<OriFace, Integer> indexOnOrdering,
			final StackConditionAggregate stackConditionAggregate,
			final int index,
			final boolean parallel) {

		if (index == parentFaces.size()) {
			var ans = new ArrayList<>(localLayerOrder);
			localLayerOrders.add(ans);
			return;
		}

		var facesToBePut = candidateFaces.stream()
				.filter(f -> !alreadyInLocalLayerOrder[f.getFaceID()])
				.collect(Collectors.toList());
		var facesToBePutStream = facesToBePut.stream();

		// Avoids overhead of insane parallelization.
		// At most 8! = 40320 calls for each process.
		final int PARALLELIZATION_LOWER_BOUND = 8;
		boolean doParallel = parallel && facesToBePut.size() > PARALLELIZATION_LOWER_BOUND;
		if (doParallel) {
			facesToBePutStream = facesToBePutStream.parallel();
		}

		facesToBePutStream.forEach(f -> {
			if (!stackConditionAggregate.satisfiesConditionsOf2Faces(alreadyInLocalLayerOrder, f)) {
				return;
			}

			if (!stackConditionAggregate.satisfiesConditionsOf3Faces(alreadyInLocalLayerOrder, f)) {
				return;
			}

			if (!stackConditionAggregate.satisfiesConditionsOf4Faces(modelFaces, alreadyInLocalLayerOrder,
					indexOnOrdering, f)) {
				return;
			}

			if (doParallel) {
				var nextLocalLayerOrder = new ArrayList<OriFace>(localLayerOrder);
				var nextAlreadyInLocalLayerOrder = alreadyInLocalLayerOrder.clone();
				var nextIndexOnOrdering = new HashMap<OriFace, Integer>(indexOnOrdering);

				nextLocalLayerOrder.set(index, f);
				nextAlreadyInLocalLayerOrder[f.getFaceID()] = true;
				nextIndexOnOrdering.put(f, index);

				sort(facesToBePut,
						localLayerOrders,
						nextLocalLayerOrder,
						nextAlreadyInLocalLayerOrder,
						nextIndexOnOrdering,
						stackConditionAggregate,
						index + 1,
						parallel);
			} else {
				localLayerOrder.set(index, f);
				alreadyInLocalLayerOrder[f.getFaceID()] = true;
				indexOnOrdering.put(f, index);

				sort(facesToBePut,
						localLayerOrders,
						localLayerOrder,
						alreadyInLocalLayerOrder,
						indexOnOrdering,
						stackConditionAggregate,
						index + 1,
						parallel);

				alreadyInLocalLayerOrder[localLayerOrder.get(index).getFaceID()] = false;
				indexOnOrdering.put(localLayerOrder.get(index), -1);
				localLayerOrder.set(index, null);
			}
		});
	}

	/**
	 *
	 * @return geometric center of this subface
	 */
	public Vector2d getInnerPoint() {
		return outline.getCentroid();
	}

	OriFace getOutline() {
		return outline;
	}

	public void clearStackConditions() {
		condition3s.clear();
		condition4s.clear();
	}

	public void addStackConditionOf4Faces(final StackConditionOf4Faces condition) {
		condition4s.add(condition);
	}

	public void addStackConditionOf3Faces(final StackConditionOf3Faces condition) {
		condition3s.add(condition);
	}

	public boolean addParentFaces(final Collection<OriFace> faces) {
		return parentFaces.addAll(faces);
	}

	public Iterable<OriFace> ParentFacesIterable() {
		return parentFaces;
	}

	public OriFace getParentFace(final int index) {
		return parentFaces.get(index);
	}

	public boolean isParentFace(final OriFace face) {
		return parentFaces.contains(face);
	}

	public int getParentFaceCount() {
		return parentFaces.size();
	}

	public boolean isSame(final SubFace sub) {
		if (parentFaces.size() != sub.parentFaces.size()) {
			return false;
		}

		return parentFaces.stream()
				.allMatch(face -> sub.parentFaces.contains(face));
	}

	public int getAllCountOfConditionsOf2Faces(final OverlapRelation overlapRelation) {
		var stackConditionAggregate = new StackConditionAggregate();

		stackConditionAggregate.prepareConditionsOf2Faces(parentFaces, overlapRelation);

		return stackConditionAggregate.getAllCountOfConditionsOf2Faces();
	}
}