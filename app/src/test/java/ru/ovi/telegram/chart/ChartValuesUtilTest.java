package ru.ovi.telegram.chart;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ChartValuesUtilTest {
    private final double MAX_VALUE = 100;
    private final double MAX_VALUE_2 = 50;
    private final double MIN_VALUE = -100;
    private final double MIN_VALUE_2 = -50;
    private final List<Double> list1 = Arrays.asList(0.0, 1.0, 2.0, MAX_VALUE_2, MIN_VALUE_2, MAX_VALUE);
    private final List<Double> list2 = Arrays.asList(1.0, 2.0, 3.0, 4.0, MIN_VALUE_2, MIN_VALUE);
    private final List<List<Double>> list3 = Arrays.asList(list1, list2);

    @Test
    public void maxInList() {
        assertEquals(MAX_VALUE, ChartValuesUtil.maxInList(list3, 0, list1.size()), .1);
    }

    @Test
    public void maxInList2() {
        assertEquals(MAX_VALUE_2, ChartValuesUtil.maxInList(list3, 0, list1.size() - 1), .1);
    }

    @Test
    public void minInList() {
        assertEquals(MIN_VALUE, ChartValuesUtil.minInList(list3, 0, list1.size()), .1);
    }

    @Test
    public void minInList2() {
        assertEquals(MIN_VALUE_2, ChartValuesUtil.minInList(list3, 0, list2.size() - 1), .1);
    }

    @Test
    public void max() {
        assertEquals(MAX_VALUE, ChartValuesUtil.max(list1, 0, list1.size()), .1);
    }

    @Test
    public void max2() {
        assertEquals(MAX_VALUE_2, ChartValuesUtil.max(list1, 0, list1.size() - 1), .1);
    }

    @Test
    public void min() {
        assertEquals(MIN_VALUE, ChartValuesUtil.min(list2, 0, list1.size()), .1);
    }

    @Test
    public void min2() {
        assertEquals(MIN_VALUE_2, ChartValuesUtil.min(list2, 0, list2.size() - 1), .1);
    }
}