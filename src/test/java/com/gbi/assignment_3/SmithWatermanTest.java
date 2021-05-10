package com.gbi.assignment_3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmithWatermanTest {
    @Test
    public void testAlign() {
        SmithWaterman smithWaterman = new SmithWaterman();
        SmithWaterman.Score score = smithWaterman.align("GGTTGACTA", "TGTTACGG",3, -3,2);
        System.out.println("score:"+score.score);
        System.out.println("i (row):"+score.row);
        System.out.println("j (col):"+score.col);
        assertEquals(13,score.score);
        assertEquals(7,score.row);
        assertEquals(6,score.col);

        System.out.println("Scores:");
        smithWaterman.printScoreMatrix();
        System.out.println("Traces:");
        smithWaterman.printTraceMatrix();

        String trace = smithWaterman.traceBackAndShowAlignment(score);
        assertEquals("GTT-AC:GTTGAC",trace);
    }
}
