package org.example;

import java.io.*;

interface Comparable<T> {
    int compareTo(T x);
}

public class RBTree<T extends Comparable<T>> {
    private enum Color {
        RED, BLACK
    }


    public static class Node<T extends Comparable<T>> {
        T value;
        Color color;
        Node<T> left, right, parent;

        Node(T value) {
            this.value = value;
            this.color = Color.RED;
        }
    }

    private Node<T> root;

    public RBTree() {
        root = null;
    }

    public void insert(T value) {
        Node<T> newNode = new Node<>(value);
        insertNode(newNode);
    }

    private void insertNode(Node<T> newNode) {
        Node<T> node = root, parent = null;
        while (node != null) {
            parent = node;
            if (newNode.value.compareTo(node.value) < 0) { // neuer Knoten ist kleiner als der Vater -> nach links
                node = node.left;
            } else if (newNode.value.compareTo(node.value) > 0) { // neuer Knoten ist größer als der Vater -> nach rechts
                node = node.right;
            } else {
                throw new IllegalArgumentException("BST already contains value: " + newNode.value); // wir akzeptieren keine gleichgroßen Werte
            }
        }

        newNode.color = Color.RED;
        newNode.parent = parent;
        if (parent == null) {
            root = newNode;
        } else if (newNode.value.compareTo(parent.value) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixRedBlackPropertiesAfterInsert(newNode);
    }

    private void fixRedBlackPropertiesAfterInsert(Node<T> node) {
        // Fall 2-5
        if (node != root && node.parent.color == Color.RED) {
            Node<T> parent = node.parent;
            Node<T> grandParent = parent.parent;

            // Fall 2: Der Vater ist die Wurzel und rot
            if (grandParent == null) {
                root.color = Color.BLACK;
                return;
            }

            boolean isLeftChild = (parent == grandParent.left);
            Node<T> uncle = isLeftChild ? grandParent.right : grandParent.left;

            // Fall 3: Vater und Onkelknoten sind rot
            if (uncle != null && uncle.color == Color.RED) {
                parent.color = Color.BLACK;
                uncle.color = Color.BLACK;
                grandParent.color = Color.RED;
                fixRedBlackPropertiesAfterInsert(grandParent);
            } else {
                // Fall 5: Vater ist rot, Onkel ist schwarz, Knoten ist “äußerer Enkel”
                if (isLeftChild && node == parent.right) {
                    rotateLeft(parent);
                    node = parent;
                    parent = node.parent;
                } else if (!isLeftChild && node == parent.left) {
                    rotateRight(parent);
                    node = parent;
                    parent = node.parent;
                }
                // Fall 4 (und Fortsetzung 5): Vater ist rot, Onkel ist schwarz, Knoten ist “innerer Enkel”
                parent.color = Color.BLACK;
                grandParent.color = Color.RED;
                if (isLeftChild) {
                    rotateRight(grandParent);
                } else {
                    rotateLeft(grandParent);
                }
                fixRedBlackPropertiesAfterInsert(node);
            }
        }
        // Fall 1: Der neue Knoten ist die Wurzel
        root.color = Color.BLACK;
    }

    private void rotateLeft(Node<T> node) {
        Node<T> rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        replaceParentChild(node.parent, node, rightChild);
        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node<T> node) {
        Node<T> leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        replaceParentChild(node.parent, node, leftChild);
        leftChild.right = node;
        node.parent = leftChild;
    }

    private void replaceParentChild(Node<T> parent, Node<T> oldChild, Node<T> newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else {
            parent.right = newChild;
        }
        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    private static Object[][] nodeParents = new Object[10000][2];
    int highestIndex;
    private void inorder(Node<T> n, int i) {
        if (i > highestIndex) highestIndex = i;
        if (n.left != null) inorder(n.left, 2*i+1); // solange links suchen bis keine Knoten mehr da sind, index des Kindes mitgeben

        nodeParents[i][0] = n.value; // Value speichern
        nodeParents[i][1] = n.color; // Color speichern

        if (n.right != null) inorder(n.right, 2*i+2); // solange rechts suchen bis keine Knoten mehr da sind, index des Kindes mitgeben

    }
    public void printDOT(String file) throws IOException {
        nodeParents = new Object[10000][2]; // Array erstmal (wieder) leeren
        highestIndex = 0;
        // Aufbau der Datei
        String startText = "digraph G {\n" +
                "\tgraph [ratio=.48];\n" +
                "\tnode [style=filled, color=black, shape=circle, width=.6 \n" +
                "\t\tfontname=Helvetica, fontweight=bold, fontcolor=white, \n" +
                "\t\tfontsize=24, fixedsize=true];\n" +
                "\t\n\n";
        String leftSingleParents = "  "; // linke Werte müssen als Erstes initialisiert werden in dem Programm (hier die Geisternodes)
        String order = "  "; // linke Werte müssen als Erstes initialisiert werden
        String rightSingleParents = "  ";
        String redDots = "  "; // alle Knoten, die rot sind, so färben
        String redText =
                "\t\n" +
                "\t[fillcolor=red];\n" +
                "\n";
        String connectDots = ""; // die Verbindungen anzeigen
        String endText = "\n" + "}";


        // über inOrder nodeParents befüllen
        inorder(root, 0);
        int i = 0;

        // jede Stelle durchgehen
        while (i <= highestIndex) {

            if (nodeParents[((i-1)/2)][0] != null) {

                // Code für Geisterknoten, damit die links-rechts-Einrückung teilweise funktioniert
                /*
                if (nodeParents[i][0] == null) {
                    if (i % 2 == 1 && nodeParents[i + 1][0] != null) {
                        leftSingleParents = leftSingleParents.concat((1000 + i) + " [label=\"\",style=invis]; \n");
                        connectDots = connectDots.concat(((i - 1) / 2 + 1) + " -> " + (1000 + i) + " [style=invis]; \n");
                    } else if (i % 2 == 0 && nodeParents[i - 1][0] != null) {
                        rightSingleParents = rightSingleParents.concat((1000 + i) + " [label=\"\",style=invis]; \n");
                        connectDots = connectDots.concat(((i - 1) / 2 + 1) + " -> " + (1000 + i) + " [style=invis]; \n");
                    }
                }
                else */

                // falls an diesem Index Knoten vorhanden
                if (nodeParents[i][0] != null) {
                    // i --> Index vom Array, i+1 --> Index für Graphviz
                    if (nodeParents[i][1] == Color.RED) redDots = redDots.concat((i + 1) + ", "); // falls rot, zu redDots hinzufügen
                    if (i != 0) connectDots = connectDots.concat((((i - 1) / 2) + 1) + " -> " + (i + 1) + "; \n"); // falls nicht die root, mit Elternteil verknüpfen
                    order = order.concat((i + 1) + " [label=\"" + nodeParents[i][0] + "\"]; \n "); // zur Reihenfolge hinzufügen
                }
            }
            i++;
        }

        // hier haben wir mit Kommas gearbeitet, die dann vom letzten Element jeweils wieder entfernt werden müssen
        redDots = redDots.substring(0, redDots.length()-2);
        order = order.substring(0, order.length() -2) + "\n\n";

        // die Reihenfolge der String-Elemente der Datei (leftSingleParents und rightSingleParents sind für die Geisterknoten)
        String input = startText.concat(leftSingleParents + order + rightSingleParents + redDots + redText + connectDots + endText);
        // falls nur die Root existiert
        if (redDots.isEmpty()) input = startText.concat("1 [label=\"" + root.value + "\"];" + endText);

        File out = new File(file);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {

            writer.write(input);
            writer.flush();
        }
    }
}