package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import main.RedBlackTree;

/**
 * Testing class of individual methods and integration
 * of the Red Black Tree.
 * 
 * @author Jacob Cobler
 */
public class RedBlackTreeTest {
    public static void main(String[] args) {
        testEmptyTree();
        testInsertSearchAndTraversal();
        testDuplicateInsertUpdatesValue();
        testDeleteLeafNode();
        testDeleteNodeWithOneChild();
        testDeleteNodeWithTwoChildren();
        testMinimumMaximumAndHeight();
        testRandomStressAgainstTreeMap();
        testDeleteMissingKey();
        System.out.println("All RedBlackTree tests passed.");
    }

    private static void testEmptyTree() {
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        assertTrue(tree.isEmpty(), "new tree should be empty");
        assertEquals(0, tree.size(), "new tree size");
        assertEquals(null, tree.search(10), "searching empty tree");
        assertTrue(tree.validateRedBlackProperties(), "empty tree should validate");
    }

    private static void testInsertSearchAndTraversal() {
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        int[] keys = { 20, 15, 25, 10, 5, 1, 30, 22 };
        for (int key : keys) {
            tree.insert(key, "v" + key);
            assertTrue(tree.validateRedBlackProperties(), "tree should validate after inserting " + key);
        }

        assertEquals(8, tree.size(), "size after inserts");
        assertEquals("v22", tree.search(22), "search existing key");
        assertEquals(null, tree.search(99), "search missing key");
        assertEquals(List.of(1, 5, 10, 15, 20, 22, 25, 30), tree.inorderKeys(), "inorder traversal sorted");
    }

    private static void testDuplicateInsertUpdatesValue() {
        RedBlackTree<Integer, String> tree = new RedBlackTree<>();
        tree.insert(7, "old");
        tree.insert(7, "new");
        assertEquals(1, tree.size(), "duplicate key should not increase size");
        assertEquals("new", tree.search(7), "duplicate key should update value");
        assertTrue(tree.validateRedBlackProperties(), "tree should validate after duplicate update");
    }

    private static void testDeleteLeafNode() {
        RedBlackTree<Integer, Integer> tree = filledTree(10, 5, 15, 3, 7);
        assertTrue(tree.delete(3), "delete existing leaf");
        assertEquals(List.of(5, 7, 10, 15), tree.inorderKeys(), "after deleting leaf");
        assertTrue(tree.validateRedBlackProperties(), "tree should validate after leaf deletion");
    }

    private static void testDeleteNodeWithOneChild() {
        RedBlackTree<Integer, Integer> tree = filledTree(10, 5, 15, 3);
        assertTrue(tree.delete(5), "delete node with one child");
        assertEquals(List.of(3, 10, 15), tree.inorderKeys(), "after deleting one-child node");
        assertTrue(tree.validateRedBlackProperties(), "tree should validate after one-child deletion");
    }

    private static void testDeleteNodeWithTwoChildren() {
        RedBlackTree<Integer, Integer> tree = filledTree(20, 10, 30, 5, 15, 25, 35, 13, 17);
        assertTrue(tree.delete(10), "delete node with two children");
        assertEquals(List.of(5, 13, 15, 17, 20, 25, 30, 35), tree.inorderKeys(), "after deleting two-child node");
        assertTrue(tree.validateRedBlackProperties(), "tree should validate after two-child deletion");
    }

    private static void testMinimumMaximumAndHeight() {
        RedBlackTree<Integer, Integer> tree = filledTree(8, 4, 12, 2, 6, 10, 14);
        assertEquals(2, tree.minimumKey(), "minimum key");
        assertEquals(14, tree.maximumKey(), "maximum key");
        assertTrue(tree.height() <= 4, "height should be small for seven nodes");
        assertTrue(tree.validateRedBlackProperties(), "tree should validate after height/min/max test");
    }

    private static void testRandomStressAgainstTreeMap() {
        RedBlackTree<Integer, Integer> tree = new RedBlackTree<>();
        TreeMap<Integer, Integer> reference = new TreeMap<>();

        List<Integer> keys = IntStream.range(0, 500).boxed().collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(keys);

        for (Integer key : keys) {
            tree.insert(key, key * 10);
            reference.put(key, key * 10);
            assertTrue(tree.validateRedBlackProperties(), "tree should validate after random insert " + key);
        }

        assertEquals(new ArrayList<>(reference.keySet()), tree.inorderKeys(),
                "inorder should match TreeMap after inserts");

        Collections.shuffle(keys);
        for (int i = 0; i < 250; i++) {
            Integer key = keys.get(i);
            assertEquals(reference.remove(key) != null, tree.delete(key), "delete return value for " + key);
            assertTrue(tree.validateRedBlackProperties(), "tree should validate after random delete " + key);
        }

        assertEquals(new ArrayList<>(reference.keySet()), tree.inorderKeys(),
                "inorder should match TreeMap after deletes");
        for (Integer key : reference.keySet()) {
            assertEquals(reference.get(key), tree.search(key), "search should match TreeMap for " + key);
        }
    }

    private static void testDeleteMissingKey() {
        RedBlackTree<Integer, Integer> tree = filledTree(1, 2, 3);
        assertTrue(!tree.delete(99), "deleting missing key should return false");
        assertEquals(3, tree.size(), "missing delete should not change size");
        assertTrue(tree.validateRedBlackProperties(), "tree should validate after missing delete");
    }

    private static RedBlackTree<Integer, Integer> filledTree(int... keys) {
        RedBlackTree<Integer, Integer> tree = new RedBlackTree<>();
        for (int key : keys) {
            tree.insert(key, key);
        }
        return tree;
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " | expected=" + expected + ", actual=" + actual);
        }
    }
}
