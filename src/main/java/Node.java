/**
 * A Red-black tree node. The node value must implement the {@link Comparable} interface.
 */
class Node<V extends Comparable<? super V>> {
    enum Color {Red, Black}

    Node<V> left;
    Node<V> right;
    Node<V> parent;
    Color color;
    V value;

    Node(V value) {
        this.value = value;
        color = Color.Red;
    }

    void setBlack() {
        color = Color.Black;
    }

    void setRed() {
        color = Color.Red;
    }

    boolean isBlack() {
        return color == Color.Black;
    }

    boolean isRed() {
        return color == Color.Red;
    }

    boolean isRoot() {
        return parent == null;
    }

    boolean isChildless() {
        return left == null && right == null;
    }

    Node<V> grandparent() {
        if (parent != null) {
            return parent.parent;
        } else {
            return null;
        }
    }

    Node<V> uncle() {
        Node<V> grandparent = grandparent();
        if (grandparent == null) {
            return null;
        } else if (parent == grandparent.left) {
            return grandparent.right;
        } else {
            return grandparent.left;
        }
    }

    int size() {
        return 1 + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
    }

    Node<V> sibling() {
        if (this == parent.left) {
            return parent.right;
        } else {
            return parent.left;
        }
    }

    @Override
    public String toString() {
        String leftInfo = left == null ? "" : String.format(" [left %s, %s]", left.value, left.color);
        String rightInfo = right == null ? "" : String.format(" [right %s, %s]", right.value, right.color);
        return String.format("%s[%s, %s%s%s]", getClass().getSimpleName(), value, color, leftInfo, rightInfo);
    }
}

