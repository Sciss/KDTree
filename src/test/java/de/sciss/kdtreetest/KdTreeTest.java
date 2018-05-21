package de.sciss.kdtreetest;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.sciss.kdtree.KdNode;
import de.sciss.kdtree.KdTree;
import org.junit.jupiter.api.Test;

import de.sciss.kdtree.generator.RandomDoubleKdTreeGenerator;

class KdTreeTest {
	private static final int POINT_COUNT = 100_000;

	@Test
	void testKdTree() {
		for (int dimensionCount = 1; dimensionCount <= 10; dimensionCount++) {
			testKdTreeWithDimensionCount(dimensionCount);
		}
	}

	private void testKdTreeWithDimensionCount(final int dimensionCount) {
		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final KdTree<Double> tree = treeGenerator.generate(dimensionCount, POINT_COUNT);

		// To test the tree, we have to traverse each node, checking whether both
		// sub nodes have values smaller/larger than the current node for the current
		// dimension.

		assert (tree.rootNode != null);
        checkNode(tree, tree.rootNode);
	}

	private void checkNode(final KdTree<Double> tree, final KdNode<Double> node) {
		final int axisIndex = node.axisIndex;

		// Assert that the value of the left node on the relevant axis index is always
		// smaller than or equal to the parent we're checking.

		final KdNode<Double> leftNode = node.getLeftNode();

		if (leftNode != null) {

			assertEquals(leftNode.depth, node.depth + 1);

			assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
			assertEquals(leftNode.axisIndex, tree.getAxisIndex(leftNode.depth));

			assertTrue(leftNode.point.values.get(axisIndex) <= node.point.values.get(axisIndex));

			assertSame(leftNode.getParentNode(), node);

			checkNode(tree, leftNode);
		}

		// Assert that the value of the left node on the relevant axis index is always
		// larger than the parent we're checking.

		final KdNode<Double> rightNode = node.getRightNode();

		if (rightNode != null) {
			assertEquals(rightNode.depth, node.depth + 1);

			assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
			assertEquals(rightNode.axisIndex, tree.getAxisIndex(rightNode.depth));

			assertTrue(rightNode.point.values.get(axisIndex) > node.point.values.get(axisIndex));

			assertSame(rightNode.getParentNode(), node);

			checkNode(tree, rightNode);
		}
	}
}
