package de.sciss.kdtree;

/**
 * Solver class to find nearest neighbour elements in a given k-d tree. The tree
 * consists of nodes with points of the given generic type.
 */
public class NNSolverFloat2d {
    private final KdFloat2dTree tree;

    private float targetX, targetY;

    private KdFloat2dPoint currentBestPoint;
    private float currentBestDistanceSquared;

    public NNSolverFloat2d(final KdFloat2dTree tree) {
        this.tree = tree;
    }

    public KdFloat2dPoint getClosestPoint(final KdFloat2dPoint p) {
        return getClosestPoint(p.x, p.y);
    }

    public KdFloat2dPoint getClosestPoint(final float x, final float y) {
        targetX = x;
        targetY = y;
        this.currentBestPoint = null;

        solveForNode(tree.rootNode);

        return currentBestPoint;
    }

    private void solveForNode(final KdFloat2dNode node) {
        // Move down the tree recursively to get the starting leaf node and use it as an
        // initial "best point" if none was set yet.

        final KdFloat2dNode leaf = findLeaf(node);

        updateCurrentBestIfNeeded(leaf.point);

        // Then unwind from this leaf, possibly calling this node solver recursively for
        // different branches.

        unwindFrom(leaf, node);
    }

    private KdFloat2dNode findLeaf(final KdFloat2dNode node) {
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

                final int idx = node.axisIndex;
                final float searchValue = idx == 0 ? targetX : targetY;
                final float nodeValue   = node.point.get(idx);

                // If the axis value of the point we're searching for is greater than the axis
                // value of node we're looking at, continue with the right sub node.

                if (searchValue > nodeValue) {
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
    private void updateCurrentBestIfNeeded(final KdFloat2dPoint point) {

// This seems wrong:
//        // Don't use the actual search point as the best point.
//
//        if (point == searchTargetPoint) {
//            return;
//        }

        final float distSq = point.getDistanceSquared(targetX, targetY);

        if (currentBestPoint == null) {
            currentBestPoint = point;
            currentBestDistanceSquared = distSq;
        } else {
            // Cache the leaf distance, to only do the distance calculation once.
            if (distSq < currentBestDistanceSquared) {
                currentBestPoint = point;
                currentBestDistanceSquared = distSq;
            }
        }
    }

    private void unwindFrom(final KdFloat2dNode leafNode, final KdFloat2dNode topNode) {
        // Iteratively move up the node tree until we reach the top node.

        KdFloat2dNode workingNode = leafNode;

        while (workingNode.getParentNode() != topNode.getParentNode()) {
            final KdFloat2dNode parentNode = workingNode.getParentNode();

            updateCurrentBestIfNeeded(parentNode.point);

            // Check whether there could be any points on the other side of this parent
            // node.

            // To do this we check the intersection of the hypersphere with the hyperplane.
            // Simply put, because the final axis is aligned, we can final just compare some
            // values.

            final int idx = parentNode.axisIndex;
            final float parentPointValue = parentNode.point .get(idx);
            final float searchPointValue = idx == 0 ? targetX : targetY;

            final float axisDistance = parentPointValue - searchPointValue;

            // Because we need to compare apples to apples, we need to calculate the squared
            // distance.

            final float axisDistanceSquared = axisDistance * axisDistance;

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
