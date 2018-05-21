package de.sciss.kdtree;

import java.util.ArrayList;
import java.util.List;

public class NNSolverWorker<T extends Number & Comparable<T>> {
	private final Thread thread;

	private List<KdPoint<T>> resultPoints;

	public NNSolverWorker(final KdTree<T> tree, final List<KdPoint<T>> inputPoints) {
		this.thread = new Thread(() -> {
			resultPoints = new ArrayList<>(inputPoints);

			final NNSolver<T> solver = new NNSolver<>(tree);

			for (final KdPoint<T> point : inputPoints) {
				resultPoints.add(solver.getClosestPoint(point));
			}
		});
	}

	public void start() {
		this.thread.start();
	}

	/**
	 * Waits for this worker thread to finish, then returns the list of result
	 * points to the caller. The index of each result point corresponds to the index
	 * of the input points when constructing this worker.
	 */
	public List<KdPoint<T>> getResultPoints() {
		try {
			this.thread.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		return resultPoints;
	}
}
