# nearest-neighbour
Finding the [nearest neighbour](https://en.wikipedia.org/wiki/Nearest_neighbor_search) of a k-dimensional set of points using an efficient [k-d tree](https://en.wikipedia.org/wiki/K-d_tree) data structure.

## Installation
Just download the repository and include the sources in your project.

## Usage
First you need to create a `KdTree` based on a list of `KdPoints`. Both are generic classes to be suitable for any data payload that extends from `java.lang.Number` and implements the `Comparable` interface. Use `Integer`, `Double` or any other payload class for the axis data.

Set up a list of k-dimensional points like this:
```java
List<KdPoint<Integer>> points = new ArrayList<>();

points.add(new KdPoint<>(5, 8));
points.add(new KdPoint<>(5, 5));
points.add(new KdPoint<>(9, 1));
```

Then set up a `KdTree` based on those points. Pay attention to the correct dimension count, matching the amount of individual axis values used when creating the points. For performance reasons the number of axis values is not checked for each point during tree setup.

```java
int dimensions = 2;
KdTree<Integer> tree = new KdTree<>(dimensions, points);
```

To calculate the nearest neighbour of any of the points, use a ```NNSolver``` for the used payload class.
```java
NNSolver<Integer> solver = new NNSolver<>(tree);
		
KdPoint<Integer> searchPoint = new KdPoint<>(5, 10);
KdPoint<Integer> nearestOtherPoint = solver.findNearestPoint(searchPoint);

// Returns the point at (5, 8)
```

Note that this library  allows nearest neighbour search for arbitrary points, which were not present and used during tree setup, as well as searching for the newest neighbour of an existing tree point. When providing a point instance, that is already present in the tree structure, the closest point except the search point itself will be returned.

```java
KdPoint<Integer> searchPoint = points.get(0);
KdPoint<Integer> nearestOtherPoint = solver.findNearestPoint(searchPoint);

// Returns the nearest point at (5, 5) to this instance,
// instead of the point instance at (5, 8) we used to search.
// When provided a new point instance at (5, 8) instead,
// the closest point would be the original point at (5, 8).
```

To improve performance, you should always use a `NNSolverOrchestrator`, which distributes the workload to a set number of threads. Using the orchestrator is pretty straightforward:

```java
int workerThreadsCount = Runtime.getRuntime().availableProcessors();

final List<KdPoint<Integer>> searchPoints = new ArrayList<>();
searchPoints.add(new KdPoint<>(1, 1));
searchPoints.add(new KdPoint<>(7, 7));
    
NNSolverOrchestrator<Integer> solverOrchestrator = new NNSolverOrchestrator<>(tree, workerThreadsCount);

List<KdPoint<Integer>> nearestPoints = solverOrchestrator.findNearestPoints(searchPoints);
```

Note that the orchestrator simply returns a new list of points. The index of each result point corresponds to the index of the provided search points. So, the nearest point for the first search point will be returned at `nearestPoints.get(0)` etc.
