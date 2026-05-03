package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A generic Red-Black Tree map implementation.
 *
 * @param <K> key type; keys must be comparable
 * @param <V> value type
 * @author Jacob Cobler
 */
public class RedBlackTree<K extends Comparable<K>, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static final class Node {
        Object key;
        Object value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(Object key, Object value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private final Node nil;
    private Node root;
    private int size;

    public RedBlackTree() {
        nil = new Node(null, null, BLACK);
        nil.left = nil;
        nil.right = nil;
        nil.parent = nil;
        root = nil;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = nil;
        nil.parent = nil;
        nil.color = BLACK;
        size = 0;
    }

    public boolean containsKey(K key) {
        Objects.requireNonNull(key, "key cannot be null");
        return searchNode(key) != nil;
    }

    @SuppressWarnings("unchecked")
    public V search(K key) {
        Objects.requireNonNull(key, "key cannot be null");
        Node node = searchNode(key);
        return node == nil ? null : (V) node.value;
    }

    public void insert(K key, V value) {
        Objects.requireNonNull(key, "key cannot be null");

        Node parent = nil;
        Node current = root;

        while (current != nil) {
            parent = current;
            int cmp = compare(key, current.key);
            if (cmp == 0) {
                current.value = value;
                return;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node node = new Node(key, value, RED);
        node.left = nil;
        node.right = nil;
        node.parent = parent;

        if (parent == nil) {
            root = node;
        } else if (compare(key, parent.key) < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        size++;
        insertFixup(node);
    }

    public boolean delete(K key) {
        Objects.requireNonNull(key, "key cannot be null");
        Node z = searchNode(key);
        if (z == nil) {
            return false;
        }

        Node y = z;
        boolean yOriginalColor = y.color;
        Node x;

        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimumNode(z.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        size--;
        if (yOriginalColor == BLACK) {
            deleteFixup(x);
        }
        if (root != nil) {
            root.parent = nil;
        }
        nil.color = BLACK;
        return true;
    }

    public List<K> inorderKeys() {
        List<K> result = new ArrayList<K>();
        inorder(root, result);
        return result;
    }

    public List<K> preorderKeys() {
        List<K> result = new ArrayList<K>();
        preorder(root, result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public K minimumKey() {
        return root == nil ? null : (K) minimumNode(root).key;
    }

    @SuppressWarnings("unchecked")
    public K maximumKey() {
        if (root == nil) {
            return null;
        }
        Node current = root;
        while (current.right != nil) {
            current = current.right;
        }
        return (K) current.key;
    }

    /**
     * Height measured in edges. Empty tree height is -1; single-node tree height is
     * 0.
     */
    public int height() {
        return height(root);
    }

    public boolean validateRedBlackProperties() {
        if (root == nil) {
            return size == 0 && nil.color == BLACK;
        }
        if (root.color != BLACK || root.parent != nil) {
            return false;
        }
        if (!isBST(root, null, null)) {
            return false;
        }
        if (countNodes(root) != size) {
            return false;
        }
        return blackHeight(root) != -1;
    }

    private Node searchNode(K key) {
        Node current = root;
        while (current != nil) {
            int cmp = compare(key, current.key);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return nil;
    }

    private void insertFixup(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
        root.parent = nil;
        nil.color = BLACK;
    }

    private void deleteFixup(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;

                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == BLACK && w.right.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }

                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;

                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }

                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
        nil.color = BLACK;
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != nil) {
            y.right.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }

    private void transplant(Node oldSubtree, Node newSubtree) {
        if (oldSubtree.parent == nil) {
            root = newSubtree;
        } else if (oldSubtree == oldSubtree.parent.left) {
            oldSubtree.parent.left = newSubtree;
        } else {
            oldSubtree.parent.right = newSubtree;
        }
        newSubtree.parent = oldSubtree.parent;
    }

    private Node minimumNode(Node node) {
        Node current = node;
        while (current.left != nil) {
            current = current.left;
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    private int compare(K key, Object nodeKey) {
        return key.compareTo((K) nodeKey);
    }

    @SuppressWarnings("unchecked")
    private void inorder(Node node, List<K> result) {
        if (node == nil) {
            return;
        }
        inorder(node.left, result);
        result.add((K) node.key);
        inorder(node.right, result);
    }

    @SuppressWarnings("unchecked")
    private void preorder(Node node, List<K> result) {
        if (node == nil) {
            return;
        }
        result.add((K) node.key);
        preorder(node.left, result);
        preorder(node.right, result);
    }

    private int height(Node node) {
        if (node == nil) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    private boolean isBST(Node node, K minExclusive, K maxExclusive) {
        if (node == nil) {
            return true;
        }
        @SuppressWarnings("unchecked")
        K key = (K) node.key;
        if (minExclusive != null && key.compareTo(minExclusive) <= 0) {
            return false;
        }
        if (maxExclusive != null && key.compareTo(maxExclusive) >= 0) {
            return false;
        }
        return isBST(node.left, minExclusive, key)
                && isBST(node.right, key, maxExclusive);
    }

    private int blackHeight(Node node) {
        if (node == nil) {
            return 1;
        }

        if (node.color == RED) {
            if (node.left.color != BLACK || node.right.color != BLACK) {
                return -1;
            }
        }

        int leftBlackHeight = blackHeight(node.left);
        int rightBlackHeight = blackHeight(node.right);

        if (leftBlackHeight == -1 || rightBlackHeight == -1 || leftBlackHeight != rightBlackHeight) {
            return -1;
        }

        return leftBlackHeight + (node.color == BLACK ? 1 : 0);
    }

    private int countNodes(Node node) {
        if (node == nil) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }
}
