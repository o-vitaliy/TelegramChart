package ru.ovi.telegram.chart;

import android.graphics.Color;
import com.annimon.stream.Stream;
import ru.ovi.telegram.chart.data.GraphLine;

import java.util.List;

public class ChartValuesUtil {

    private ChartValuesUtil(){}

    static double maxInLines(List<GraphLine> lines) {
        return Stream.of(lines).map(line -> max(line.getValues())).reduce(Math::max).orElse(Double.MIN_VALUE);
    }

    static double minInLines(List<GraphLine> lines) {
        return Stream.of(lines).map(line -> min(line.getValues())).reduce(Math::min).orElse(Double.MAX_VALUE);
    }

    static double maxInList(List<List<Double>> lines) {
        return Stream.of(lines).map(ChartValuesUtil::max).reduce(Math::max).orElse(Double.MIN_VALUE);
    }

    static double minInList(List<List<Double>> lines) {
        return Stream.of(lines).map(ChartValuesUtil::min).reduce(Math::min).orElse(Double.MAX_VALUE);
    }

    static double max(List<Double> values) {
        return Stream.of(values).reduce(Math::max).orElse(Double.MIN_VALUE);
    }

    static double min(List<Double> values) {
        return Stream.of(values).reduce(Math::min).orElse(Double.MAX_VALUE);
    }

    static float[] colorToFloatArray(int color) {
        //RGBA
        return new float[]{
                (float) Color.red(color) / (float) (0xFF),
                (float) Color.green(color) / (float) (0xFF),
                (float) Color.blue(color) / (float) (0xFF),
                (float) Color.alpha(color) / (float) (0xFF),
        };
    }


}
