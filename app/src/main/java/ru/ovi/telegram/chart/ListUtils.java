package ru.ovi.telegram.chart;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import java.util.List;

public class ListUtils {
    //--
    static double maxInList(List<List<Double>> lines, int fromIndex, int toIndex) {
        return Stream.of(lines)
                .map(l -> max(l, fromIndex, toIndex))
                .reduce(Math::max)
                .orElse(Double.MIN_VALUE);
    }

    static double minInList(List<List<Double>> lines, int fromIndex, int toIndex) {
        return Stream.of(lines)
                .map(l -> min(l, fromIndex, toIndex))
                .reduce(Math::min)
                .orElse(Double.MAX_VALUE);
    }

    //--
    static double max(List<Double> values, int fromIndex, int toIndex) {
        return Stream.of(values.subList(fromIndex, toIndex)).reduce(Math::max).orElse(Double.MIN_VALUE);
    }

    static double min(List<Double> values, int fromIndex, int toIndex) {
        return Stream.of(values.subList(fromIndex, toIndex)).reduce(Math::min).orElse(Double.MAX_VALUE);
    }

    //--
    static <T> int findIndex(List<T> list, Predicate<T> predicate, int defaultValue) {
        for (int i = 0; i < list.size(); i++) {
            final T value = list.get(i);
            if (predicate.test(value)) return i;
        }
        return defaultValue;
    }
}
