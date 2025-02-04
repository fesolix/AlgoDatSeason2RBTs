package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class IntComparable implements Comparable<IntComparable> {
    private final int value;

    public IntComparable(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(IntComparable x) {
        return this.value - x.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

public class Main {

    public static void main(String[] args) throws IOException {
        RBTree<IntComparable> tree = new RBTree<>();
        Random rand = new Random();

        // variabler Pfad für die Dateien
        String pfad = "C:\\Users\\Admin\\Desktop\\DOT\\Tree";
        ArrayList<Integer> arrayList = new ArrayList<>(); // Liste mit Zufallswerten, um Wiederholungen zu vermeiden
        int currentDot = 10;

        for (int i = 0; i < 15; i++) {
            int num;
            for (int j = 0; j < 1000000; j++) { // wiederhole 1 million mal, um sicherzugehen, dass ein neuer Wert gefunden wird, aber kein while(true), damit das Programm auch irgendwann abbrechen kann
                num = rand.nextInt(200)-100; // Zufallszahl von -100 bis 100
                if (!arrayList.contains(num)) { // falls Zahl in Liste, neue Zufallszahl ausrechnen
                    arrayList.add(num);

                    System.out.println("Einfügen: " + num);
                    tree.insert(new IntComparable(num)); // insert Methode in den Baum
                    tree.printDOT(pfad + currentDot + ".dot"); // printDOT mit einzigartigen Dateinamen durch currentDot (fängt bei 10 an, für die Reihenfolge der PDFs)
                    currentDot++;
                    break;
                }
            }
        }
    }
}