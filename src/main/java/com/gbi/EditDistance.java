package com.gbi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Compute edit distance using dynamic programming
 * STUDENT SOLTUION BY: YOUR_NAMES_HERE_PLEASE
 * for GBI, 29.4.2021
 */

public class EditDistance {
    int d [][];

    /**
     * computes the edit distance between two sequences using dynamic programming
     *
     * @param x first sequence
     * @param y second sequence
     * @return edit distance
     */
    public int align(String x, String y) {
        d = new int[x.length() +1][y.length() +1];
        for (int i = 0; i <= x.length(); ++i) {
            d[i][0] = i;
        }
        for (int j = 0; j <= y.length(); ++j) {
            d[0][j] = j;
        }
        for (int i = 1; i <= x.length(); ++i) {
            for (int j = 1; j <= y.length(); ++j) {
                if (x.charAt(i-1) == y.charAt(j-1)) {
                    d[i][j] = min(d[i-1][j-1] +0, d[i][j-1] +1, d[i-1][j] +1);
                } else {
                    d[i][j] = min(d[i-1][j-1] +1, d[i][j-1] +1, d[i-1][j] +1);
                }
            }
        }
        return d[x.length()][y.length()];
    }

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * perform traceback and print an optimal alignment to  the console (standard output)
     */
    public void traceBackAndShowAlignment() {
        // PLEASE IMPLEMENT (Practical Assignment 3)
    }

    /**
     * main program: reads two sequences in fastA format and computes their optimal alignment score.
     *
     * @param args commandline arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("YOUR_NAME_HERE_PLEASE");  // Hallo

        if (args.length != 1)  // zweite änderung, aber hallo test
            throw new IOException("Usage: EditDistanceDP fileName");

        String fileName = args[0];
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        FastA fastA = new FastA();
        fastA.read(reader);
        reader.close();

        EditDistance editDistanceDP = new EditDistance();

        if (fastA.size() == 2) {
            int editDistance = editDistanceDP.align(fastA.getSequence(0), fastA.getSequence(1));

            System.out.println("Edit distance is=" + editDistance);

            System.out.println("An optimal alignment=");
            editDistanceDP.traceBackAndShowAlignment();
        }
    }
}
