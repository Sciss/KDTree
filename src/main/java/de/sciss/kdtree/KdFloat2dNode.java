package de.sciss.kdtree;

public final class KdFloat2dNode {
    public final KdFloat2dPoint point;

    public final int depth;
    public final int axisIndex;

    private KdFloat2dNode parentNode;
    private KdFloat2dNode leftNode;
    private KdFloat2dNode rightNode;

    public KdFloat2dNode(final KdFloat2dPoint point, final int depth, final int axisIndex) {
        this.point = point;
        this.depth = depth;
        this.axisIndex = axisIndex;
    }

    public KdFloat2dNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(final KdFloat2dNode parentNode) {
        this.parentNode = parentNode;
    }

    public KdFloat2dNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(final KdFloat2dNode leftNode) {
        this.leftNode = leftNode;
    }

    public KdFloat2dNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(final KdFloat2dNode rightNode) {
        this.rightNode = rightNode;
    }

    public boolean hasParentNode() {
        return parentNode != null;
    }

    public boolean hasLeftNode() {
        return leftNode != null;
    }

    public boolean hasRightNode() {
        return rightNode != null;
    }

    public boolean hasChildren() {
        return leftNode != null && rightNode != null;
    }

    public int numberOfChildren() {
        return (leftNode != null ? 1 : 0) + (rightNode != null ? 1 : 0);
    }
}
