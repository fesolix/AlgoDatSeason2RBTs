package org.example;

public class RBTree {
    private Node root;

    private Node left;
    private Node right;
    private Node parent;

    // private Node grandParent;
    // private Node greatGrandParent;

    public RBTree() {}

    public void insertNode(Node newNode) {
        Node node = root; Node parent = null;
        // Traverse the tree to the left or right depending on the key
        while (node != null) {
            parent = node;
            if (newNode.data < node.data) {
                node = node.left;
            } else if (newNode.data > node.data) {
                node = node.right;
            } else {
                throw new IllegalArgumentException("BST already contains a node with key " + newNode.data);
            }
        }
        // Insert new node
        newNode.color = Color.RED;
        if (parent == null) {
            root = newNode;
        } else if (newNode.data < parent.data) {
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
        }

        // Fall 3: Vater und Onkelknoten sind rot
        // if (parent.color == Color.RED && grandParent.color == Color.RED) {}

        // Fall 4: Vater ist rot, Onkel ist schwarz, Knoten ist innerer Enkel
        // Fall 5: Vater ist rot, Onkel ist schwarz, Knoten ist äußerer Enkel
    }
}