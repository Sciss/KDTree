package de.jilocasin.nearestneighbour;

import de.jilocasin.kdtree.generator.RandomDoubleKdTreeGenerator;
import de.jilocasin.kdtree.model.KdTree;
import de.jilocasin.kdtree.model.KdNode;

public class NearestNeighbourProblem {
	private static final int DIMENSION_COUNT = 2;

	private static final int POINT_COUNT = 100_000;

	public static void main(final String[] args) {
		final RandomDoubleKdTreeGenerator treeGenerator = new RandomDoubleKdTreeGenerator();

		final KdTree<Double> tree = treeGenerator.generate(DIMENSION_COUNT, POINT_COUNT);

		printNode(tree.rootNode);
	}

	private static void printNode(final KdNode<Double> node) {
		if (node == null) {
			return;
		}

		System.out.print("Depth " + node.depth + ": ");
		System.out.print(node.point);
		System.out.print(" has left node: " + getNodePointDescription(node.getLeftNode()));
		System.out.println(", has right node " + getNodePointDescription(node.getRightNode()));

		printNode(node.getLeftNode());
		printNode(node.getRightNode());
	}

	private static String getNodePointDescription(final KdNode<Double> node) {
		if (node != null) {
			return node.point.toString();
		} else {
			return "[None]";
		}
	}
}
