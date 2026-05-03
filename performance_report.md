# Performance Analysis Report

### Jacob Cobler

## Goal

The goal of the benchmark is to measure how the Java Red-Black Tree implementation performs during insertion, search, and deletion. The benchmark also records the tree height after insertion to show that the tree remains balanced under different input distributions.

AVL Tree and regular Binary Search Tree structures are discussed as comparisons in the analysis section.

## Expected Time Complexity

| Data Structure |                           Insert |                           Search |                           Delete | Notes                                        |
| -------------- | -------------------------------: | -------------------------------: | -------------------------------: | -------------------------------------------- |
| Red-Black Tree |                       `O(log n)` |                       `O(log n)` |                       `O(log n)` | Balanced using colors and rotations          |
| AVL Tree       |                       `O(log n)` |                       `O(log n)` |                       `O(log n)` | More strictly balanced than a Red-Black Tree |
| Regular BST    | Average `O(log n)`, worst `O(n)` | Average `O(log n)`, worst `O(n)` | Average `O(log n)`, worst `O(n)` | Can become a linked list with sorted input   |

## Benchmark Setup

The benchmark used these input distributions:

- **Ascending input:** `0, 1, 2, 3, ...`
- **Random input:** shuffled integers

Each distribution was tested with input sizes:

- 1,000
- 2,500
- 5,000
- 10,000

The benchmark uses multiple warm-up rounds and reports the median of measured rounds to reduce the effect of one-time JVM startup behavior.

## Sample Results

These results were generated in the included `benchmark_results.csv` file. Exact numbers may change on a different computer.

| Structure      | Distribution |      n | Insert ms | Search ms | Delete ms | Height |
| -------------- | ------------ | -----: | --------: | --------: | --------: | -----: |
| Red-Black Tree | ascending    |  1,000 |     0.182 |     0.120 |     0.146 |     16 |
| Red-Black Tree | ascending    |  2,500 |     0.527 |     0.311 |     0.371 |     19 |
| Red-Black Tree | ascending    |  5,000 |     1.136 |     0.559 |     0.678 |     21 |
| Red-Black Tree | ascending    | 10,000 |     0.735 |     1.191 |     1.505 |     23 |
| Red-Black Tree | random       |  1,000 |     0.546 |     0.085 |     0.111 |     11 |
| Red-Black Tree | random       |  2,500 |     1.560 |     0.218 |     0.291 |     13 |
| Red-Black Tree | random       |  5,000 |     0.638 |     0.638 |     0.588 |     14 |
| Red-Black Tree | random       | 10,000 |     1.441 |     1.322 |     1.527 |     15 |

## Analysis

The Red-Black Tree performed consistently for both ascending and random input. The height stayed small even when values were inserted in sorted order. For example, with 10,000 ascending keys, the Red-Black Tree height was 23.

A regular unbalanced Binary Search Tree can perform well on many random inputs, but sorted input can cause it to become a linked list. In that worst case, its height becomes `n - 1`, and insertion, search, and deletion can degrade to `O(n)`.

An AVL Tree also guarantees logarithmic height and is usually more strictly balanced than a Red-Black Tree. Because of that stricter balance, AVL search can be slightly faster in some cases, but AVL insertions and deletions may require more rebalancing work. Red-Black Trees are commonly used when a good balance between search speed and update speed is needed.

The important result is that the Red-Black Tree avoids the sorted-input problem that affects a regular BST. Even when values are inserted in ascending order, the tree uses recoloring and rotations to keep height logarithmic.

## Conclusion

The Red-Black Tree is a strong general-purpose balanced search tree. It keeps insertion, deletion, and searching efficient without requiring the project to implement additional tree structures. The benchmark supports this by showing that height stays small across different input sizes and input distributions.
