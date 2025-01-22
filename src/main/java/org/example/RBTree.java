package org.example;
import java.util.*;

public class RBTree {
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
        // Fall 1: Der neue Knoten ist die Wurzel
        // Fall 2: Der Vater ist die Wurzel und rot
        if (root.color == Color.RED) {
            root.color = Color.BLACK;
            return;
        }

        // Fall 3: Vater und Onkelknoten sind rot
        if (newNode.parent.color == Color.RED && newNode.parent.parent.left.color == Color.RED) {
            Node node = newNode;
            do {
                node.parent.color = Color.BLACK;
                node.parent.parent.left.color = Color.BLACK;

                if (node.parent.parent != root) {
                    node.parent.parent.color = Color.RED;
                }

                node = node.parent;
            } while (node.parent.parent != root);
        }

        // Fall 4: Vater ist rot, Onkel ist schwarz, Knoten ist innerer Enkel
        // Fall 5: Vater ist rt, Onkel ist schwarz, Knoten ist äußerer Enkel
    }

    public static void main(String[] args) {
        RBTree tree = new RBTree();

        tree.insertNode(new Node(15));
        tree.insertNode(new Node(9));
        tree.root.left.color = Color.BLACK;
        tree.insertNode(new Node(17));
        tree.root.right.color = Color.BLACK;
        tree.insertNode(new Node(16));
        tree.insertNode(new Node(73));
        tree.insertNode(new Node(79));
        System.out.println(tree.root.value);
        System.out.println(tree.root.color);
    }
}