package com.example.tripreminderapp.ui.add_trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminderiti.R;
import com.example.tripreminderiti.database.trip.Trip;
import com.example.tripreminderiti.databinding.ActivityAddTripBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTripActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityAddTripBinding  binding;
    private static final String TAG = "Tag";
    private static final int REQ_CODE = 111;
    final Calendar myCalendar = Calendar.getInstance();
    private AddTripViewModel viewModel;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(AddTripViewModel.class);


        spinner = findViewById(R.id.trip_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        Places.initialize(getApplicationContext(), getString(R.string.api_places_key));
        List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddTripActivity.this);

        viewModel.getIsInsertedLiveData().observe(this,aBoolean -> {
            if (aBoolean)
                finish();
        });
        binding.edStartPoint.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQ_CODE);
            }
        });
        binding.edEndPoint.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intent, REQ_CODE + 1);
            }
        });
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        binding.edDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddTripActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.edTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        binding.edTime.getEditText().setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        binding.addNewTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.edName.getEditText().getText().toString();
                String startPoint = binding.edStartPoint.getEditText().getText().toString();
                String endPoint = binding.edEndPoint.getEditText().getText().toString();
                String date = binding.edDate.getEditText().getText().toString();
                String time = binding.edTime.getEditText().getText().toString();
                String type = getResources().getStringArray(R.array.types)[spinner.getSelectedItemPosition()];



                Trip trip = new Trip(name, startPoint, endPoint, date, time,date+" "+time,type);
                viewModel.insertTrip(trip);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.edStartPoint.getEditText().setText(place.getAddress());
        } else if (requestCode == REQ_CODE + 1 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            binding.edEndPoint.getEditText().setText(place.getAddress());
        } else {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, "error  " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        binding.edDate.getEditText().setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}




