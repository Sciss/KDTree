package de.sciss.kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KdFloat2dTree {
    /**
     * Use 1 % of original points to choose a median approximation.
     */
    private static final float APPROXIMATION_POINTS_PERCENTAGE = 0.01f;

    /**
     * Random instance used for the median approximation.
     */
    private static final Random random = new Random(0);

    public final KdFloat2dNode rootNode;

    public KdFloat2dTree(final List<KdFloat2dPoint> points) {
        rootNode = buildNode(null, points, 0);
    }

    /**
     * Returns the axis index for the provided depth, based on the dimension count
     * of this tree.
     *
     * @param depth
     *            the requested tree node depth.
     */
    public int getAxisIndex(final int depth) {
        return depth % 2;
    }

    private KdFloat2dNode buildNode(final KdFloat2dNode parentNode, final List<KdFloat2dPoint> points, final int depth) {
        if (points.isEmpty()) {
            return null;
        }

        final int axisIndex = getAxisIndex(depth);

        final KdFloat2dPoint medianPoint = getFastApproximatedMedianPoint(points, axisIndex);

        // Create node and construct subtrees.

        final KdFloat2dNode newNode = new KdFloat2dNode(medianPoint, depth, axisIndex);

        // Assume both sides have approximately half the number of points.

        final List<KdFloat2dPoint> leftOfMedian = new ArrayList<>(points.size() / 2);
        final List<KdFloat2dPoint> rightOfMedian = new ArrayList<>(points.size() / 2);

        for (final KdFloat2dPoint point : points) {
            if (point == medianPoint) {
                continue;
            }

            if (point.get(axisIndex) > medianPoint.get(axisIndex)) {
                rightOfMedian.add(point);
            } else {
                leftOfMedian.add(point);
            }
        }

        final KdFloat2dNode leftNode  = buildNode(newNode, leftOfMedian , depth + 1);
        final KdFloat2dNode rightNode = buildNode(newNode, rightOfMedian, depth + 1);

        newNode.setLeftNode(leftNode);
        newNode.setRightNode(rightNode);
        newNode.setParentNode(parentNode);

        return newNode;
    }

    /**
     * Uses a small subset of the points to choose a median point approximating the
     * median of all provided points.
     */
    private KdFloat2dPoint getFastApproximatedMedianPoint(final List<KdFloat2dPoint> points, final int axisIndex) {
        final int numberOfElements = (int) Math.max(points.size() * APPROXIMATION_POINTS_PERCENTAGE, 1);

        final List<KdFloat2dPoint> subset = pickRandomSubset(points, numberOfElements);

        sortByAxisIndex(subset, axisIndex);

        return subset.get(subset.size() / 2);
    }

    private void sortByAxisIndex(final List<KdFloat2dPoint> points, final int axisIndex) {
        points.sort((point1, point2) -> {
            final float f1 = point1.get(axisIndex);
            final float f2 = point2.get(axisIndex);
            return Float.compare(f1, f2);
        });
    }

    /**
     * <p>
     * Picks a random subset of points from the given list.
     * </p>
     *
     * <b>Important:</b>
     *
     * <p>
     * This method uses a simple select-one-of-N approach to choose each random
     * point. This may result in points being included multiple times in the
     * returned list.
     * </p>
     */
    private List<KdFloat2dPoint> pickRandomSubset(final List<KdFloat2dPoint> points, final int numberOfElements) {
        final List<KdFloat2dPoint> subset = new ArrayList<>(numberOfElements);

        for (int i = 0; i < numberOfElements; i++) {
            final int randomIndex = random.nextInt(numberOfElements);

            subset.add(points.get(randomIndex));
        }

        return subset;
    }
}
