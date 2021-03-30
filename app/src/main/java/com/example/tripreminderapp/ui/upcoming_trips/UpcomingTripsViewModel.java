package com.example.tripreminderapp.ui.upcoming_trips;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.tripreminderapp.MyWorker;
import com.example.tripreminderapp.database.TripDatabase;
import com.example.tripreminderapp.database.trip.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UpcomingTripsViewModel extends AndroidViewModel {
    private final TripDatabase database;
    private final MutableLiveData<List<Trip>> tripsListLiveData = new MutableLiveData<>();
  //  private MutableLiveData<Boolean> isInsertedLiveData =new MutableLiveData<Boolean>(false);

    public UpcomingTripsViewModel(@NonNull Application application) {
        super(application);
        database = TripDatabase.getInstance(getApplication());
        getTripsFromDatabase();
    }


    public void getTripsFromDatabase() {
        tripsListLiveData.setValue(database.tripDao().getAll());

    }



    public void setAlarm(Trip trip) {
        Trip currentTrip = trip;
        long seconds = calcDeferenceInDatesBySeconds(currentTrip.getDate_time());
        if (!trip.getIsAlarmPrepared()) {
            WorkRequest uploadWorkRequest =
                    new OneTimeWorkRequest.Builder(MyWorker.class)
                            .setInitialDelay(seconds, TimeUnit.SECONDS)
                            .build();
            WorkManager.getInstance(getApplication()).enqueue(uploadWorkRequest);
            currentTrip.setAlarmPrepared(true);
            updateTrip(currentTrip);
        }
    }

    private long calcDeferenceInDatesBySeconds(String date_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date tripDate = null;
        try {
            tripDate = dateFormat.parse(date_time);
            Date currentDate = new Date();
            long diff = tripDate.getTime() - currentDate.getTime();
            return diff / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

//    private void updateTrip(Trip trip) {
//        database.tripDao().updateTrip(trip.getName(), trip.getStartPoint(), trip.getStartPoint(), trip.getDate(), trip.getTime(), trip.getDate_time(), trip.getIsAlarmPrepared(),false, trip.getSpinner(),trip.getId());
//    }

    private void updateTrip(Trip trip) {
        database.tripDao().update(trip);
    }

    public MutableLiveData<List<Trip>> getTripsListLiveData() {
        return tripsListLiveData;
    }

}
