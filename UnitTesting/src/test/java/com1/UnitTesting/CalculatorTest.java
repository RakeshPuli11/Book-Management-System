package com1.UnitTesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    Calculator calc;
    @BeforeEach
    void setUp(){
        calc = new Calculator();
    }

    @Test
    public void testMul(){
        assertEquals(21,calc.mutiply(3,7));
    }
    @Test
    public void tadd(){
        assertEquals(7,calc.add(3,4));
    }
    @Test
    public void tsub(){
        assertEquals(0,calc.sub(4,4));
    }
    @Test
    public void tdiv(){
        assertEquals(0,calc.div(3,4));
    }
}
