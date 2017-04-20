package com.vulpeszerda.mapoverlayview;

import android.view.View;

/**
 * Created by vulpes on 2017. 4. 20..
 */

public interface InfoWindowAdapter {
    View getInfoWindow(MarkerView markerView);

    View getInfoContents(MarkerView markerView);
}
