package com.vulpeszerda.mapoverlayview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vulpes on 2017. 4. 19..
 */

public class MapOverlayLayout extends FrameLayout
        implements GoogleMap.OnCameraIdleListener,
                   GoogleMap.OnCameraMoveListener,
                   GoogleMap.OnCameraMoveStartedListener,
                   GoogleMap.OnCameraMoveCanceledListener,
                   MarkerView.InternalMarkerListener,
                   SimpleInfoWindowAdapter.Delegate {

    public interface OnMarkerClickedListener {
        void onMarkerClicked(MarkerView markerView);
    }

    public interface OnInfoWindowClickedListener {
        void onInfoWindowClickedListener(InfoWindowView infoWindowView);
    }

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final InfoWindowAdapter simpleInfoWindowAdapter;
    private InfoWindowAdapter infoWindowAdapter;
    private List<MarkerView> markersList;
    private Map<MarkerView, InfoWindowView> infoWindowMap;
    private GoogleMap googleMap;
    private Timer timer;

    private OnMarkerClickedListener markerClickedListener;
    private OnInfoWindowClickedListener infoWindowClickedListener;

    public MapOverlayLayout(final Context context) {
        this(context, null);
    }

    public MapOverlayLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        markersList = new ArrayList<>();
        infoWindowMap = new HashMap<>();
        simpleInfoWindowAdapter = new SimpleInfoWindowAdapter(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setupMap(final GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void refresh() {
        if (googleMap != null) {
            Projection projection = googleMap.getProjection();
            MarkerView markerView;
            for (int i = 0; i < markersList.size(); i++) {
                markerView = markersList.get(i);
                markerView.onProjectionChanged(projection);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        refresh();
    }

    @Override
    public void onCameraIdle() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onCameraMoveCanceled() {
    }

    @Override
    public void onCameraMove() {
//        updateLayoutParams();
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                });
            }
        }, 0, 2);
    }

    public MarkerView addMarker(MarkerOptions markerOptions) {
        MarkerView markerView = new MarkerView(getContext(), markerOptions);
        addMarker(markerView);
        return markerView;
    }

    public void addMarker(MarkerView view) {
        if (googleMap == null) {
            throw new UnsupportedOperationException(
                    "'addMarker' should be called after GoogleMap being setup! Call 'setupMap' first.");
        }
        markersList.add(view);
        view.onProjectionChanged(googleMap.getProjection());
        addView(view);
        view.setInternalMarkerListener(this);
    }

    public void removeMarker(MarkerView view) {
        markersList.remove(view);
        removeView(view);
        InfoWindowView infoWindowView = infoWindowMap.remove(view);
        if (infoWindowView != null) {
            removeView(infoWindowView);
        }
        view.setInternalMarkerListener(null);
    }

    public void setInfoWindowAdapter(InfoWindowAdapter infoWindowAdapter) {
        this.infoWindowAdapter = infoWindowAdapter;
    }

    public InfoWindowAdapter getInfoWindowAdapter() {
        return infoWindowAdapter == null ? simpleInfoWindowAdapter : infoWindowAdapter;
    }

    public void setOnMarkerClickedListener(OnMarkerClickedListener listener) {
        markerClickedListener = listener;
    }

    public void setOnInfoWindowClickedListener(OnInfoWindowClickedListener listener) {
        infoWindowClickedListener = listener;
    }

    @Override
    public void onLocationInvalidated(MarkerView view) {
        Projection projection = googleMap.getProjection();
        view.onProjectionChanged(projection);
    }

    @Override
    public void onMarkerClicked(MarkerView markerView) {
        InfoWindowView infoWindowView = infoWindowMap.get(markerView);
        if (infoWindowView != null) {
            hideInfoWindow(markerView);
        } else {
            showInfoWindow(markerView);
        }
        if (markerClickedListener != null) {
            markerClickedListener.onMarkerClicked(markerView);
        }
    }

    protected void showInfoWindow(MarkerView markerView) {
        InfoWindowView prevInfoWindow = infoWindowMap.get(markerView);
        if (prevInfoWindow != null) {
            return;
        }
        View internalView = null;
        if (infoWindowAdapter != null) {
            internalView = infoWindowAdapter.getInfoWindow(markerView);
        }
        if (internalView == null) {
            internalView = simpleInfoWindowAdapter.getInfoWindow(markerView);
        }
        if (internalView == null) {
            return;
        }
        final InfoWindowView infoWindowView = new InfoWindowView(markerView, internalView);
        infoWindowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoWindowClickedListener != null) {
                    infoWindowClickedListener.onInfoWindowClickedListener(infoWindowView);
                }
            }
        });
        infoWindowMap.put(markerView, infoWindowView);
        addView(infoWindowView);
        markerView.addOnLayoutChangeListener(infoWindowView.onMarkerLayoutChangeListener);
    }

    protected void hideInfoWindow(MarkerView markerView) {
        InfoWindowView infoWindowView = infoWindowMap.get(markerView);
        if (infoWindowView != null) {
            infoWindowMap.remove(markerView);
            removeView(infoWindowView);
            markerView.removeOnLayoutChangeListener(infoWindowView.onMarkerLayoutChangeListener);
        }
    }

    protected void hideAllInfoWindows() {
        for (InfoWindowView infoWindowView : infoWindowMap.values()) {
            removeView(infoWindowView);
            infoWindowView.getMarkerView().removeOnLayoutChangeListener(
                    infoWindowView.onMarkerLayoutChangeListener);
        }
        infoWindowMap.clear();
    }

    @Override
    public View getExternalInfoContents(MarkerView markerView) {
        return infoWindowAdapter != null ? infoWindowAdapter.getInfoContents(markerView) : null;
    }
}

