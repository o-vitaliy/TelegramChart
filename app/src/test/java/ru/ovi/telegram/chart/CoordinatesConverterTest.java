package ru.ovi.telegram.chart;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoordinatesConverterTest {
    private static final double DELTA = .1;
    private final CoordinatesConverter converter = new CoordinatesConverter();


    @Before
    public void setUp() {
        converter.setNewSizes(100, 100);
    }

    @Test
    public void horizontalValueToRelative1() {
        assertEquals(-1, converter.verticalValueToRelative(0), DELTA);
    }

    @Test
    public void horizontalValueToRelative2() {
        assertEquals(0, converter.verticalValueToRelative(50), DELTA);
    }

    @Test
    public void horizontalValueToRelative3() {
        assertEquals(1, converter.verticalValueToRelative(100), DELTA);
    }

    @Test
    public void verticalValueToRelative1() {
        assertEquals(-1, converter.verticalValueToRelative(0), DELTA);
    }

    @Test
    public void verticalValueToRelative2() {
        assertEquals(0, converter.verticalValueToRelative(50), DELTA);
    }

    @Test
    public void verticalValueToRelative3() {
        assertEquals(1, converter.verticalValueToRelative(100), DELTA);
    }
}