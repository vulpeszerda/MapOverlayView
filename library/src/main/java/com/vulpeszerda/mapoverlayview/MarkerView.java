package com.vulpeszerda.mapoverlayview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

public class MarkerView extends View {

    interface InternalMarkerListener {
        void onLocationInvalidated(MarkerView markerView);
        void onMarkerClicked(MarkerView markerView);
    }

    private final Point coordinate;
    private LatLng latLng;
    private float anchorTop;
    private float anchorLeft;
    private float infoWindowAnchorTop;
    private float infoWindowAnchorLeft;
    private Drawable icon;
    private Rect bounds;
    private String title;
    private String snippet;
    private boolean externalClickable;
    private OnClickListener externalClickListener;

    private InternalMarkerListener internalListener;

    public MarkerView(Context context, MarkerOptions options) {
        super(context);
        this.coordinate = new Point(0, 0);
        this.latLng = options.position;
        this.icon = options.icon;
        this.anchorTop = options.anchorTop;
        this.anchorLeft = options.anchorLeft;
        this.infoWindowAnchorTop = options.infoWindowAnchorTop;
        this.infoWindowAnchorLeft = options.infoWindowAnchorLeft;
        this.title = options.title;
        this.snippet = options.snippet;

        if (icon == null) {
            icon = ContextCompat.getDrawable(context, R.drawable.mo__marker);
        }

        bounds = new Rect(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        icon.setBounds(bounds);
        setVisibility(options.visible ? View.VISIBLE : View.GONE);

        super.setClickable(true);
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (internalListener != null) {
                    internalListener.onMarkerClicked(MarkerView.this);
                }
                if (externalClickable && externalClickListener != null) {
                    externalClickListener.onClick(v);
                }
            }
        });
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(@NonNull LatLng latLng) {
        this.latLng = latLng;
        if (internalListener != null) {
            internalListener.onLocationInvalidated(this);
        }
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
        bounds = new Rect(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        updateLayoutParams();
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setAnchor(float top, float left) {
        this.anchorTop = top;
        this.anchorLeft = left;
        updateLayoutParams();
    }

    public float getAnchorTop() {
        return anchorTop;
    }

    public float getAnchorLeft() {
        return anchorLeft;
    }

    public void setInfoWindowAnchor(float top, float left) {
        this.infoWindowAnchorTop = top;
        this.infoWindowAnchorLeft = left;
    }

    public float getInfoWindowAnchorTop() {
        return infoWindowAnchorTop;
    }

    public float getInfoWindowAnchorLeft() {
        return infoWindowAnchorLeft;
    }

    @Override
    public void setClickable(boolean clickable) {
        externalClickable = clickable;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        externalClickListener = listener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSnippet() {
        return snippet;
    }

    final void setInternalMarkerListener(@Nullable InternalMarkerListener internalListener) {
        this.internalListener = internalListener;
    }

    final void onProjectionChanged(Projection projection) {
        Point point = projection.toScreenLocation(latLng);
        if (point.x != this.coordinate.x || point.y != this.coordinate.y) {
            this.coordinate.set(point.x, point.y);
            updateLayoutParams();
        }
    }

    final Point getCoordinate() {
        return coordinate;
    }

    @Override
    public void setLayoutParams(final ViewGroup.LayoutParams params) {
        updateLayoutParams();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        icon.draw(canvas);
        canvas.save();

        canvas.restore();
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void updateLayoutParams() {
        MapOverlayLayout.LayoutParams params = (MapOverlayLayout.LayoutParams) getLayoutParams();
        if (params == null) {
            params = new MapOverlayLayout.LayoutParams(bounds.width(), bounds.height());
        } else {
            params.width = bounds.width();
            params.height = bounds.height();
        }
        params.leftMargin = coordinate.x - Math.round(params.width * anchorLeft);
        params.topMargin = coordinate.y - Math.round(params.height * anchorTop);
        super.setLayoutParams(params);
    }
}
