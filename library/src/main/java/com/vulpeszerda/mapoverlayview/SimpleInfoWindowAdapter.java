package com.vulpeszerda.mapoverlayview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by vulpes on 2017. 4. 20..
 */
@SuppressLint("InflateParams")
final class SimpleInfoWindowAdapter implements InfoWindowAdapter {

    interface Delegate {
        View getExternalInfoContents(MarkerView markerView);
    }

    private final LayoutInflater inflater;
    private final Delegate delegate;

    public SimpleInfoWindowAdapter(Context context, Delegate delegate) {
        this.inflater = LayoutInflater.from(context);
        this.delegate = delegate;
    }

    @Override
    public View getInfoWindow(MarkerView markerView) {
        View contents = delegate.getExternalInfoContents(markerView);
        if (contents == null) {
            contents = getInfoContents(markerView);
        }
        if (contents == null) {
            return null;
        }
        View window = inflater.inflate(R.layout.mo__info_window, null, false);
        FrameLayout container = (FrameLayout) window.findViewById(R.id.container);
        container.addView(contents, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        container.setLayoutParams(new MapOverlayLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return window;
    }

    @Override
    public View getInfoContents(MarkerView markerView) {
        String title = markerView.getTitle();
        String snippet = markerView.getSnippet();
        if (title == null && snippet == null) {
            return null;
        }
        View contents = inflater.inflate(R.layout.mo__info_window_contents, null, false);
        TextView titleView = (TextView) contents.findViewById(R.id.title);
        TextView descView = (TextView) contents.findViewById(R.id.description);
        titleView.setText(title);
        descView.setText(snippet);

        titleView.setVisibility(CommonUtils.isNullOrEmpty(title) ? View.GONE : View.VISIBLE);
        descView.setVisibility(CommonUtils.isNullOrEmpty(snippet) ? View.GONE : View.VISIBLE);
        return contents;
    }
}
