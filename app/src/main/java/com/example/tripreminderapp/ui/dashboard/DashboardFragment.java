package com.example.tripreminderapp.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tripreminderiti.GeoLocation;

import com.example.tripreminderiti.R;
import com.example.tripreminderiti.database.TripDatabase;
import com.example.tripreminderiti.database.trip.Trip;
import com.example.tripreminderiti.databinding.FragmentDashboardBinding;
import com.example.tripreminderiti.databinding.FragmentUpcomingBinding;
import com.example.tripreminderiti.ui.add_trip.AddTripActivity;
import com.example.tripreminderiti.ui.upcoming_trips.UpcomingTripsViewModel;

import java.util.List;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;

    String address;
    String[]sentenseArray;
    String lat,lang;
    double latitude,langtude;

    private  final DashboardAdapter dashboardAdapter = new DashboardAdapter();
    List<Trip> startPoints;
    Trip trip;

    public DashboardFragment(){}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);



        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        binding.dashRvTrip.setAdapter(dashboardAdapter);

        dashboardViewModel.getTripsListLiveData().observe(getViewLifecycleOwner(), trips -> {
            dashboardAdapter.changeData(trips);

        });
/*
        binding.h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MapActivity.class));
            }
        });
*/



        dashboardAdapter.changeData(TripDatabase.getInstance(getActivity()).tripDao().getTripDone());


        List<Trip>data = TripDatabase.getInstance(getActivity()).tripDao().getAll();

//        getLangLat(data.get(0).getStartPoint());
  //      getLangLat(data.get(0).getEndPoint());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }






    //togetLangtude and latitude
    public void getLangLat(String address){

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.getAddress(address, getActivity().getApplicationContext(),new GeoHandler());
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch(msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address =bundle.getString("address");
                    break;
                default:
                    address=null;
            }
            Log.e("lll","the item is "+ address);
            //sentenseArray = address.split(",,,");
            //lat = sentenseArray[0];
            //lang = sentenseArray[1];

            //latitude = Double.parseDouble(lat);
            //langtude = Double.parseDouble(lang);


            Log.e("lll","lat item is "+ latitude);
            Log.e("lll","lang item is "+ langtude);


        }
    }




}