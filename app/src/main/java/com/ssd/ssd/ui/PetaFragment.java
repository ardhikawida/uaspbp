package com.ssd.ssd.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.ssd.ssd.R;
import com.ssd.ssd.databinding.FragmentPetaBinding;

public class PetaFragment extends Fragment {

    FragmentPetaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Mapbox.getInstance(requireContext().getApplicationContext(), getString(R.string.mapbox_access_token));

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_peta,container,false);
        binding.getLifecycleOwner();


        binding.mapbox.onCreate(savedInstanceState);
        binding.mapbox.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {


                        LatLng lokasi = new LatLng(-7.804159, 110.387281);
                        CameraPosition position = new CameraPosition.Builder()
                                .target(lokasi)
                                .zoom(10)
                                .tilt(20)
                                .build();                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments

                        mapboxMap.addMarker(new MarkerOptions()
                                .position(lokasi)
                                .title("Posisi SSD Restaurant"));


                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000);


                    }
                });

            }
        });
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapbox.onDestroy();

        binding = null;

    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapbox.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapbox.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapbox.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapbox.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapbox.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapbox.onLowMemory();
    }

}