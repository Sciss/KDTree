package de.sciss.kdtree;

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

	@Override
	public boolean equals(Object any) {
		if (any instanceof KdPoint) {
			final KdPoint that = (KdPoint<?>) any;
			final int size = this.values.size();
			if (size == that.values.size()) {
				for (int i = 0; i < size; i++) {
					final T 	    ptA = this.values.get(i);
					final Object    ptB = that.values.get(i);
					if (!ptA.equals(ptB)) return false;
				}
				return true;

			} else {
				return false;
			}

		} else {
			return super.equals(any);
		}
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
