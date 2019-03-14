package ru.ovi.telegram.chart;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ChartValuesUtilTest {
    private final double MAX_VALUE = 100;
    private final double MIN_VALUE = -100;
    private final List<Double> list1 = Arrays.asList(0.0, 1.0, 2.0, MIN_VALUE, MAX_VALUE);
    private final List<Double> list2 = Arrays.asList(1.0, 2.0, MIN_VALUE);
    private final List<List<Double>> list3 = Arrays.asList(list1, list2);

    @Test
    public void maxInList() {
        assertEquals(MAX_VALUE, ChartValuesUtil.maxInList(list3), .1);
    }

    @Test
    public void minInList() {
        assertEquals(MIN_VALUE, ChartValuesUtil.minInList(list3), .1);
    }

    @Test
    public void max() {
        assertEquals(MAX_VALUE, ChartValuesUtil.max(list1), .1);
    }

    @Test
    public void min() {
        assertEquals(MIN_VALUE, ChartValuesUtil.min(list1), .1);
    }
}