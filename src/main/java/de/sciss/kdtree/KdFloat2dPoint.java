package de.sciss.kdtree;

/**
 * Defines a single floating point point in a 2-dimensional space.
 */
public final class KdFloat2dPoint {
	public final float x;
    public final float y;

	public float get(int axis) {
	    switch(axis) {
            case 0:
                return x;
            case 1:
                return y;
            default:
                throw new IndexOutOfBoundsException(String.valueOf(axis));
        }
    }

	public KdFloat2dPoint(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	public float getDistanceSquared(final KdFloat2dPoint that) {
        final float dx = this.x - that.x;
        final float dy = this.y - that.y;
        return dx*dx + dy*dy;
	}

    public float getDistanceSquared(final float x, final float y) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        return dx*dx + dy*dy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KdFloat2dPoint) {
            final KdFloat2dPoint that = (KdFloat2dPoint) obj;
            return this.x == that.x && this.y == that.y;
        } else {
            return super.equals(obj);
        }
    }
}
