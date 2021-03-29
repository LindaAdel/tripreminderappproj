package com.example.tripreminderapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tripreminderiti.database.note.Note;
import com.example.tripreminderiti.database.note.NoteDao;
import com.example.tripreminderiti.database.trip.Trip;
import com.example.tripreminderiti.database.trip.TripDao;

@Database(entities = {Trip.class, Note.class}, version = 1)
public abstract class TripDatabase extends RoomDatabase {
    private static final String DB_NAME = "DB-TRIP";
    private static TripDatabase tripDatabase = null;

    public static TripDatabase getInstance(Context context) {
        if (tripDatabase == null) {
            tripDatabase = Room.databaseBuilder(context,
                    TripDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return tripDatabase;
    }

    public abstract TripDao tripDao();

    public abstract NoteDao noteDao();
}
