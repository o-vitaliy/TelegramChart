package ru.ovi.telegram.chart.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class AssetsFileReaderTest {

    @Test
    public void parse() throws IOException, JSONException {
        final AssetsFileReader assetsFileReader = new AssetsFileReader();
        final String result = assetsFileReader.parse(RuntimeEnvironment.application, "chart_data.json");
        assertNotNull(result);
        assertNotNull(new JSONArray(result));

    }
}