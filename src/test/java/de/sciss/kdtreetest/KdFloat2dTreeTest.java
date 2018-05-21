package de.sciss.kdtreetest;

import de.sciss.kdtree.KdFloat2dNode;
import de.sciss.kdtree.KdFloat2dTree;
import de.sciss.kdtree.generator.RandomKdFloat2dTreeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KdFloat2dTreeTest {
    private static final int POINT_COUNT = 100_000;

    @Test
    void testKdTree() {
        final RandomKdFloat2dTreeGenerator treeGenerator = new RandomKdFloat2dTreeGenerator();

        final KdFloat2dTree tree = treeGenerator.generate(POINT_COUNT);

        // To test the tree, we have to traverse each node, checking whether both
        // sub nodes have values smaller/larger than the current node for the current
        // dimension.

        assert (tree.rootNode != null);
        checkNode(tree, tree.rootNode);
    }

    private void checkNode(final KdFloat2dTree tree, final KdFloat2dNode node) {
        final int axisIndex = node.axisIndex;

        // Assert that the value of the left node on the relevant axis index is always
        // smaller than or equal to the parent we're checking.

        final KdFloat2dNode leftNode = node.getLeftNode();

        if (leftNode != null) {

            assertEquals(leftNode.depth, node.depth + 1);

            assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
            assertEquals(leftNode.axisIndex, tree.getAxisIndex(leftNode.depth));

            assertTrue(leftNode.point.get(axisIndex) <= node.point.get(axisIndex));

            assertSame(leftNode.getParentNode(), node);

            checkNode(tree, leftNode);
        }

        // Assert that the value of the left node on the relevant axis index is always
        // larger than the parent we're checking.

        final KdFloat2dNode rightNode = node.getRightNode();

        if (rightNode != null) {
            assertEquals(rightNode.depth, node.depth + 1);

            assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
            assertEquals(rightNode.axisIndex, tree.getAxisIndex(rightNode.depth));

            assertTrue(rightNode.point.get(axisIndex) > node.point.get(axisIndex));

            assertSame(rightNode.getParentNode(), node);

            checkNode(tree, rightNode);
        }
    }
}
