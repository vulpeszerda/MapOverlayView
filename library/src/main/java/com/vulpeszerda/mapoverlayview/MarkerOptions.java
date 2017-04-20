package com.vulpeszerda.mapoverlayview;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vulpes on 2017. 4. 20..
 */

public final class MarkerOptions {
    final LatLng position;
    Drawable icon;
    float anchorTop = 1.0f;
    float anchorLeft = 0.5f;
    float infoWindowAnchorTop = 0.0f;
    float infoWindowAnchorLeft = 0.5f;
    String title;
    String snippet;
    boolean visible = true;

    public MarkerOptions(LatLng position) {
        this.position = position;
    }

    public MarkerOptions icon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public MarkerOptions anchor(float top, float left) {
        anchorTop = top;
        anchorLeft = left;
        return this;
    }

    public MarkerOptions infowWindowAnchor(float top, float left) {
        infoWindowAnchorTop = top;
        infoWindowAnchorLeft = left;
        return this;
    }

    public MarkerOptions visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public MarkerOptions title(String title) {
        this.title = title;
        return this;
    }

    public MarkerOptions snippet(String snippet) {
        this.snippet = snippet;
        return this;
    }
}
