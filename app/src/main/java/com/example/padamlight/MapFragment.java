package com.example.padamlight;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Map Fragment
 * Responsible of displaying map and interactions with it
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MapActionsDelegate {

    @Nullable
    private GoogleMap mMap;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Instanciate map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.902964, 1.9092510000000402), 16f));
        }
    }

    @Override
    public void updateMap(LatLng... latLngs) {
        if (mMap != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : latLngs) {
                builder.include(latLng);
            }
            LatLngBounds bounds = builder.build();
            animateMapCamera(bounds);
        }
    }

    @Override
    public void updateMarker(MarkerType type, String markerName, LatLng markerLatLng) {
        if (mMap != null) {
            MarkerOptions marker = new MarkerOptions()
                    .position(markerLatLng)
                    .title(markerName)
                    .icon(getMarkerIcon(type));

            mMap.addMarker(marker);
        }
    }

    @Override
    public void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    private void animateMapCamera(LatLngBounds bounds) {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    private BitmapDescriptor getMarkerIcon(MarkerType type) {
        @DrawableRes int icon = R.drawable.ic_pickup;

        switch (type) {
            case PICKUP: {
                icon = R.drawable.ic_pickup;
                break;
            }
            case DROPOFF: {
                icon = R.drawable.ic_dropoff;
                break;
            }
        }
        return BitmapDescriptorFactory.fromResource(icon);
    }

}


/**
 * Map interface
 * Implement this in your page where there is a map to use map methods
 */
interface MapActionsDelegate {
    void updateMap(LatLng... latLngs);

    void updateMarker(MarkerType type, String markerName, LatLng markerLatLng);

    void clearMap();
}

/**
 * Market enum
 * Define if marker is pickup type or dropoff type
 */
enum MarkerType {
    PICKUP, DROPOFF;
}
