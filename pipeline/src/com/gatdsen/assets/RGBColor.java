package com.gatdsen.assets;

import java.util.Arrays;

/**
 * A simple class to store rgb triples
 */
public class RGBColor {
    public int[] rgb;

    public RGBColor(int[] rgb) {
        this.rgb = rgb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RGBColor rgbColor = (RGBColor) o;
        return Arrays.equals(rgb, rgbColor.rgb);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(rgb);
    }

    @Override
    public String toString() {
        return "RGBColor{" +
                Arrays.toString(rgb) +
                '}';
    }
}
