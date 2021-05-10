package com.gbi.assignment_3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SmithWatermanTest {
    @Test
    public void testAlign() {
        SmithWaterman smithWaterman = new SmithWaterman();
        SmithWaterman.Score score = smithWaterman.align("GGTTGACTA", "TGTTACGG",3, -3,2);
        System.out.println("score:"+score.score);
        System.out.println("i:"+score.i);
        System.out.println("j:"+score.j);
        assertEquals(13,score.score);
        assertEquals(7,score.i);
        assertEquals(6,score.j);
        String trace = smithWaterman.traceBackAndShowAlignment(score);
        assertEquals("GTTGAC:GTT-AC",trace);
    }
}
