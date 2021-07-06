package com.owu.geekhub.dummy;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MathDummyTest {

    @Test
    void testAdd() {
        MathDummy mathDummy = new MathDummy();
        int expected = 11;
        int actual = mathDummy.add(5, 6);
        assertEquals(expected, actual);
    }

}