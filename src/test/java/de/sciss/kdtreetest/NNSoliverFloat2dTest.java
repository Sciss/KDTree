package de.sciss.kdtreetest;

import de.sciss.kdtree.KdFloat2dPoint;
import de.sciss.kdtree.KdFloat2dTree;
import de.sciss.kdtree.NNSolverFloat2d;
import de.sciss.kdtree.generator.RandomKdFloat2dTreeGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NNSolverFloat2dTest {
    private static final int POINT_COUNT = 100_000;

    @Test
    void testNNSearch() {
        final RandomKdFloat2dTreeGenerator treeGenerator = new RandomKdFloat2dTreeGenerator();

        final List<KdFloat2dPoint> points = treeGenerator.generatePoints(POINT_COUNT);
        final KdFloat2dTree tree = new KdFloat2dTree(points);
        final NNSolverFloat2d solver = new NNSolverFloat2d(tree);

        points.forEach(p -> {
            final KdFloat2dPoint res = solver.getClosestPoint(p);
            assertEquals(res, p);
        });

        final List<KdFloat2dPoint> points2 = treeGenerator.generatePoints(POINT_COUNT / 10);

        points2.forEach(p2 -> {
            final KdFloat2dPoint res = solver.getClosestPoint(p2);
            float bestDist = Float.MAX_VALUE;
            KdFloat2dPoint bestPoint = null;
            for (KdFloat2dPoint p1 : points) {
                final float dist = p1.getDistanceSquared(p2);
                if (dist < bestDist) {
                    bestDist    = dist;
                    bestPoint   = p1;
                }
            }
            assertEquals(res, bestPoint);
        });
    }
}
