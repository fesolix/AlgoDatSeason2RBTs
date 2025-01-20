package org.example;

public class Node {
    int data;

    Node left;
    Node right;
    Node parent;

    Color color;

    public Node(int data) {
        this.data = data;
    }
}