package de.jilocasin.kdtree;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.jilocasin.kdtree.generator.RandomDoubleKdTreeGenerator;
import de.jilocasin.kdtree.model.KdTree;
import de.jilocasin.kdtree.model.KdTreeNode;

class KdTreeTest {
	private static final int POINT_COUNT = 100_000;

	@Test
	void testKdTree() {
		for (int dimensionCount = 1; dimensionCount <= 10; dimensionCount++) {
			testKdTreeWithDimensionCount(dimensionCount);
		}
	}

	void testKdTreeWithDimensionCount(final int dimensionCount) {
		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final KdTree<Double> tree = treeGenerator.generate(dimensionCount, POINT_COUNT);

		// To test the tree, we have to traverse each node, checking whether both
		// sub nodes have values smaller/larger than the current node for the current
		// dimension.

		checkNode(tree, tree.rootNode);
	}

	void checkNode(final KdTree<Double> tree, final KdTreeNode<Double> node) {
		final int axisIndex = node.axisIndex;

		// Assert that the value of the left node on the relevant axis index is always
		// smaller than or equal to the parent we're checking.

		final KdTreeNode<Double> leftNode = node.getLeftNode();

		if (leftNode != null) {

			assertEquals(leftNode.depth, node.depth + 1);

			assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
			assertEquals(leftNode.axisIndex, tree.getAxisIndex(leftNode.depth));

			assertTrue(leftNode.point.values.get(axisIndex) <= node.point.values.get(axisIndex));

			checkNode(tree, leftNode);
		}

		// Assert that the value of the left node on the relevant axis index is always
		// larger than the parent we're checking.

		final KdTreeNode<Double> rightNode = node.getRightNode();

		if (rightNode != null) {
			assertEquals(rightNode.depth, node.depth + 1);

			assertEquals(node.axisIndex, tree.getAxisIndex(node.depth));
			assertEquals(rightNode.axisIndex, tree.getAxisIndex(rightNode.depth));

			assertTrue(rightNode.point.values.get(axisIndex) > node.point.values.get(axisIndex));

			checkNode(tree, rightNode);
		}
	}
}
