/**
 *
 */
public class RedBlackTree<V extends Comparable<? super V>> {
    Node<V> root;

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
     * Retrieves the minimum value in the tree.
     *
     * @return the minimum value.
     */
    public V min() {
        Node<V> node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.value;
    }

    /**
     * Retrieves the minimum value in the tree.
     *
     * @return the minimum value.
     */
    public V max() {
        Node<V> node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.value;
    }

    public int size() {
        return root.size();
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
        Node<V> right = node.right;
        replace(node, right);
        node.right = right.left;
        if (right.left != null) {
            right.left.parent = node;
        }
        right.left = node;
        node.parent = right;
    }

    private void rotateRight(Node<V> node) {
        Node<V> left = node.left;
        replace(node, left);
        node.left = left.right;
        if (left.right != null) {
            left.right.parent = node;
        }
        left.right = node;
        node.parent = left;
    }

    void insert(V x) {
        insert(insertWhere(root, new Node<V>(x)));
    }

    Node<V> insertWhere(Node<V> parent, Node<V> node) {
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
            root = node;
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

    void delete(V x) {
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
                isBlack(sibling.right) &&
                isBlack(sibling.left)) {
            sibling.setRed();
            if (sibling.left != null) sibling.left.setBlack();
            rotateRight(sibling);
        } else if (node == node.parent.right &&
                sibling != null &&
                isBlack(sibling.left) &&
                isBlack(sibling.right)) {
            sibling.setRed();
            if (sibling.right != null) sibling.right.setBlack();
            rotateLeft(sibling);
        }
        deleteCase6(node);
    }

    private void deleteCase6(Node<V> node) {
        Node<V> sibling = node.sibling();
        if (sibling != null) {
            if (node.parent.isBlack()) {
                sibling.setBlack();
            } else {
                sibling.setRed();
            }
        }
        node.parent.setBlack();
        if (sibling != null) {
            if (node == node.parent.left) {
                sibling.right.setBlack();
            } else {
                sibling.left.setBlack();
            }
        }
        if (node == node.parent.left) {
            rotateLeft(node.parent);
        } else {
            rotateRight(node.parent);
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
            root = replacement;
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
