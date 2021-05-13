package com.gbi.assignment_3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmithWatermanTest {
    @Test
    public void testAlign() {
        SmithWaterman smithWaterman = new SmithWaterman();

        // Tests
        SmithWaterman.Score score = smithWaterman.align("GGTTGACTA", "TGTTACGG",3, -3,2);
        System.out.println("score:"+score.score);
        System.out.println("i (row):"+score.row);
        System.out.println("j (col):"+score.col);

        String[] trace = smithWaterman.traceBackAndShowAlignment(score);

        // Assertions
        assertEquals(13,score.score);
        assertEquals(7,score.row);
        assertEquals(6,score.col);
    }

    @Test
    public void testAlignMultiple() {
        SmithWaterman smithWaterman = new SmithWaterman();

        // Tests
        SmithWaterman.Score score = smithWaterman.align("ABCDEABCDEABCDE", "ABCD",3, -3,2);
        System.out.println("score:"+score.score);
        System.out.println("i (row):"+score.row);
        System.out.println("j (col):"+score.col);


        String[] trace = smithWaterman.traceBackAndShowAlignment(score);

        // Assertions
        assertEquals(12,score.score);
        assertEquals(4,score.row);
        assertEquals(4,score.col);
    }

    @Test
    public void testAlignEquals() {
        SmithWaterman smithWaterman = new SmithWaterman();

        // Tests
        SmithWaterman.Score score = smithWaterman.align("GGTTGACTA", "GGTTGACTA",3, -3,2);
        System.out.println("score:"+score.score);
        System.out.println("i (row):"+score.row);
        System.out.println("j (col):"+score.col);

        String[] trace = smithWaterman.traceBackAndShowAlignment(score);

        // Assertions
        assertEquals(27,score.score);
        assertEquals(9,score.row);
        assertEquals(9,score.col);
    }

    @Test
    public void testAlignNotEquals() {
        SmithWaterman smithWaterman = new SmithWaterman();

        // Tests
        SmithWaterman.Score score = smithWaterman.align("ACACACAC", "GTGTGTGT",3, -3,2);
        System.out.println("score:"+score.score);
        System.out.println("i (row):"+score.row);
        System.out.println("j (col):"+score.col);

        String[] trace = smithWaterman.traceBackAndShowAlignment(score);

        // Assertions
        assertEquals(0,score.score);
        assertEquals(-1,score.row);
        assertEquals(-1,score.col);
    }

}
