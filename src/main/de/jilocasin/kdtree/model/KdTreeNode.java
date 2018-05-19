package de.jilocasin.kdtree.model;

public class KdTreeNode<T extends Comparable<T>> {
	public final KdPoint<T> point;

	public final int depth;
	public final int axisIndex;

	private KdTreeNode<T> leftNode;
	private KdTreeNode<T> rightNode;

	public KdTreeNode(final KdPoint<T> point, final int depth, final int axisIndex) {
		this.point = point;
		this.depth = depth;
		this.axisIndex = axisIndex;
	}

	public KdTreeNode<T> getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(final KdTreeNode<T> leftNode) {
		this.leftNode = leftNode;
	}

	public KdTreeNode<T> getRightNode() {
		return rightNode;
	}

	public void setRightNode(final KdTreeNode<T> rightNode) {
		this.rightNode = rightNode;
	}

}
