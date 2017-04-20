package com.vulpeszerda.mapoverlayviewdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.vulpeszerda.mapoverlayview.MapOverlayLayout;
import com.vulpeszerda.mapoverlayview.MarkerOptions;

import static com.vulpeszerda.mapoverlayviewdemo.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapOverlayLayout mapOverlayLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        mapOverlayLayout = (MapOverlayLayout) findViewById(R.id.map_overlay);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapOverlayLayout.setupMap(mMap);
        mMap.setOnCameraMoveListener(mapOverlayLayout);
        mMap.setOnCameraMoveCanceledListener(mapOverlayLayout);
        mMap.setOnCameraMoveStartedListener(mapOverlayLayout);
        mMap.setOnCameraIdleListener(mapOverlayLayout);

        LatLng sydney = new LatLng(-34, 151);
        mapOverlayLayout.addMarker(new MarkerOptions(sydney).title("Sydney").snippet("snippet"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
