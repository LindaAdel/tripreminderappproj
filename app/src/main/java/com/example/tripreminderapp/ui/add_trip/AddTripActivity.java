package com.example.tripreminderapp.ui.add_trip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.tripreminderapp.R;
import com.example.tripreminderapp.database.trip.Trip;
import com.example.tripreminderapp.databinding.ActivityAddTripBinding;
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

    private ActivityAddTripBinding binding;
    private static final String TAG = "Tag";
    private static final int REQ_CODE = 111;
    final Calendar c = Calendar.getInstance();
    private AddTripViewModel viewModel;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(AddTripViewModel.class);
        getSupportActionBar().hide();

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
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//        };
        binding.edDate.getEditText().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(AddTripActivity.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }

            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String _year = String.valueOf(year);
                        String _month = (month+1) < 10 ? "0" + (month+1) : String.valueOf(month+1);
                        String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                        String _pickedDate = _year + "-" + _month + "-" + _date;
                        Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12
                        binding.edDate.getEditText().setText(_pickedDate);
                        //updateLabel();
                    }
                } , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                dialog.show();
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



                if(validateError() == true) {
                    Trip trip = new Trip(name, startPoint, endPoint, date, time,date+" "+time,type);
                    viewModel.insertTrip(trip);
                }

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
//    private void updateLabel() {
//        String myFormat = "MM/dd/yyyy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        binding.edDate.getEditText().setText(sdf.format(c.getTime()));
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private Boolean validateError() {
        String tNameVal = binding.edName.getEditText().getText().toString();
        String spointVal = binding.edStartPoint.getEditText().getText().toString();
        String epointVal = binding.edEndPoint.getEditText().getText().toString();
        String dateVal = binding.edDate.getEditText().getText().toString();
        String timeVal = binding.edTime.getEditText().getText().toString();
        if(tNameVal.isEmpty() ){
            binding.edName.setError("TripName Required");
            binding.edName.requestFocus();
            return false;
        }
        else if(spointVal.isEmpty()) {
            binding.edStartPoint.setError("Start Point required");
            binding.edStartPoint.requestFocus();

            return false;
        }
        else if(epointVal.isEmpty()) {
            binding.edEndPoint.setError("End Point required");
            binding.edEndPoint.requestFocus();
            return false;
        }
        else if(dateVal.isEmpty()) {
            binding.edDate.setError("date reqquired");
            binding.edDate.requestFocus();
            return false;
        }
        else if(timeVal.isEmpty()) {
            binding.edTime.setError("Time required");
            binding.edTime.requestFocus();
            return false;
        }
        else {
            binding.edName.setError(null);
            binding.edName.setErrorEnabled(false);

            binding.edStartPoint.setError(null);
            binding.edStartPoint.setErrorEnabled(false);

            binding.edEndPoint.setError(null);
            binding.edEndPoint.setErrorEnabled(false);

            binding.edDate.setError(null);
            binding.edDate.setErrorEnabled(false);

            binding.edTime.setError(null);
            binding.edTime.setErrorEnabled(false);

            return true;
        }

    }
}




