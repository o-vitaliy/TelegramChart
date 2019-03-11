package ru.ovi.telegram.chart.data;

import android.content.Context;

import java.io.IOException;
import java.util.Scanner;

public class AssetsFileReader {

    public String parse(final Context context, final String fileName) throws IOException {
        final Scanner s = new Scanner(context.getAssets().open(fileName)).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
