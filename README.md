# Red-Black Tree Implementation and Performance Analysis

### Jacob Cobler - cobler.12

## Project Overview

This project implements a Red-Black Tree in Java from scratch. The implementation supports insertion, deletion, searching, traversal, height calculation, and validation of Red-Black Tree properties.

The performance report compares Red-Black Trees to AVL Trees and regular Binary Search Trees conceptually.

## Files

```text
RBTreeProjectJava/
├── src/main/RedBlackTree.java          # Main Red-Black Tree implementation
├── src/main/TreeBenchmark.java         # Red-Black Tree performance benchmark
├── test/test/RedBlackTreeTest.java     # Test suite
├── docs/implementation_notes.md        # Design and algorithm documentation
├── performance_report.md               # Performance analysis report
├── benchmark_results.csv               # Sample benchmark output
└── README.md                           # Setup and run instructions
```

## Run the Test Suite

Expected output:

```text
All RedBlackTree tests passed.
```

The test suite includes unit tests for individual methods and integration-style tests that insert, search, traverse, delete, and validate the full tree.

## Run the Performance Benchmark

This creates a file named:

```text
benchmark_results.csv
```

The benchmark measures Red-Black Tree insertion, searching, deletion, and height for both ascending and random input distributions.

## Main Red-Black Tree Operations

The main class is `RedBlackTree<K extends Comparable<K>, V>`.

Important public methods:

```java
void insert(K key, V value)
boolean delete(K key)
V search(K key)
boolean containsKey(K key)
List<K> inorderKeys()
List<K> preorderKeys()
K minimumKey()
K maximumKey()
int height()
boolean validateRedBlackProperties()
int size()
boolean isEmpty()
void clear()
```

## Notes

- Duplicate keys update the existing value instead of creating another node.
- `null` keys are not allowed.
- The tree uses a shared black `NIL` sentinel node instead of Java `null` children.
- The validation method checks ordering, root color, red-parent/red-child violations, black-height consistency, and stored size.
