package com.vulpeszerda.mapoverlayview;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vulpes on 2017. 4. 20..
 */

public final class PolylineOptions {
    final List<LatLng> points;
    int color = Color.RED;
    float width = 1.0f;
    boolean visible = true;

    public PolylineOptions() {
        points = new ArrayList<>();
    }

    public PolylineOptions add(LatLng... points) {
        this.points.addAll(Arrays.asList(points));
        return this;
    }

    public PolylineOptions add(LatLng point) {
        this.points.add(point);
        return this;
    }

    public PolylineOptions add(Iterable<LatLng> points) {
        for (LatLng point : points) {
            this.points.add(point);
        }
        return this;
    }

    public PolylineOptions color(int color) {
        this.color = color;
        return this;
    }

    public PolylineOptions visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public PolylineOptions width(float width) {
        this.width = width;
        return this;
    }
}
