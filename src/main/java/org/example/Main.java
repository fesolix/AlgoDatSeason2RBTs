package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

    public static void main(String[] args) throws IOException {
        RBTree tree = new RBTree();
        Random rand = new Random();

        // bei Belieben ändern:
        String pfad = "C:\\Users\\Admin\\Desktop\\DOT\\Tree"; //-----------------------------------------------------------------------------------------------

        ArrayList<Integer> arrayList = new ArrayList<>();
        int currentDot = 10; // startet bei 10 für die Reihenfolge von PDFs

        System.out.println("Einfügen von 15 zufälligen Zahlen in den RB-Baum:");
        for (int i = 0; i < 15; i++) {
            int num = 0;
            // Zufallszahlen sollen sich nicht wiederholen, aber auch nicht möglicherweise in einer while(true) Schleife
            // gefangen sein
            for (int j = 0; j < 1000000; j++) {
                num = rand.nextInt(100); // Zufallszahl zwischen 0 und 99
                if (!arrayList.contains(num)) break;
            }
            arrayList.add(num);
            System.out.println("Einfügen: " + num);
            tree.insertNode(new RBTree.Node(num));

            //printDot mit variablen Pfad
            tree.printDOT(pfad + currentDot + ".dot");
            currentDot++;
        }

        // Testcase: (oben tree.insertNode bis currentDot++ auskommentieren)

        //int[] test = {74, 94, 72, 13, 80, 4, 61, 3, 90, 27, 51, 79, 76, 43, 44};
        //for (int j : test) {
            //System.out.println(j);
            //tree.insertNode(new RBTree.Node(j));
            //tree.printDOT("C:\\Users\\Admin\\Desktop\\DOT\\Tree" + currentDot + ".dot");
            currentDot++;
        //}
    }
}

// Windows ToolChain für einen Order DOT auf dem Desktop (verwendete Tools: graphviz(dot), inkscape(inkscape), pdfk(Pfad angegeben):

//     Get-ChildItem .\Desktop\DOT\*.dot | ForEach-Object { & "dot" -Tsvg $_.FullName > "$($_.FullName -replace '\.dot$', '.svg')" }
//
//     Get-ChildItem .\Desktop\DOT\*.svg | ForEach-Object { & "inkscape" $_.FullName --export-filename "$($_.FullName -replace '\.svg$', '.pdf')" }
//
//     C:\"Program Files (x86)"\PDFtk\bin\pdftk.exe .\Desktop\DOT\*.pdf cat output .\Desktop\combined.pdf