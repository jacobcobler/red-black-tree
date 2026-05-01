public class RedBlackTree<K extends Comparable<K>, V> {
    private enum Color {
        RED,
        BLACK
    }

    private final Node NIL;
    private Node root;
    private int size;

    private final class Node {
        K key;
        V value;
        Node left;
        Node right;
        Node parent;
        Color color;

        Node(K key, V value, Color color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    public RedBlackTree() {
        NIL = new Node(null, null, Color.BLACK);
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.parent = NIL;
        root = NIL;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = NIL;
        NIL.parent = NIL;
        size = 0;
    }
}
