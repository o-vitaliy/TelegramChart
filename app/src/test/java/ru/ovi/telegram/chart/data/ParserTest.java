package ru.ovi.telegram.chart.data;

import android.graphics.Color;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ParserTest {

    @Test
    public void parse() throws JSONException, IOException {
        final AssetsFileReader assetsFileReader = new AssetsFileReader();
        final String result = assetsFileReader.parse(RuntimeEnvironment.application, "chart_data.json");

        final Parser parser = new Parser();


        final List<ChartData> charts = parser.parse(result);
        assertEquals(5, charts.size());

        final ChartData chart = charts.get(0);
        assertEquals(112, chart.getAbscissa().getValues().size());
        assertEquals(2, chart.getOrdinates().size());
        assertEquals(112, chart.getOrdinates().get(0).getValues().size());
        assertEquals("#0", chart.getOrdinates().get(0).getName());
        assertEquals(Color.parseColor("#3DC23F"), chart.getOrdinates().get(0).getColor());
        assertEquals(112, chart.getOrdinates().get(1).getValues().size());
        assertEquals("#1", chart.getOrdinates().get(1).getName());
        assertEquals(Color.parseColor("#F34C44"), chart.getOrdinates().get(1).getColor());
    }
}