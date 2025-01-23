package org.example;

import java.util.Random;

class IntComparable implements Comparable {
    private final int value;

    public IntComparable(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Object x) {
        return this.value - ((IntComparable) x).value;
    }
}

public class Main {
    public static void main(String[] args) {
        RBTree tree = new RBTree();
        Random rand = new Random();

        System.out.println("Einfügen von 15 zufälligen Zahlen in den RB-Baum:");
        for (int i = 0; i < 15; i++) {
            int num = rand.nextInt(100); // Zufallszahl zwischen 0 und 99
            System.out.println("Einfügen: " + num);
            tree.insertNode(new RBTree.Node(num));
        }
    }
}