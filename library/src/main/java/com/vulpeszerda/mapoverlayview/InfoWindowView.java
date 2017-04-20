package com.vulpeszerda.mapoverlayview;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by vulpes on 2017. 4. 20..
 */

final class InfoWindowView extends FrameLayout {

    private final MarkerView markerView;
    private final View internalView;

    public InfoWindowView(MarkerView markerView, final View internalView) {
        super(markerView.getContext());
        this.internalView = internalView;
        this.markerView = markerView;

        addView(internalView, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setVisibility(View.INVISIBLE);
    }

    public MarkerView getMarkerView() {
        return markerView;
    }

    public View getInternalView() {
        return internalView;
    }

    @Override
    public void setLayoutParams(final ViewGroup.LayoutParams params) {
        updateLayoutParams();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                updateLayoutParams();
            }
        });
    }

    final void updateLayoutParams() {
        MapOverlayLayout.LayoutParams markerParams =
                (MapOverlayLayout.LayoutParams) markerView.getLayoutParams();
        int left = markerParams.leftMargin;
        int top = markerParams.topMargin;
        int w = markerParams.width;
        int h = markerParams.height;

        float anchorTop = markerView.getInfoWindowAnchorTop();
        float anchorLeft = markerView.getInfoWindowAnchorLeft();

        MapOverlayLayout.LayoutParams params = (MapOverlayLayout.LayoutParams) getLayoutParams();

        if (params == null) {
            params = new MapOverlayLayout.LayoutParams(
                    MapOverlayLayout.LayoutParams.WRAP_CONTENT,
                    MapOverlayLayout.LayoutParams.WRAP_CONTENT);
        }

        int selfW = getWidth();
        int selfH = getHeight();
        params.leftMargin = left + Math.round(w * anchorLeft) - Math.round(selfW / 2f);
        params.topMargin = top + Math.round(h * anchorTop) - selfH;
        super.setLayoutParams(params);

        if (selfH > 0 && selfW > 0) {
            setVisibility(View.VISIBLE);
        }
    }

    final OnLayoutChangeListener onMarkerLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v,
                                   int left,
                                   int top,
                                   int right,
                                   int bottom,
                                   int oldLeft,
                                   int oldTop,
                                   int oldRight,
                                   int oldBottom) {
            updateLayoutParams();
        }
    };
}
