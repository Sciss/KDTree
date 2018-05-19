package de.jilocasin.nearestneighbour;

import de.jilocasin.kdtree.model.KdNode;
import de.jilocasin.kdtree.model.KdPoint;
import de.jilocasin.kdtree.model.KdTree;

/**
 * Solver class to find nearest neighbour elements in a given k-d tree. The tree
 * consists of nodes with points of the given generic type.
 */
public class NNSolver<T extends Number & Comparable<T>> {
	private final KdTree<T> tree;

	private KdPoint<T> searchTargetPoint;

	private KdPoint<T> currentBestPoint;
	private double currentBestDistanceSquared;

	public NNSolver(final KdTree<T> tree) {
		this.tree = tree;
	}

	public KdPoint<T> getClosestPoint(final KdPoint<T> searchTargetPoint) {
		this.searchTargetPoint = searchTargetPoint;
		this.currentBestPoint = null;

		solveForNode(tree.rootNode);

		return currentBestPoint;
	}

	private void solveForNode(final KdNode<T> node) {
		// Move down the tree recursively to get the starting leaf node and use it as an
		// initial "best point" if none was set yet.

		final KdNode<T> leaf = findLeaf(node);

		updateCurrentBestIfNeeded(leaf.point);

		// Then unwind from this leaf, possibly calling this node solver recursively for
		// different branches.

		unwindFrom(leaf, node);
	}

	private KdNode<T> findLeaf(final KdNode<T> node) {
		switch (node.numberOfChildren()) {
		case 0:
			// If we reached a leaf node, return it.

			return node;

		case 1:
			// Use the single child node to continue.

			if (node.hasLeftNode()) {
				return findLeaf(node.getLeftNode());
			} else {
				return findLeaf(node.getRightNode());
			}

		case 2:
			// Decide which sub node to follow.

			final T searchValue = searchTargetPoint.values.get(node.axisIndex);
			final T nodeValue = node.point.values.get(node.axisIndex);

			// If the axis value of the point we're searching for is greater than the axis
			// value of node we're looking at, continue with the right sub node.

			if (searchValue.compareTo(nodeValue) > 0) {
				return findLeaf(node.getRightNode());
			} else {
				return findLeaf(node.getLeftNode());
			}
		}

		return null;
	}

	/**
	 * Updates the current best, if no current best is set or the provided point is
	 * better than the current best.
	 */
	private void updateCurrentBestIfNeeded(final KdPoint<T> point) {
		// Don't use the actual search point as the best point.

		if (point == searchTargetPoint) {
			return;
		}

		if (currentBestPoint == null) {
			currentBestPoint = point;
			currentBestDistanceSquared = point.getDistanceSquared(searchTargetPoint);
		} else {
			// Cache the leaf distance, to only do the distance calculation once.

			final double leafDistanceSquared = point.getDistanceSquared(searchTargetPoint);

			if (currentBestPoint == null || leafDistanceSquared < currentBestDistanceSquared) {
				currentBestPoint = point;
				currentBestDistanceSquared = leafDistanceSquared;
			}
		}
	}

	private void unwindFrom(final KdNode<T> leafNode, final KdNode<T> topNode) {
		// Iteratively move up the node tree until we reach the top node.

		KdNode<T> workingNode = leafNode;

		while (workingNode.getParentNode() != topNode.getParentNode()) {
			final KdNode<T> parentNode = workingNode.getParentNode();

			updateCurrentBestIfNeeded(parentNode.point);

			// Check whether there could be any points on the other side of this parent
			// node.

			// To do this we check the intersection of the hypersphere with the hyperplane.
			// Simply put, because the final axis is aligned, we can final just compare some
			// values.

			final double parentPointValue = parentNode.point.values.get(parentNode.axisIndex).doubleValue();
			final double searchPointValue = searchTargetPoint.values.get(parentNode.axisIndex).doubleValue();

			final double axisDistance = parentPointValue - searchPointValue;

			// Because we need to compare apples to apples, we need to calculate the squared
			// distance.

			final double axisDistanceSquared = axisDistance * axisDistance;

			if (axisDistanceSquared < currentBestDistanceSquared && parentNode.numberOfChildren() == 2) {
				// We want to traverse the other path, so we need to check which side we started
				// unwinding from.

				if (parentNode.getLeftNode() == workingNode) {
					// Start the full solver procedure for the right sub node.

					solveForNode(parentNode.getRightNode());
				} else {
					// Start the full solver procedure for the left sub node.

					solveForNode(parentNode.getLeftNode());
				}
			}

			workingNode = parentNode;
		}
	}
}
