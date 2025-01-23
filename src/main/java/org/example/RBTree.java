package org.example;

interface Comparable {
    public int compareTo(Object x);
}

public class RBTree {
    private enum Color {
        RED, BLACK
    }

    private static class Node implements Comparable {
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
        // Fall 1: Der neue Knoten ist die Wurzel
        // Fall 2: Der Vater ist die Wurzel und rot
        if (root.color == Color.RED) {
            root.color = Color.BLACK;
            return;
        }

        Node grandParent = newNode.parent.parent;
        if (grandParent == null) {
            return;
        }

        // Fall 3: Vater und Onkelknoten sind rot
        // Bestimmen, ob der Vater links oder rechts vom Großvater ist
        if (newNode.parent == grandParent.left) {
            Node uncle = grandParent.right;

            // Fall A: Onkel ist rot => Recolor und höher prüfen
            if (uncle != null && uncle.color == Color.RED) {
                newNode.parent.color = Color.BLACK;
                uncle.color = Color.BLACK;
                grandParent.color = Color.RED;
                fixRedBlackPropertiesAfterInsert(grandParent);
            }
            // Fall B: Onkel ist schwarz
            else {
                // innerer Enkel => Linksdrehung beim Vater
                if (newNode == newNode.parent.right) {
                    rotateLeft(newNode.parent);
                    // Nach der Rotation ist newNode.parent gewechselt
                    // Verschiebe Fokus auf das ehemals "linke Kind"
                    newNode = newNode.left;
                }
                // äußerer Enkel => Rechtsdrehung beim Großvater
                newNode.parent.color = Color.BLACK;
                grandParent.color = Color.RED;
                rotateRight(grandParent);
            }
        } else {
            // Vater ist rechtes Kind vom grandParent
            Node uncle = grandParent.left;

            // Fall A: Onkel ist rot
            if (uncle != null && uncle.color == Color.RED) {
                newNode.parent.color = Color.BLACK;
                uncle.color = Color.BLACK;
                grandParent.color = Color.RED;
                fixRedBlackPropertiesAfterInsert(grandParent);
            }
            // Fall B: Onkel ist schwarz
            else {
                // innerer Enkel => Rechtsdrehung beim Vater
                if (newNode == newNode.parent.left) {
                    rotateRight(newNode.parent);
                    newNode = newNode.right;
                }
                // äußerer Enkel => Linksdrehung beim Großvater
                newNode.parent.color = Color.BLACK;
                grandParent.color = Color.RED;
                rotateLeft(grandParent);
            }
        }

        // Fall 4: Vater ist rot, Onkel ist schwarz, Knoten ist innerer Enkel
        // Fall 5: Vater ist rt, Onkel ist schwarz, Knoten ist äußerer Enkel
        if (newNode.parent.color == Color.RED && newNode == newNode.parent.left) {
            rotateRight(newNode.parent);
            rotateLeft(newNode.parent.parent);

            newNode.parent.color = Color.BLACK;
            newNode.parent.parent.color = Color.RED;
        } else if (newNode.parent.color == Color.RED && newNode == newNode.parent.right) {
            rotateLeft(newNode.parent.parent);

            newNode.parent.color = Color.BLACK;
            newNode.parent.parent.color = Color.RED;
        }
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

    public static void main(String[] args) {
        RBTree tree = new RBTree();

        tree.insertNode(new Node(15));
        tree.insertNode(new Node(9));
        tree.insertNode(new Node(17));
        tree.insertNode(new Node(73));
        tree.insertNode(new Node(23));
        tree.insertNode(new Node(24));
        System.out.println(tree.root.value);
        System.out.println(tree.root.color);
    }
}