package ru.ovi.telegram.chart;

public class CoordinatesConverter {

    private float width;
    private float height;

    public void setNewSizes(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float horizontalValueToRelative(float value) {
        return value / width * 2 - 1;
    }

    public float verticalValueToRelative(float value) {
        return value / height * 2 - 1;
    }

}