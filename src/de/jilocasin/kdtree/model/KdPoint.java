package de.jilocasin.kdtree.model;

import java.util.List;

/**
 * Defines a single point in a k dimensional space.
 */
public class KdPoint<T extends Comparable<T>> {
	public List<T> values;

	public KdPoint(final List<T> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return values.toString();
	}
}
