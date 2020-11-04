package com.aki.mareu;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aki.mareu.events.FilterByDateEvent;
import com.aki.mareu.events.FilterByRoomEvent;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activity = this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_filter);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                FilterPopup popup = new FilterPopup(activity);
                popup.build();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Classe générant et gérant le popup de filtre
    public class FilterPopup extends Dialog {

        Spinner roomSpinner;
        DatePicker picker;

        public FilterPopup(Activity activity) {
            super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
            setContentView(R.layout.popup);
        }

        public void build() {
            show();
            //FindViewById boilerplate car j'ai pas réussi à utiliser le ViewBinding
            roomSpinner = findViewById(R.id.room_spinner);
            RadioGroup radioGroup = findViewById(R.id.radiogroup);
            LinearLayout roomPickerLayout = findViewById(R.id.roompicker_layout);
            LinearLayout datePickerLayout = findViewById(R.id.datepicker_layout);
            Button filterDateButton = findViewById(R.id.filter_date_button);
            Button filterRoomButton = findViewById(R.id.filter_room_button);
            Button cancelButton = findViewById(R.id.cancel_button);
            picker = findViewById(R.id.picker);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.Room, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roomSpinner.setAdapter(adapter);

            //RadioGroup pour gérer quel filtre appliquer
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup1, int i) {
                    switch (radioGroup1.getCheckedRadioButtonId()) {
                        case R.id.radio_date:
                            roomPickerLayout.setVisibility(View.GONE);
                            datePickerLayout.setVisibility(View.VISIBLE);
                            cancelButton.setVisibility(View.GONE);
                            Log.d(TAG, "onCheckedChanged: Filter chosen : DATE");
                            break;
                        case R.id.radio_room:
                            datePickerLayout.setVisibility(View.GONE);
                            roomPickerLayout.setVisibility(View.VISIBLE);
                            cancelButton.setVisibility(View.GONE);
                            Log.d(TAG, "onCheckedChanged: Filter chosen : ROOM");
                            break;
                    }
                }
            });
            //Appliquer le filtre par date
            filterDateButton.setOnClickListener(new FilterByDate());
            //Appliquer le filtre par salle
            filterRoomButton.setOnClickListener(new FilterByRoom());
            //Annuler l'ajout d'un filtre
            cancelButton.setOnClickListener(view -> {
                this.dismiss();
            });
        }

        class FilterByDate implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                int day, month, year;
                String date;
                day = picker.getDayOfMonth();
                month = picker.getMonth();
                year = picker.getYear();
                if (month < 9) {
                    if (day < 10) {
                        date = ("0" + day + "/0" + (month + 1) + "/" + year);
                    } else date = (day + "/0" + (month + 1) + "/" + year);
                } else if (day < 10) {
                    date = ("0" + day + "/" + (month + 1) + "/" + year);
                } else date = (day + "/" + (month + 1) + "/" + year);
                EventBus.getDefault().post(new FilterByDateEvent(date));
                Toast.makeText(FilterPopup.this.getContext(), "Filtering by date has been applied.\nOnly " + date + " is showing", Toast.LENGTH_SHORT).show();
                FilterPopup.this.dismiss();
            }
        }

        class FilterByRoom implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                if (roomSpinner.getSelectedItem() != roomSpinner.getItemAtPosition(0)) {
                    String room = roomSpinner.getSelectedItem().toString();
                    EventBus.getDefault().post(new FilterByRoomEvent(room));
                    Toast.makeText(FilterPopup.this.getContext(), "Filtering by room has been applied.\nOnly " + roomSpinner.getSelectedItem().toString() + " is showing.", Toast.LENGTH_SHORT).show();
                    FilterPopup.this.dismiss();
                } else {
                    FilterPopup.this.dismiss();
                    Toast.makeText(FilterPopup.this.getContext(), "No filter were applied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}