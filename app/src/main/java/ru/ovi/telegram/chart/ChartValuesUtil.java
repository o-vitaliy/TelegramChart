package ru.ovi.telegram.chart;

import android.graphics.Color;
import com.annimon.stream.Stream;
import ru.ovi.telegram.chart.data.GraphLine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public class ChartValuesUtil {

    private ChartValuesUtil() {
    }

    static double maxInLines(List<GraphLine> lines) {
        if (lines.size() > 0) {
            return maxInLines(lines, 0, lines.get(0).getValues().size());
        }
        return Double.MIN_VALUE;
    }

    static double minInLines(List<GraphLine> lines) {
        if (lines.size() > 0) {
            return minInLines(lines, 0, lines.get(0).getValues().size());
        }
        return Double.MAX_VALUE;
    }

    static double maxInLines(List<GraphLine> lines, int fromIndex, int toIndex) {
        return Stream.of(lines)
                .map(line -> max(line.getValues(), fromIndex, toIndex))
                .reduce(Math::max)
                .orElse(Double.MIN_VALUE);
    }

    static double minInLines(List<GraphLine> lines, int fromIndex, int toIndex) {
        return Stream.of(lines)
                .map(line -> min(line.getValues(), fromIndex, toIndex))
                .reduce(Math::min)
                .orElse(Double.MAX_VALUE);
    }


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

    static float[] colorToFloatArray(int color) {
        //RGBA
        return new float[]{
                (float) Color.red(color) / (float) (0xFF),
                (float) Color.green(color) / (float) (0xFF),
                (float) Color.blue(color) / (float) (0xFF),
                (float) Color.alpha(color) / (float) (0xFF),
        };
    }


    static FloatBuffer coordinatesToBuffer(float[] coordinates) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                coordinates.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);

        return vertexBuffer;
    }

    static Touchable findTouched(List<?> items, float x, float y) {
        return Stream.of(items)
                .filter(e -> e instanceof Touchable)
                .map(e -> (Touchable) e)
                .map(t -> t.onTouched(x, y))
                .withoutNulls()
                .findFirst().orElse(null);
    }
}
