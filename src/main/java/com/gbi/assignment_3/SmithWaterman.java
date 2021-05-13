package com.gbi.assignment_3;

import com.gbi.FastA;

import java.io.*;

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
    int matchCount = 0;
    int mismatchCount = 0;
    int gapCountX = 0;
    int gapCountY = 0;
    int firstMatchX = 0;
    int firstMatchY = 0;

    /**
     * Result structure of alignment
     */
    public static class Score {
        /**
         * row of highest score.
         */
        public int row = -1;
        /**
         * column of highest score.
         */
        public int col = -1;
        /**
         * highest score
         */
        public int score = 0;
    }

    /**
     * Trace directions
     */
    public enum TbDirection {
        Diagonal("D"),
        Left("L"),
        Above("A"),
        Undefined("x");

        private final String direction;

        TbDirection(String direction) {
            this.direction = direction;
        }

        public String toString() {
            return direction;
        }
    }

    /**
     * Calculate maximum score value from diagonal, left  or above
     * @param diagonal (i-1,j-1)
     * @param left (i,j-1)
     * @param above (i-1,j)
     * @return
     */
    private int max(int diagonal, int left, int above) {
        return Math.max(0, (Math.max(Math.max(diagonal, left), above)));
    }

    /**
     * Determine direction causing the maximum score, see {@link #max(int, int, int)}
     * @param diagonal
     * @param above
     * @param left
     * @return
     */
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
     * @param x             first sequence (vertical, mapped to rows in matrix)
     * @param y             second sequence (horizontal, mapped to columns in matrix)
     * @param matchScore    match score
     * @param mismatchScore mismatch score
     * @param gapPenalty    gap penalty
     * @return edit distance
     */
    public Score align(String x, String y, int matchScore, int mismatchScore, int gapPenalty) {
        this.x = x;
        this.y = y;
        Score score = new Score();

        // Matrix [rows][columns]
        edMatrix = new int[x.length() + 1][y.length() + 1];
        tbMatrix = new TbDirection[x.length() + 1][y.length() + 1];

        //Initialisation
        for (int i = 0; i <= x.length(); ++i) {
            edMatrix[i][0] = 0;
            tbMatrix[i][0] = TbDirection.Undefined;
        }
        for (int j = 0; j <= y.length(); ++j) {
            edMatrix[0][j] = 0;
            tbMatrix[0][j] = TbDirection.Undefined;
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
                    score.row = i;
                    score.col = j;
                }
            }
        }

        //Return optimal score for alignment (edit distance)
        return score;
    }

    /**
     * perform traceback and print an optimal alignment to  the console (standard output)
     */
    public String[] traceBackAndShowAlignment(Score score) {
        int diagonalCondition;
        StringBuilder xAligned = new StringBuilder();
        StringBuilder yAligned = new StringBuilder();

        //Traceback through edit distance matrix & alignment of chars for each sequence
        for (int i = score.row, j = score.col; i > 0 && j > 0; ) {
            if (edMatrix[i][j] == 0) {
                break;
            }
            switch (tbMatrix[i][j]) {
                case Diagonal:
                    xAligned.insert(0,x.charAt(i-1));
                    yAligned.insert(0,y.charAt(j-1));
                    if (x.charAt(i - 1) == y.charAt(j - 1)) {
                        ++matchCount;
                    }
                    else {
                        ++mismatchCount;
                    }
                    --i;
                    --j;
                    break;
                case Left:
                    xAligned.insert(0,"-");
                    yAligned.insert(0,y.charAt(j-1));
                    --j;
                    ++gapCountX;
                    break;
                case Above:
                    xAligned.insert(0,x.charAt(i-1));
                    yAligned.insert(0,"-");
                    --i;
                    ++gapCountY;
                    break;
                case Undefined:
                    xAligned.insert(0,"@");
                    yAligned.insert(0,"@");
            }
        }
        firstMatchX = score.row - (matchCount + mismatchCount + gapCountY);
        firstMatchY = score.col - (matchCount + mismatchCount + gapCountX);


        // Print optimal alignment of x and y to console in correct order
        System.out.println(xAligned.toString());
        System.out.println(yAligned.toString());
        return new String[] {
                xAligned.toString(), yAligned.toString()
        };
    }

    /**
     * main program: reads two sequences in fastA format and computes their optimal alignment score.
     *
     * @param args commandline arguments
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Friederike Moroff, Gwendolyn Gusak");

        int matchScore = 3;
        int mismatchScore = -3;
        int gapPenalty = 5;

        if (args.length != 5)
            throw new IOException("Usage: SmithWatermanDP inputFileName matchScore mismatchScore gapPenalty outputFileName");

        if (args.length == 4) {
            matchScore = Integer.parseInt(args[1]);
            mismatchScore = Integer.parseInt(args[2]);
            gapPenalty = Integer.parseInt(args[3]);
        }

        String fileName = args[0];
        System.out.println("Currently working on file:" + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        FastA fastA = new FastA();
        fastA.read(reader);
        reader.close();

        SmithWaterman smithWaterman = new SmithWaterman();
        String sequence1 = fastA.getSequence(0);
        String sequence2 = fastA.getSequence(1);
        Score score = smithWaterman.align(sequence1, fastA.getSequence(1),matchScore,mismatchScore,gapPenalty);

        String[] alignedSeq = smithWaterman.traceBackAndShowAlignment(score);
        System.out.println("The optimal alignment score is "+ score.score);

        Writer writer = new FileWriter(args[4]);

        writer.append("match score: " + matchScore + ", mismatch score: " + mismatchScore + ", gap penalty: " + gapPenalty).append('\n');

        writer.append("match count: " + smithWaterman.matchCount).//
                append(", mismatch count: " + smithWaterman.mismatchCount).//
                append(", gap count: " + (smithWaterman.gapCountX+ smithWaterman.gapCountY)).append('\n');
        writer.append("Alignment in Sequence 1 starts at: " + smithWaterman.firstMatchX).//
                append( " with length: " + (score.row - smithWaterman.firstMatchX)).//
                append(" and gap count: " + smithWaterman.gapCountX).append('\n');
        writer.append("Alignment in Sequence 2 starts at: " + smithWaterman.firstMatchY).//
                append( " with length: " + (score.col - smithWaterman.firstMatchY)).//
                append(" and gap count: " + smithWaterman.gapCountY).append('\n');

        for (int i = 0; i < smithWaterman.firstMatchY; i++) {
            writer.append(" ");
        }
        for (int i = 0; i < smithWaterman.firstMatchX; i++) {
            writer.append(sequence1.charAt(i));
        }
        writer.append(alignedSeq[0]);
        for (int i = score.row; i < sequence1.length(); i++) {
            writer.append(sequence1.charAt(i));
        }
        writer.append('\n');

        for (int i = 0; i < Math.max(smithWaterman.firstMatchX, smithWaterman.firstMatchY); i++) {
            writer.append(" ");
        }
        for (int i = 0; i < alignedSeq[0].length(); i++) {
            if (alignedSeq[0].charAt(i) == alignedSeq[1].charAt(i)) {
                writer.append('|');
            }
            else if (alignedSeq[0].charAt(i) != '-' && alignedSeq[1].charAt(i) != '-') {
                writer.append('.');
            }
            else {
                writer.append(' ');
            }
        }
        writer.append('\n');

        for (int i = 0; i < smithWaterman.firstMatchX; i++) {
            writer.append(" ");
        }
        for (int i = 0; i < smithWaterman.firstMatchY; i++) {
            writer.append(sequence2.charAt(i));
        }
        writer.append(alignedSeq[1]);
        for (int i = score.row; i < sequence2.length(); i++) {
            writer.append(sequence2.charAt(i));
        }

        writer.close();

    }
}