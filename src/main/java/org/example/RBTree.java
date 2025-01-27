package org.example;
import java.io.*;

interface Comparable {
    int compareTo(Object x);
}

public class RBTree {
    private enum Color {
        RED, BLACK
    }

    public static class Node implements Comparable {
        int value;
        Color color;
        Node left, right, parent;

        Node(int value) {
            this.value = value;
            this.color = Color.RED;
        }

        @Override
        public int compareTo(Object x) {
            return this.value - ((Node)x).value;
        }
    }

    private Node root;

    public RBTree() {
        root = null;
    }

    public void insertNode(Node newNode) {
        Node node = root; Node parent = null;
        // Traverse the tree to the left or right depending on the key
        while (node != null) {
            parent = node;
            if (newNode.compareTo(node) < 0) {
                node = node.left;
            } else if (newNode.compareTo(node) > 0) {
                node = node.right;
            } else {
                throw new IllegalArgumentException("BST already contains a node with key " + newNode.value);
            }
        }
        // Insert new node
        newNode.color = Color.RED;
        if (parent == null) {
            root = newNode;
        } else if (newNode.compareTo(parent) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;
        fixRedBlackPropertiesAfterInsert(newNode);
    }

    public void fixRedBlackPropertiesAfterInsert(Node newNode) {
        // 1) If newNode is the root, just color it black
        if (newNode == root) {
            newNode.color = Color.BLACK;
            return;
        }

        // 2) If newNode has no parent, it is effectively the root
        if (newNode.parent == null) {
            root = newNode;
            newNode.color = Color.BLACK;
            return;
        }

        // 3) If the parent is black, nothing to fix
        if (newNode.parent.color == Color.BLACK) {
            root.color = Color.BLACK; // Ensure root is black
            return;
        }

        // Now we know newNode != root and newNode.parent is RED
        Node parent = newNode.parent;
        Node grandParent = parent.parent;

        // 4) If there's no grandparent, parent is the root
        if (grandParent == null) {
            parent.color = Color.BLACK;
            root = parent;
            return;
        }

        // Determine whether parent is a left child or a right child of grandParent
        boolean parentIsLeftChild = (parent == grandParent.left);
        // Uncle is the sibling of parent
        Node uncle = parentIsLeftChild ? grandParent.right : grandParent.left;

        // (A) If uncle is red => recolor + recurse upward
        if (uncle != null && uncle.color == Color.RED) {
            parent.color = Color.BLACK;
            uncle.color = Color.BLACK;
            grandParent.color = Color.RED;
            fixRedBlackPropertiesAfterInsert(grandParent);
            return;
        }

        // (B) Uncle is black (or null)
        // (B1) Inner child => small rotation (parent)
        if (parentIsLeftChild && newNode == parent.right) {
            rotateLeft(parent);
            // After rotation, newNode has changed position
            newNode = parent;
            parent = newNode.parent;
            grandParent = (parent != null) ? parent.parent : null;
        } else if (!parentIsLeftChild && newNode == parent.left) {
            rotateRight(parent);
            newNode = parent;
            parent = newNode.parent;
            grandParent = (parent != null) ? parent.parent : null;
        }

        // (B2) Outer child => rotate at grandParent
        if (parent == null || grandParent == null) {
            // If that happens, just make whichever is not null the root and color it black
            if (parent != null) {
                root = parent;
                parent.color = Color.BLACK;
            } else {
                root = newNode;
                newNode.color = Color.BLACK;
            }
            return;
        }

        parent.color = Color.BLACK;
        grandParent.color = Color.RED;

        if (parentIsLeftChild) {
            rotateRight(grandParent);
        } else {
            rotateLeft(grandParent);
        }

        // Possibly continue fixing upwards if further conflicts remain
        fixRedBlackPropertiesAfterInsert(parent);

        // Ensure root is black
        root.color = Color.BLACK;
    }

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;
        replaceParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.left = node;
        node.parent = rightChild;
        replaceParentsChild(parent, node, rightChild);
    }

    private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }
        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    private static Object[][] nodeParents = new Object[10000][3];
    private void inorder(Node n) {
        int i = 0;
        if (n.left != null) inorder(n.left);
        if (n.parent != null) {

            while (nodeParents[i][0] != null) {
                i++;
            }
            nodeParents[i][0] = n.value;
            nodeParents[i][1] = n.color;
            nodeParents[i][2] = n.parent.value;

        }
        if (n.right != null) inorder(n.right);

    }
    public void printDOT(String file) throws IOException {
        nodeParents = new Object[10000][3];
        String startText = "digraph G {\n" +
                "\tgraph [ratio=.48];\n" +
                "\tnode [style=filled, color=black, shape=circle, width=.6 \n" +
                "\t\tfontname=Helvetica, fontweight=bold, fontcolor=white, \n" +
                "\t\tfontsize=24, fixedsize=true];\n" +
                "\t\n\n";
        String order = "  ";
        String redDots = "  ";
        String redText =
                "\t\n" +
                "\t[fillcolor=red];\n" +
                "\n";
        String connectDots = "";
        String endText = "\n" + "}";


        // connect inOrder
        inorder(root);
        int i = 0;

        while (nodeParents[i][0] != null) {
            if (nodeParents[i][1] == Color.RED) redDots = redDots.concat(nodeParents[i][0].toString() + ", ");
            connectDots = connectDots.concat(nodeParents[i][2] + " -> " + nodeParents[i][0] + "; \n");
            order = order.concat(nodeParents[i][0] + ", ");
            i++;
        }

        redDots = redDots.substring(0, redDots.length()-2);
        order = order.substring(0, order.length() -2) + ";\n\n";
        String input = startText.concat(order + redDots + redText + connectDots + endText);
        if (redDots.isEmpty()) input = startText.concat(root.value + ";" + endText);

        File out = new File(file);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {

            writer.write(input);
            writer.flush();
        }




    }
}