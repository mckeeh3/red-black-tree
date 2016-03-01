import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Tests {@link RedBlackTree}.
 */
public class RedBlackTreeTest {
    @Test
    public void emptyTreeHas0sizeAndIsEmpty() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    @Test
    public void add1valueAndTestForFirstAndLastValues() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        tree.add(1);
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
        assertEquals(Integer.valueOf(1), tree.first());
        assertEquals(Integer.valueOf(1), tree.last());
    }

    @Test
    public void clearNonEmptyTreeIsEmptyAndSizeIs0() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        for (int i = 1; i <= 10; i++) {
            tree.add(i);
        }
        assertFalse(tree.isEmpty());
        assertTrue(tree.size() > 0);

        tree.clear();

        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    @Test
    public void add10valuesAndTestForFirstAndLastValues() throws TreePropertyException {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        for (int i = 1; i <= 10; i++) {
            tree.add(i);
        }
        verifyTree(tree);

        assertFalse(tree.contains(0));
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(10));
        assertEquals(Integer.valueOf(1), tree.first());
        assertEquals(Integer.valueOf(10), tree.last());
    }

    @Test
    public void addSomeValuesAndTryVariousCeilingValues() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        assertNull(tree.ceiling(0));

        tree.add(0);
        assertEquals(Integer.valueOf(0), tree.ceiling(0));
        assertNull(tree.ceiling(1));

        for (int i = 1; i <= 10; i++) {
            tree.add(i);
        }

        assertEquals(Integer.valueOf(0), tree.ceiling(-1));
        assertEquals(Integer.valueOf(0), tree.ceiling(Integer.MIN_VALUE));
        assertEquals(Integer.valueOf(1), tree.ceiling(1));
        assertEquals(Integer.valueOf(10), tree.ceiling(10));
        assertNull(tree.ceiling(Integer.MAX_VALUE));
    }

    @Test
    public void addSomeValuesAndTryVariousHigherValues() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        assertNull(tree.higher(0));

        tree.add(0);
        assertEquals(Integer.valueOf(0), tree.higher(-1));
        assertNull(tree.higher(0));

        for (int i = 1; i <= 10; i++) {
            tree.add(i);
        }

        assertEquals(Integer.valueOf(0), tree.higher(-1));
        assertEquals(Integer.valueOf(0), tree.higher(Integer.MIN_VALUE));
        assertEquals(Integer.valueOf(2), tree.higher(1));
        assertEquals(Integer.valueOf(10), tree.higher(9));
        assertNull(tree.higher(10));
        assertNull(tree.higher(Integer.MAX_VALUE));
    }

    @Test
    public void addSomeValuesThenRemoveAndVerifyThatRemoveWorked() throws TreePropertyException {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        for (int i = 1; i <= 10; i++) {
            tree.add(i);
        }
        for (int i = 1; i <= 10; i++) {
            assertTrue(tree.contains(i));
        }

        assertEquals(10, tree.size());
        assertFalse(tree.isEmpty());

        assertTrue(tree.remove(1));
        assertFalse(tree.remove(1));
        assertEquals(9, tree.size());
        assertFalse(tree.contains(1));
        assertEquals(Integer.valueOf(2), tree.first());
        assertEquals(Integer.valueOf(10), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(5));
        assertFalse(tree.remove(5));
        assertEquals(8, tree.size());
        assertFalse(tree.contains(5));
        assertEquals(Integer.valueOf(2), tree.first());
        assertEquals(Integer.valueOf(10), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(10));
        assertFalse(tree.remove(10));
        assertEquals(7, tree.size());
        assertFalse(tree.contains(10));
        assertEquals(Integer.valueOf(2), tree.first());
        assertEquals(Integer.valueOf(9), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(3));
        assertFalse(tree.remove(3));
        assertEquals(6, tree.size());
        assertFalse(tree.contains(3));
        assertEquals(Integer.valueOf(2), tree.first());
        assertEquals(Integer.valueOf(9), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(7));
        assertFalse(tree.remove(7));
        assertEquals(5, tree.size());
        assertFalse(tree.contains(7));
        assertEquals(Integer.valueOf(2), tree.first());
        assertEquals(Integer.valueOf(9), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(4));
        assertFalse(tree.remove(4));
        assertEquals(4, tree.size());
        assertFalse(tree.contains(4));
        assertEquals(Integer.valueOf(2), tree.first());
        assertEquals(Integer.valueOf(9), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(8));
        assertFalse(tree.remove(8));
        assertEquals(3, tree.size());
        assertFalse(tree.contains(8));
        assertEquals(Integer.valueOf(2), tree.first());
        assertEquals(Integer.valueOf(9), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(2));
        assertFalse(tree.remove(2));
        assertEquals(2, tree.size());
        assertFalse(tree.contains(2));
        assertEquals(Integer.valueOf(6), tree.first());
        assertEquals(Integer.valueOf(9), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(6));
        assertFalse(tree.remove(6));
        assertEquals(1, tree.size());
        assertFalse(tree.contains(6));
        assertEquals(Integer.valueOf(9), tree.first());
        assertEquals(Integer.valueOf(9), tree.last());
        verifyTree(tree);

        assertTrue(tree.remove(9));
        assertFalse(tree.remove(9));
        assertEquals(0, tree.size());
        assertFalse(tree.contains(9));
        assertNull(tree.first());
        assertNull(tree.last());
        assertTrue(tree.isEmpty());
    }

    @Test
    public void treeWith10kEntriesIsValid() throws TreePropertyException {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        Random random = new Random();
        for (int i = 1; i <= 10000; i++) {
            tree.add(random.nextInt(10000) - 5000);
        }
        verifyTree(tree);
    }

    private void verifyTree(RedBlackTree<Integer> tree) throws TreePropertyException {
        verifyTreeProperty1(tree.root);
        verifyTreeProperty2(tree.root);
        verifyTreeProperty4(tree.root);
        verifyTreeProperty5(tree.root);
    }

    private void verifyTreeProperty1(Node<Integer> node) throws TreePropertyException {
        if (node != null) {
            if (node.color == Node.Color.Red || node.color == Node.Color.Black) {
                verifyTreeProperty1(node.left);
                verifyTreeProperty1(node.right);
            } else {
                throw new TreePropertyException(String.format("Node color must be black or red, %s", node));
            }
        }
    }

    private void verifyTreeProperty2(Node<Integer> root) throws TreePropertyException {
        if (root != null && root.isRed()) {
            throw new TreePropertyException("Tree root node color must be black.");
        }
    }

    private void verifyTreeProperty4(Node<Integer> node) throws TreePropertyException {
        if (node != null) {
            if (node.isRed() && (isRed(node.left) || isRed(node.right))) {
                throw new TreePropertyException(String.format("Red parent node has one or more red child nodes, %s",
                        node));
            } else if (node.isRed() && isRed(node.parent)) {
                throw new TreePropertyException(String.format("Red node must have black parent, %s", node));
            } else {
                verifyTreeProperty4(node.left);
                verifyTreeProperty4(node.right);
            }
        }
    }

    private void verifyTreeProperty5(Node<Integer> node) throws TreePropertyException {
        int pathBlackCountToMin = pathBlackCountToMinNode(node);
        int pathBlackCountToMax = pathBlackCountToMaxNode(node);
        if (pathBlackCountToMin != pathBlackCountToMax) {
            throw new TreePropertyException(String.format("Path black count to first %d does not match path black " +
                            "count" +
                            " to last %d",
                    pathBlackCountToMin, pathBlackCountToMax));
        } else {
            verifyTreeProperty5(node, pathBlackCountToMax, 0);
        }
    }

    private void verifyTreeProperty5(Node<Integer> node, int blackCount, int pathBlackCount) throws
            TreePropertyException {
        if (node == null && blackCount != pathBlackCount) {
            throw new TreePropertyException(String.format("Patch black count expected %d, patch black count found %d.",
                    blackCount, pathBlackCount));
        } else if (node != null) {
            pathBlackCount += isBlack(node) ? 1 : 0;
            verifyTreeProperty5(node.left, blackCount, pathBlackCount);
            verifyTreeProperty5(node.right, blackCount, pathBlackCount);
        }
    }

    private int pathBlackCountToMinNode(Node<Integer> node) {
        int blackCount = -1;
        while (node != null) {
            if (isBlack(node)) {
                blackCount++;
            }
            node = node.left;
        }
        if (blackCount == -1) {
            blackCount = 0;
        } else {
            blackCount += 1;
        }
        return blackCount;
    }

    private int pathBlackCountToMaxNode(Node<Integer> node) {
        int blackCount = -1;
        while (node != null) {
            if (isBlack(node)) {
                blackCount++;
            }
            node = node.right;
        }
        if (blackCount == -1) {
            blackCount = 0;
        } else {
            blackCount += 1;
        }
        return blackCount;
    }

    private static class TreePropertyException extends Exception {
        private TreePropertyException(String message) {
            super(message);
        }
    }

    private boolean isBlack(Node<Integer> node) {
        return node == null || node.color == Node.Color.Black;
    }

    private boolean isRed(Node<Integer> node) {
        return !isBlack(node);
    }
}
