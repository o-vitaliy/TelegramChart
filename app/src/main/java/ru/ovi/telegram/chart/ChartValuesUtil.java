package ru.ovi.telegram.chart;

import com.annimon.stream.Stream;
import ru.ovi.telegram.chart.data.GraphLine;

import java.util.List;

public class ChartValuesUtil {

    static double maxInLines(List<GraphLine> lines) {
        return Stream.of(lines).map(line -> max(line.getValues())).reduce(Math::max).orElse(Double.MIN_VALUE);
    }

    static double minInLine(List<GraphLine> lines) {
        return Stream.of(lines).map(line -> min(line.getValues())).reduce(Math::min).orElse(Double.MAX_VALUE);
    }

    static double max(List<Double> values) {
        return Stream.of(values).reduce(Math::max).orElse(Double.MIN_VALUE);
    }

    static double min(List<Double> values) {
        return Stream.of(values).reduce(Math::min).orElse(Double.MAX_VALUE);
    }


}
