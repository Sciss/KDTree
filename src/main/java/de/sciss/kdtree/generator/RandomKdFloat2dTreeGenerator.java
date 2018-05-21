package de.sciss.kdtree.generator;

import de.sciss.kdtree.KdFloat2dPoint;
import de.sciss.kdtree.KdFloat2dTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomKdFloat2dTreeGenerator {
    /**
     * The minimum for point values on any axis.
     */
    private static final float AXIS_POSITION_MIN = 100_000;

    /**
     * The maximum for random point values on any axis.
     */
    private static final float AXIS_POSITION_MAX = 1_000_000;

    private final Random random = new Random(0);

    private float buildRandomValue() {
        return stretch(random.nextFloat());
    }

    private float stretch(final float uniformDouble) {
        return RandomKdFloat2dTreeGenerator.AXIS_POSITION_MIN + (uniformDouble *
                (RandomKdFloat2dTreeGenerator.AXIS_POSITION_MAX - RandomKdFloat2dTreeGenerator.AXIS_POSITION_MIN));
    }

    public KdFloat2dTree generate(final int pointCountt) {
        return new KdFloat2dTree(generatePoints(pointCountt));
    }

    public List<KdFloat2dPoint> generatePoints(final int pointCount) {
        final List<KdFloat2dPoint> points = new ArrayList<>(pointCount);

        for (int i = 0; i < pointCount; i++) {
            final float x = buildRandomValue();
            final float y = buildRandomValue();
            points.add(new KdFloat2dPoint(x, y));
        }

        return points;
    }
}
