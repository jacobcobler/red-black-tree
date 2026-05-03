# Implementation Notes

### Jacob Cobler

## Red-Black Tree Properties Used

A Red-Black Tree is a Binary Search Tree with extra coloring rules. This implementation maintains these rules:

1. Every node is red or black.
2. The root is black.
3. Every `NIL` leaf is black.
4. If a node is red, both of its children are black.
5. Every simple path from a node to a descendant `NIL` leaf has the same number of black nodes.

Because these properties keep the height proportional to `log n`, search, insertion, and deletion stay efficient.

## Design Choices

### Generic Key and Value Types

The main class is:

```java
public class RedBlackTree<K extends Comparable<K>, V>
```

Keys must implement `Comparable<K>` so the tree can preserve Binary Search Tree ordering. Values can be any object type.

### NIL Sentinel Node

The implementation uses a shared black `NIL` node instead of Java `null` references for leaves. This makes the insertion and deletion fix-up algorithms closer to the standard Red-Black Tree algorithm because every child pointer always points to a node object.

### Duplicate Keys

If `insert` receives a key that already exists, the value is updated. The tree size does not increase.

## Insertion Algorithm

Insertion has two phases:

1. Insert the new key using normal Binary Search Tree insertion.
2. Color the new node red and call `insertFixup`.

The fix-up handles the standard cases:

- **Case 1:** Parent and uncle are red. Recolor parent and uncle black, recolor grandparent red, and move upward.
- **Case 2:** Parent is red, uncle is black, and the node is an inside child. Rotate to convert to Case 3.
- **Case 3:** Parent is red, uncle is black, and the node is an outside child. Rotate around the grandparent and recolor.

After fix-up, the root is forced to black.

## Deletion Algorithm

Deletion also has two phases:

1. Remove the node using normal Binary Search Tree deletion.
2. If a black node was removed, call `deleteFixup` to restore Red-Black Tree properties.

The delete operation handles:

- Deleting a node with no non-`NIL` children.
- Deleting a node with one non-`NIL` child.
- Deleting a node with two children by replacing it with its in-order successor.

The delete fix-up uses sibling color and sibling child colors to decide whether to recolor, rotate, or move the correction upward.

## Traversal

The implementation includes:

- `inorderKeys()` for sorted key order.
- `preorderKeys()` for root-first traversal.

In-order traversal is useful for proving that the Binary Search Tree ordering is correct.

## Validation Method

The method `validateRedBlackProperties()` checks that:

- The root is black.
- The root parent points to the sentinel `NIL`.
- The tree satisfies Binary Search Tree ordering.
- Red nodes do not have red children.
- Every path has the same black height.
- The counted number of nodes matches the stored `size`.

This method is used heavily in the test suite and benchmark to catch implementation mistakes.

## Time Complexity

Because a Red-Black Tree has height `O(log n)`, the main operations are:

| Operation          | Time Complexity |
| ------------------ | --------------: |
| Search             |      `O(log n)` |
| Insert             |      `O(log n)` |
| Delete             |      `O(log n)` |
| In-order traversal |          `O(n)` |
| Minimum / maximum  |      `O(log n)` |

## Comparison Note

The performance report discusses AVL Trees and regular BSTs conceptually for comparison.
