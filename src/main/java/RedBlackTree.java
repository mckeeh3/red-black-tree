/**
 * A Red-black tree implementation. Values are stored in the tree
 */
public class RedBlackTree<V extends Comparable<? super V>> {
    Node<V> root;

    /**
     * Add the specified value to the tree.
     *
     * @param x the value to be added
     */
    public void add(V x) {
        insert(insertWhere(root, new Node<V>(x)));
    }

    /**
     * Removes the specified value from the tree if it is present. Returns true if the tree contained the element.
     *
     * @param value the value to be removed from this tree
     * @return true if the tree contained the specified value
     */
    public boolean remove(V value) {
        return delete(value);
    }

    /**
     * Removes all of the values from the tree.
     */
    public void clear() {
        root = null;
    }

    /**
     * Return true if the tree contains no values.
     *
     * @return true if the tree contains no values
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns true if tree contains the specified value.
     *
     * @param value the value whose presence is checked for in the tree
     * @return true if the tree contains the specified value
     */
    public boolean contains(V value) {
        return find(value) != null;
    }

    /**
     * Returns the least value in the tree greater then or equal to the given value, or null if there is no such value.
     *
     * @param value the value to match
     * @return the least value greater than or equal to the specified value, or null if there is no such value
     */
    public V ceiling(V value) {
        Node<V> node = root;

        while (node != null) {
            int compare = node.value.compareTo(value);
            if (compare == 0 || compare > 0 && node.right == null) {
                return node.value;
            }

            if (node.left != null && value.compareTo(last(node.left)) <= 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    /**
     * Returns the least value in the tree strictly greater then the given value, or null if there is no such value.
     *
     * @param value the value to match
     * @return the least value greater than the value, or null is there is no such value
     */
    public V higher(V value) {
        Node<V> node = root;

        while (node != null) {
            int compare = node.value.compareTo(value);
            if (compare > 0 && node.isChildless()) {
                return node.value;
            }

            if (node.left != null && value.compareTo(last(node.left)) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    /**
     * Retrieves the first (lowest) value in the tree.
     *
     * @return the first value.
     */
    public V first() {
        return first(root);
    }

    private V first(Node<V> node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    /**
     * Retrieves the last (highest) value in the tree.
     *
     * @return the last value.
     */
    public V last() {
        return last(root);
    }

    private V last(Node<V> node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node.value;
    }

    /**
     * Returns the number of values in the tree.
     *
     * @return the number of values in the tree
     */
    public int size() {
        return root == null ? 0 : root.size();
    }

    @Override
    public String toString() {
        return String.format("%s[%s (%d)]", getClass().getSimpleName(), root, size());
    }

    private void setRoot(Node<V> node) {
        root = node;
        if (node != null) {
            root.setBlack();
        }
    }

    private Node<V> find(V value) {
        Node<V> node = root;
        while (node != null) {
            if (node.value == value) {
                return node;
            } else if (value.compareTo(node.value) < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    private void rotateLeft(Node<V> node) {
        if (node != null) {
            Node<V> right = node.right;
            replace(node, right);
            node.right = right == null ? null : right.left;
            if (right != null) {
                if (right.left != null) {
                    right.left.parent = node;
                }
                right.left = node;
            }
            node.parent = right;
        }
    }

    private void rotateRight(Node<V> node) {
        if (node != null) {
            Node<V> left = node.left;
            replace(node, left);
            node.left = left == null ? null : left.right;
            if (left != null) {
                if (left.right != null) {
                    left.right.parent = node;
                }
                left.right = node;
            }
            node.parent = left;
        }
    }

    private Node<V> insertWhere(Node<V> parent, Node<V> node) {
        if (parent == null) {
            node.setBlack();
        } else {
            if (node.value.compareTo(parent.value) < 0) {
                if (parent.left == null) {
                    node.parent = parent;
                    parent.left = node;
                } else {
                    node = insertWhere(parent.left, node);
                }
            } else {
                if (parent.right == null) {
                    node.parent = parent;
                    parent.right = node;
                } else {
                    node = insertWhere(parent.right, node);
                }
            }
        }
        return node;
    }

    private void insert(Node<V> node) {
        if (root == null) {
            setRoot(node);
        }
        insertCase1(node);
    }

    private void insertCase1(Node<V> node) {
        if (node.isRoot()) {
            node.setBlack();
        } else {
            insertCase2(node);
        }
    }

    private void insertCase2(Node<V> node) {
        if (node.parent.isRed()) {
            insertCase3(node);
        }
    }

    private void insertCase3(Node<V> node) {
        Node<V> grandparent;
        Node<V> uncle = node.uncle();
        if (uncle != null && uncle.isRed()) {
            node.parent.setBlack();
            uncle.setBlack();
            grandparent = node.grandparent();
            grandparent.setRed();
            insertCase1(grandparent);
        } else {
            insertCase4(node);
        }
    }

    private void insertCase4(Node<V> node) {
        Node<V> grandparent = node.grandparent();
        if (node == node.parent.right && node.parent == grandparent.left) {
            rotateLeft(node.parent);
            node = node.left;
        } else if (node == node.parent.left && node.parent == grandparent.right) {
            rotateRight(node.parent);
            node = node.right;
        }
        insertCase5(node);
    }

    private void insertCase5(Node<V> node) {
        Node<V> grandparent = node.grandparent();
        node.parent.setBlack();
        grandparent.setRed();
        if (node == node.parent.left && node.parent == grandparent.left) {
            rotateRight(grandparent);
        } else if (node == node.parent.right && node.parent == grandparent.right) {
            rotateLeft(grandparent);
        }
    }

    private boolean delete(V x) {
        Node<V> node = find(x);
        if (node != null) {
            if (!isLeaf(node.left) && !isLeaf(node.right)) {
                node = copyMaxPredecessor(node);
            }
            Node<V> child = isLeaf(node.right) ? node.left : node.right;
            if (node.isBlack()) {
                if (!isBlack(child)) {
                    node.setRed();
                }
                deleteCase1(node);
            }
            replace(node, child);
        }
        return node != null;
    }

    private void deleteCase1(Node<V> node) {
        if (node.parent != null) {
            deleteCase2(node);
        }
    }

    private void deleteCase2(Node<V> node) {
        Node<V> sibling = node.sibling();
        if (isRed(sibling)) {
            node.parent.setRed();
            sibling.setBlack();
            if (node == node.parent.left) {
                rotateLeft(node.parent);
            } else {
                rotateRight(node.parent);
            }
        }
        deleteCase3(node);
    }

    private void deleteCase3(Node<V> node) {
        Node<V> sibling = node.sibling();
        if (node.parent.isBlack() &&
                sibling != null &&
                isBlack(sibling) &&
                isBlack(sibling.left) &&
                isBlack(sibling.right)) {
            sibling.setRed();
            deleteCase1(node.parent);
        } else {
            deleteCase4(node);
        }
    }

    private void deleteCase4(Node<V> node) {
        Node<V> sibling = node.sibling();
        if (node.parent.isRed() &&
                sibling != null &&
                isBlack(sibling) &&
                isBlack(sibling.left) &&
                isBlack(sibling.right)) {
            sibling.setRed();
            node.parent.setBlack();
        } else {
            deleteCase5(node);
        }
    }

    private void deleteCase5(Node<V> node) {
        Node<V> sibling = node.sibling();
        if (node == node.parent.left &&
                sibling != null &&
                sibling.isBlack() &&
                isRed(sibling.left) &&
                isBlack(sibling.right)) {
            sibling.setRed();
            if (sibling.left != null) sibling.left.setBlack();
            rotateRight(sibling);
        } else if (node == node.parent.right &&
                sibling != null &&
                isBlack(sibling) &&
                isBlack(sibling.left) &&
                isRed(sibling.right)) {
            sibling.setRed();
            if (sibling.right != null) sibling.right.setBlack();
            rotateLeft(sibling);
        }
        deleteCase6(node);
    }

    private void deleteCase6(Node<V> node) {
        Node<V> sibling = node.sibling();
        setColorOfOther(sibling, node.parent);
        setBlack(node.parent);
        if (node == node.parent.left) {
            setBlack(sibling.right);
            rotateLeft(node.parent);
        } else {
            setBlack(sibling.left);
            rotateRight(node.parent);
        }
    }

    private void setBlack(Node<V> node) {
        if (node != null) {
            node.setBlack();
        }
    }

    private void setRed(Node<V> node) {
        if (node != null) {
            node.setRed();
        }
    }

    private void setColorOfOther(Node<V> node, Node<V> other) {
        if (node != null && other != null) {
            node.color = other.color;
        }
    }

    private boolean isRed(Node<V> node) {
        return node != null && node.isRed();
    }

    private boolean isBlack(Node<V> node) {
        return node == null || node.isBlack();
    }

    private boolean isLeaf(Node<V> node) {
        return node == null;
    }

    private Node<V> copyMaxPredecessor(Node<V> node) {
        Node<V> predecessor = maxPredecessor(node);
        node.value = predecessor.value;
        return predecessor;
    }

    private Node<V> maxPredecessor(Node<V> node) {
        node = node.left;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void replace(Node<V> node, Node<V> replacement) {
        if (node.isRoot()) {
            setRoot(replacement);
        } else {
            if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
        }
        if (replacement != null) {
            replacement.parent = node.parent;
        }
    }
}
