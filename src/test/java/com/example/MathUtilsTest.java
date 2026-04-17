package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MathUtilsTest {

    MathUtils math = new MathUtils();

    @Test
    void testAdd() {
        assertEquals(5, math.add(2, 3));
    }

    @Test
    void testSubtract() {
        assertEquals(2, math.subtract(5, 3));
    }

    @Test
    void testMultiply() {
        assertEquals(6, math.multiply(2, 3));
    }

    @Test
    void testDivide() {
        assertEquals(2.0, math.divide(6, 3));
    }

    @Test
    void testDivideByZero() {
        assertEquals(-1.0, math.divide(6, 0));
    }
}