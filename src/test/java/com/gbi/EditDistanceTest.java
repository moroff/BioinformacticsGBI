package com.gbi;

import static org.junit.jupiter.api.Assertions.*;

class EditDistanceTest {

    @org.junit.jupiter.api.Test
    void align() {
        EditDistance classUnderTest = new EditDistance();
        int d = classUnderTest.align("TOR","TIER");
        assertEquals(2,d);
    }
}