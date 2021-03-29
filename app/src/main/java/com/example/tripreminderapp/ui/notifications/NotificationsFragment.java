package com.example.tripreminderapp.ui.notifications;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminderiti.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment
{

    GoogleMap gMap;
    Polyline polyline = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    public NotificationsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);

        //Initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
              //when map is loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //when clicked on map
                        //Initialize maker option
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set position of maker
                        markerOptions.position(latLng);
                        //setTitile of maker
                        markerOptions.title(latLng.latitude+" : "+latLng.longitude);
                        //Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                latLng,10
                        ));
                        //Add marker on map
                       Marker marker =  googleMap.addMarker(markerOptions);

                    }
                });
            }
        });


        return  view;
    }


}


























//public class NotificationsFragment extends Fragment implements OnMapReadyCallback {
//
//    private NotificationsViewModel notificationsViewModel;
//    GoogleMap gMap;
//    SupportMapFragment supportMapFragment;
//    Polyline polyline = null;
//    List<LatLng> latLngList = new ArrayList<>();
//    List<Marker> markerList = new ArrayList<>();
//    private GoogleMap googleMap;
//
//
//
//
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_notifications, null, false);
//
//
//
////        final TextView textView = root.findViewById(R.id.text_notifications);
////        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
////            @Override
////            public void onChanged(@Nullable String s) {
////                textView.setText(s);
////            }
////        });
//
//
//
//
//        //Initialize MapFragment
////        supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
////
////        supportMapFragment.getMapAsync( this);
//
//
//        SupportMapFragment mapFragment = (SupportMapFragment) (getActivity()).getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//
//
//
//
//
//
//
//        return root;
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gMap = googleMap;
//        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                //create marker options
//                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
//                //create Marker
//                Marker marker = gMap.addMarker(markerOptions);
//                //AddLatlng and marker
//                latLngList.add(latLng);
//                markerList.add(marker);
//
//                //draw PolyLine onMap
//                if(polyline != null)polyline.remove();
//
//                //create polyLineOptions
//                PolylineOptions polylineOptions = new PolylineOptions()
//                        .addAll(latLngList).clickable(true);
//                polyline = gMap.addPolyline(polylineOptions);
//
//
//
//            }
//        });
//    }
//}