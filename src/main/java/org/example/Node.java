package org.example;

interface Comparable {
    public int compareTo(Object x);
}

public class Node implements Comparable {
    int value;

    Node left;
    Node right;
    Node parent;

    Color color;

    @Override
    public int compareTo(Object x) {
        return this.value - ((Node)x).value;
    }

    public Node(int value) {
        this.value = value;
    }
}