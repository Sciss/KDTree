# KDTree

[![Build Status](https://travis-ci.org/Sciss/KDTree.svg?branch=master)](https://travis-ci.org/Sciss/KDTree)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.sciss/kdtree/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.sciss/kdtree)

## statement

A static spatial index data structure, implemented in Java.

Forked from [here](https://github.com/Jilocasin/nearest-neighbour). The original author is Jilocasin (Daniel Obermeier).
License of fork is original license: Apache 2.0.

What I did is use [sbt]() for building,
move classes into my own package `de.sciss.kdtree` to avoid confusion, and add some optimised special cases
such as 2D float tree. I publish this artifact to Maven Central:

    "de.sciss" % "kdtree" % v
    
The current version `v` is `"0.1.1"`.

The library requires a JVM 1.8 or newer.

To-do:

- the junit tests somehow don't run through `sbt test`. You can execute them from IntelliJ, for example.

Below is the original 'read-me':

------------------

Finding the [nearest neighbour](https://en.wikipedia.org/wiki/Nearest_neighbor_search) of a k-dimensional set of 
points using an efficient [k-d tree](https://en.wikipedia.org/wiki/K-d_tree) data structure.

## Installation

Just download the repository and include the sources in your project.

## Usage

To search for any nearest neighbours points, you first need to set up a `KdTree` based on a list of `KdPoints`. Both 
classes use generics to allow any point data payload. Use `Integer`, `Double` or any other class extending 
`java.lang.Number` and implementing the `Comparable` interface.

Start by setting up a list of k-dimensional points:
```java
List<KdPoint<Integer>> points = new ArrayList<>();

points.add(new KdPoint<>(5, 8));
points.add(new KdPoint<>(5, 5));
points.add(new KdPoint<>(9, 1));
```

Now set up a `KdTree` based on those points. Pay attention to the correct dimension value, matching the amount of
individual axis values used when creating the points. For performance reason the number of axis values per point
is not checked during tree creation.

```java
int dimensions = 2;
KdTree<Integer> tree = new KdTree<>(dimensions, points);
```

To calculate the nearest neighbour of an arbitrary point, use a generic ```NNSolver``` for the used axis type.
```java
NNSolver<Integer> solver = new NNSolver<>(tree);
		
KdPoint<Integer> searchPoint = new KdPoint<>(5, 10);
KdPoint<Integer> nearestOtherPoint = solver.findNearestPoint(searchPoint);

// Returns the point at (5, 8)
```

This library allows nearest neighbour search for arbitrary point instances not used during tree setup, as well as
existing tree points. When providing a point instance included in the tree data, the closest point except the search
point itself will be returned.

```java
KdPoint<Integer> searchPoint = points.get(0);
KdPoint<Integer> nearestOtherPoint = solver.findNearestPoint(searchPoint);

// Returns the nearest point at (5, 5) to this instance,
// instead of the point instance at (5, 8) we used to search.
// When provided a new point instance at (5, 8) instead,
// the closest point would be the original point at (5, 8).
```

You should always use a `NNSolverOrchestrator` to get the best performance. It will distribute the workload to a
given number of threads. 

Using an orchestrator is just as easy:

```java
int workerThreadsCount = Runtime.getRuntime().availableProcessors();

List<KdPoint<Integer>> searchPoints = new ArrayList<>();
searchPoints.add(new KdPoint<>(1, 1));
searchPoints.add(new KdPoint<>(7, 7));
    
NNSolverOrchestrator<Integer> solverOrchestrator = new NNSolverOrchestrator<>(tree, workerThreadsCount);

List<KdPoint<Integer>> nearestPoints = solverOrchestrator.findNearestPoints(searchPoints);
```

Note that the orchestrator simply returns a new list of points. The index of each result point corresponds to the
index of the provided search points. So, the nearest point for the first search point will be returned at
`nearestPoints.get(0)` etc.
