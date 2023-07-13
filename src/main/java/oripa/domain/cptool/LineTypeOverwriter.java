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
package oripa.domain.cptool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import oripa.domain.cptool.compgeom.AnalyticLine;
import oripa.geom.GeomUtil;
import oripa.value.OriLine;
import oripa.value.OriPoint;

/**
 * @author OUCHI Koji
 *
 */
public class LineTypeOverwriter {
	private final SequentialLineFactory sequentialLineFactory = new SequentialLineFactory();
	private final OverlappingLineExtractor extractor = new OverlappingLineExtractor();

	/**
	 * Overwrites line types of {@code allLines} with the type of lines in
	 * {@code addedLines} if they overlap.
	 *
	 * @param addedLines
	 *            each line of {@code addedLines} should be split at cross
	 *            points.
	 * @param allLines
	 *            is the result of adding lines and splitting at cross points.
	 */
	public void overwriteLineTypes(final Collection<OriLine> addedLines, final Collection<OriLine> allLines,
			final double pointEps) {
		var overlapGroups = extractor.extractOverlapsGroupedBySupport(allLines, pointEps);

		var addedLineSet = new HashSet<>(addedLines);
		Set<OriLine> allLineSet = ConcurrentHashMap.newKeySet();
		allLineSet.addAll(allLines);

		overlapGroups.parallelStream().forEach(overlaps -> {
			determineLineTypes(overlaps, addedLineSet, allLineSet, pointEps);
		});

		allLines.clear();
		allLines.addAll(allLineSet);
	}

	private void determineLineTypes(final Collection<OriLine> overlaps, final Set<OriLine> addedLines,
			final Set<OriLine> allLines, final double pointEps) {

		var addedOverlaps = overlaps.stream()
				.filter(ov -> addedLines.contains(ov))
				.collect(Collectors.toSet());

		var existingOverlaps = overlaps.stream()
				.filter(ov -> !addedOverlaps.contains(ov))
				.collect(Collectors.toSet());

		allLines.removeAll(addedOverlaps);
		allLines.removeAll(existingOverlaps);

		var sortedPoints = sortLineEndPoints(overlaps);
		List<OriLine> splitLines = sequentialLineFactory.createSequentialLines(sortedPoints, OriLine.Type.AUX,
				pointEps);

		// find lines to be a part of crease pattern
		var linesToBeUsed = new ArrayList<OriLine>();
		for (var splitLine : splitLines) {

			Function<Collection<OriLine>, Boolean> find = overlapsForFilter -> {
				var filteredOverlap = overlapsForFilter.stream()
						.filter(line -> GeomUtil.isOverlap(splitLine, line, pointEps))
						.findFirst();

				if (filteredOverlap.isPresent()) {
					linesToBeUsed.add(filteredOverlap.get());

					return true;
				}
				return false;
			};

			if (find.apply(addedOverlaps)) {
				continue;
			}

			find.apply(existingOverlaps);
		}

		allLines.addAll(linesToBeUsed);
	}

	private List<OriPoint> sortLineEndPoints(final Collection<OriLine> overlaps) {
		var points = overlaps.stream()
				.flatMap(line -> line.oriPointStream())
				.collect(Collectors.toList());
		var analyticLine = new AnalyticLine(overlaps.stream().findFirst().get());
		if (analyticLine.isVertical()) {
			points.sort(Comparator.comparing(OriPoint::getY));
		} else {
			points.sort(Comparator.comparing(OriPoint::getX));
		}

		return points;
	}

}
