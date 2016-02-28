import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Tests {@link RedBlackTree}.
 */
public class RedBlackTreeTest {
    @Test
    public void insert1valueAndTestForMin() {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        tree.insert(1);
        assertEquals(Integer.valueOf(1), tree.min());
    }

    @Test
    public void insert10valuesAndTestForMin() throws TreePropertyException {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        for (int i = 1; i <= 10; i++) {
            tree.insert(i);
        }
        verifyTree(tree);

        assertFalse(tree.contains(0));
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(10));
        assertEquals(Integer.valueOf(1), tree.min());
        assertEquals(Integer.valueOf(10), tree.max());
    }

    @Test
    public void treeWith10kEntriesIsValid() throws TreePropertyException {
        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        Random random = new Random();
        for (int i = 1; i <= 10000; i++) {
            tree.insert(random.nextInt(10000) - 5000);
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
            throw new TreePropertyException(String.format("Path black count to min %d does not match path black count" +
                            " to max %d",
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
