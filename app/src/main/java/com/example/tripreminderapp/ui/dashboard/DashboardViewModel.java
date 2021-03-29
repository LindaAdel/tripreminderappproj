package com.example.tripreminderapp.ui.dashboard;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminderiti.database.TripDatabase;
import com.example.tripreminderiti.database.trip.Trip;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final TripDatabase database;
    private final MutableLiveData<List<Trip>> tripsListLiveData = new MutableLiveData<>();


    public DashboardViewModel(@NonNull Application application) {
        super(application);
        database = TripDatabase.getInstance(getApplication());
        getTripsFromDatabase();
    }



    private void getTripsFromDatabase() {
        tripsListLiveData.setValue(database.tripDao().getTripDone());
        tripsListLiveData.setValue(database.tripDao().getAll());

    }

    public MutableLiveData<List<Trip>> getTripsListLiveData() {
        return tripsListLiveData;
    }

//    public LiveData<String> getText() {
//        return mText;
//    }
}