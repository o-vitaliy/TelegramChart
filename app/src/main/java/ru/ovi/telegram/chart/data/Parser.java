package ru.ovi.telegram.chart.data;

import android.graphics.Color;
import com.annimon.stream.Stream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Parser {
    private final String AXIS_X = "x";
    private final String TYPES = "types";
    private final String NAMES = "names";
    private final String COLORS = "colors";
    private final String COLUMNS = "columns";

    public ChartData[] parse(String json) throws JSONException {
        final List<ChartData> chartDataList = new ArrayList<>();
        final JSONArray rootJsonChartArray = new JSONArray(json);
        for (int rootJsonChartIndex = 0; rootJsonChartIndex < rootJsonChartArray.length(); rootJsonChartIndex++) {
            final JSONObject chartJson = rootJsonChartArray.getJSONObject(rootJsonChartIndex);

            final HashMap<String, String> columnsTypes = new HashMap<>();
            final HashMap<String, String> columnsNames = new HashMap<>();
            final HashMap<String, Integer> columnsColors = new HashMap<>();
            final HashMap<String, double[]> columnsValues = new HashMap<>();

            final JSONObject typesJson = chartJson.getJSONObject(TYPES);
            final Iterator<String> typeKeys = typesJson.keys();
            while (typeKeys.hasNext()) {
                final String type = typeKeys.next();
                columnsTypes.put(type, typesJson.getString(type));
            }

            final JSONObject namesJson = chartJson.getJSONObject(NAMES);
            final Iterator<String> nameKeys = namesJson.keys();
            while (nameKeys.hasNext()) {
                final String type = nameKeys.next();
                columnsNames.put(type, namesJson.getString(type));
            }

            final JSONObject colorsJson = chartJson.getJSONObject(COLORS);
            final Iterator<String> colorKeys = colorsJson.keys();
            while (colorKeys.hasNext()) {
                final String type = colorKeys.next();
                columnsColors.put(type, Color.parseColor(colorsJson.getString(type)));
            }

            final JSONArray columnsJson = chartJson.getJSONArray(COLUMNS);
            for (int columnIndex = 0; columnIndex < columnsJson.length(); columnIndex++) {
                final JSONArray columnValues = columnsJson.getJSONArray(columnIndex);

                final String key = columnValues.getString(0);
                final double values[] = new double[columnValues.length() - 1];

                for (int columnValueIndex = 1; columnValueIndex < columnValues.length(); columnValueIndex++) {
                    values[columnValueIndex - 1] = columnValues.getDouble(columnValueIndex);
                }

                columnsValues.put(key, values);
            }

            final AtomicReference<Line> abscissa = new AtomicReference<>();
            final List<Line> ordinates = new ArrayList<>();

            Stream.of(columnsTypes).forEach(stringStringEntry -> {
                final String column = stringStringEntry.getKey();
                final String columnType = stringStringEntry.getValue();

                final Line line = new Line(
                        columnsColors.containsKey(column) ? columnsColors.get(column) : 0,
                        columnsNames.get(column),
                        columnsValues.get(column)
                );

                if (columnType.equals(AXIS_X)) {
                    abscissa.set(line);
                } else {
                    ordinates.add(line);
                }
            });

            chartDataList.add(
                    new ChartData(abscissa.get(), ordinates.toArray(new Line[0]))
            );

        }

        return chartDataList.toArray(new ChartData[0]);
    }

}
