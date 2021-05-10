package com.gbi.assignment_3;

import com.gbi.FastA;

import java.io.BufferedReader;
        import java.io.FileReader;
        import java.io.IOException;

/**
 * Compute edit distance using dynamic programming
 * STUDENT SOLUTION BY: Friederike Moroff, Gwendolyn Gusak
 * for GBI, 29.4.2021
 */
public class SmithWaterman {
    int[][] edMatrix; //Empty 2D array for matrix
    TbDirection[][] tbMatrix;
    String x;
    String y;

    public static class Score {
        public int i = -1;
        public int j = -1;
        public int score = 0;
    }

    public enum TbDirection {
        Diagonal,
        Left,
        Above,
        Undefined
    }

    //Calculation syntax for the edit distance scores
    private int max(int a, int b, int c) {
        return Math.max(0, (Math.max(Math.max(a, b), c)));
    }
    private TbDirection maxDirection(int diagonal, int left, int above) {
        if (diagonal == max(diagonal, left, above)) {
            return TbDirection.Diagonal;
        } else if (left == max(diagonal, left, above)){
            return TbDirection.Left;
        } else if (above == max(diagonal, left, above)) {
            return TbDirection.Above;
        } else {
            return TbDirection.Undefined;
        }
    }

    /**
     * Computes the edit distance between two sequences using dynamic programming
     *
     * @param x             first sequence
     * @param y             second sequence
     * @param matchScore    match score
     * @param mismatchScore mismatch score
     * @param gapPenalty    gap penalty
     * @return edit distance
     */
    public Score align(String x, String y, int matchScore, int mismatchScore, int gapPenalty) {
        this.x = x;
        this.y = y;
        Score score = new Score();
        //Edit distance matrix length initialisation
        edMatrix = new int[x.length() + 1][y.length() + 1];
        tbMatrix = new TbDirection[x.length() + 1][y.length() + 1];

        //Initialisation
        for (int i = 0; i <= x.length(); ++i) {
            edMatrix[i][0] = 0;
        }
        for (int j = 0; j <= y.length(); ++j) {
            edMatrix[0][j] = 0;
        }

        //Calculate scores
        for (int i = 1; i <= x.length(); ++i) {
            for (int j = 1; j <= y.length(); ++j) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    edMatrix[i][j] = max(edMatrix[i - 1][j - 1] + matchScore, edMatrix[i][j - 1] - gapPenalty, edMatrix[i - 1][j] - gapPenalty);
                    tbMatrix[i][j] = maxDirection(edMatrix[i - 1][j - 1] + matchScore, edMatrix[i][j - 1] - gapPenalty, edMatrix[i - 1][j] - gapPenalty);
                } else {
                    edMatrix[i][j] = max(edMatrix[i - 1][j - 1] + mismatchScore, edMatrix[i][j - 1] - gapPenalty, edMatrix[i - 1][j] - gapPenalty);
                    tbMatrix[i][j] = maxDirection(edMatrix[i - 1][j - 1] + mismatchScore, edMatrix[i][j - 1] - gapPenalty, edMatrix[i - 1][j] - gapPenalty);
                }
                if (edMatrix[i][j] > score.score) {
                    score.score = edMatrix[i][j];
                    score.i = i;
                    score.j = j;
                }
            }
        }

        //Return optimal score for alignment (edit distance)
        return score;
    }

    /**
     * perform traceback and print an optimal alignment to  the console (standard output)
     */
    public String traceBackAndShowAlignment(Score score) {
        int diagonalCondition;
        StringBuilder xAligned = new StringBuilder();
        StringBuilder yAligned = new StringBuilder();

        //Traceback through edit distance matrix & alignment of chars for each sequence
        for (int i = score.i, j = score.j; i > 0 && j > 0; ) {
            if (edMatrix[i][j] == 0) {
                break;
            }
            switch (tbMatrix[i][j]) {
                case Diagonal:
                    xAligned.insert(0,x.charAt(i-1));
                    yAligned.insert(0,y.charAt(j-1));
                    --i;
                    --j;
                    break;
                case Left:
                    xAligned.insert(0,x.charAt(i-1));
                    yAligned.insert(0,"-");
                    --i;
                    break;
                case Above:
                    xAligned.insert(0,"-");
                    yAligned.insert(0,y.charAt(j-1));
                    --j;
                    break;
                case Undefined:
                    xAligned.insert(0,"@");
                    yAligned.insert(0,"@");
            }
        }


        // Print optimal alignment of x and y to console in correct order
        System.out.println(xAligned.toString());
        System.out.println(yAligned.toString());
        return xAligned.toString() + ':' + yAligned.toString();

    }

    /**
     * main program: reads two sequences in fastA format and computes their optimal alignment score.
     *
     * @param args commandline arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Friederike Moroff, Gwendolyn Gusak");

        if (args.length != 1)
            throw new IOException("Usage: EditDistanceDP fileName");

        String fileName = args[0];
        System.out.println("Currently working on file:" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        FastA fastA = new FastA();
        fastA.read(reader);
        reader.close();

    }
}