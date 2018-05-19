package de.jilocasin.kdtree.model;

import java.util.List;

/**
 * Defines a single point in a k dimensional space.
 */
public class KdPoint<T extends Number & Comparable<T>> {
	public List<T> values;

	public KdPoint(final List<T> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return values.toString();
	}

	public double getDistanceSquared(final KdPoint<T> other) {
		// Calculate the squared distance to the other point via euclidean distance.

		final int dimensions = values.size();
		double distance = 0;

		for (int i = 0; i < dimensions; i++) {
			final double delta = values.get(i).doubleValue() - other.values.get(i).doubleValue();

			distance += (delta * delta);
		}

		return distance;

	}
}
