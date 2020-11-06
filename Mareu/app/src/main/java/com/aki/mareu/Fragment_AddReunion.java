package com.aki.mareu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aki.mareu.di.DI;
import com.aki.mareu.events.AddReunionEvent;
import com.aki.mareu.events.ParticipantsEvent;
import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.models.User;
import com.aki.mareu.service.ReunionApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Fragment_AddReunion extends Fragment {

    NavController navController = null;
    ReunionApiService mApiService = DI.getReunionApiService();
    List<User> mParticipants;

    //Ui
    EditText reunionName;
    EditText creatorName;
    Spinner roomSpinner;
    EditText dateText;
    EditText timeText;
    RecyclerView mRecyclerView;
    Button createButton;

    public static Fragment_AddReunion newInstance() {
        Fragment_AddReunion fragment = new Fragment_AddReunion();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addreunion, container, false);
        Spinner roomSpinner = view.findViewById(R.id.room_spinner_newReunion);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.Room, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(adapter);
        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.participants_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        reunionName = view.findViewById(R.id.name_edittext);
        creatorName = view.findViewById(R.id.creatorname_edittext);
        //DatePicker
        dateText = view.findViewById(R.id.date_edittext);
        dateText.setInputType(InputType.TYPE_NULL);
        dateText.setOnClickListener(view13 -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            DatePickerDialog picker = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                if (month1 < 9) {
                    if (day1 < 10) {
                        dateText.setText("0" + day1 + "/0" + (month1 + 1) + "/" + year1);
                    } else dateText.setText(day1 + "/0" + (month1 + 1) + "/" + year1);
                } else if (day1 < 10) {
                    dateText.setText("0" + day1 + "/" + (month1 + 1) + "/" + year1);
                } else dateText.setText(day1 + "/" + (month1 + 1) + "/" + year1);
            }, year, month, day);
            picker.show();
        });
        //TimePicker
        timeText = view.findViewById(R.id.time_edittext);
        timeText.setInputType(InputType.TYPE_NULL);
        timeText.setOnClickListener(view12 -> {
            final Calendar cldr = Calendar.getInstance();
            int lastSelectedHour = cldr.get(Calendar.HOUR_OF_DAY);
            int lastSelectedMinute = cldr.get(Calendar.MINUTE);
            final boolean is24HView = true;
            final boolean isSpinnerMode = false;

            TimePickerDialog.OnTimeSetListener timeSetListener = new TimeListener();

            TimePickerDialog timePickerDialog = null;
            timePickerDialog = new TimePickerDialog(getContext(), timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);
            timePickerDialog.show();
        });
        //Room Selection
        roomSpinner = view.findViewById(R.id.room_spinner_newReunion);
        //Create a new reunion
        createButton = view.findViewById(R.id.create_new_reunion_button);
        createReunion();
    }

    class TimeListener implements TimePickerDialog.OnTimeSetListener{

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            if (i1 < 10) {
                timeText.setText(i + "H0" + i1);
            } else timeText.setText(i + "H" + i1);
        }
    }

    public void createReunion() {
        createButton.setOnClickListener(view1 -> {
            Room room = null;
            for (Room r : mApiService.getRooms()) {
                if (r.getName().equals(roomSpinner.getSelectedItem().toString())) {
                    room = r;
                }
            }
            if (mParticipants == null) {
                if (reunionName.getText().toString().isEmpty() || timeText.getText().toString().isEmpty() || dateText.getText().toString().isEmpty() || creatorName.getText().toString().isEmpty()) {
                    Toast.makeText(this.getContext(), "Please fill up every field to create a new reunion.", Toast.LENGTH_LONG).show();
                }
                else {
                    Reunion newReunion = new Reunion(
                            mApiService.getNewId(),
                            reunionName.getText().toString(),
                            room,
                            timeText.getText().toString(),
                            dateText.getText().toString(),
                            0,
                            creatorName.getText().toString(),
                            mParticipants);
                    mApiService.createReunion(newReunion);
                    Log.d(TAG, "onViewCreated: Reunion passed successfully to the list");
                    navController.navigate(R.id.action_addReunionFragment_to_mainFragment);
                }
            } else {
                if (reunionName.getText().toString().isEmpty() || timeText.getText().toString().isEmpty() || dateText.getText().toString().isEmpty() || creatorName.getText().toString().isEmpty()) {
                    Toast.makeText(this.getContext(), "Please fill up every field to create a new reunion.", Toast.LENGTH_LONG).show();
                } else {
                    Reunion newReunion = new Reunion(
                            mApiService.getNewId(),
                            reunionName.getText().toString(),
                            room,
                            timeText.getText().toString(),
                            dateText.getText().toString(),
                            mParticipants.size(),
                            creatorName.getText().toString(),
                            mParticipants);
                    mApiService.createReunion(newReunion);
                    Log.d(TAG, "onViewCreated: Reunion passed successfully to the list");
                    navController.navigate(R.id.action_addReunionFragment_to_mainFragment);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void initList() {
        List<User> mUsers = mApiService.getUsers();
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.setAdapter(new ParticipantsRecyclerViewAdapter((ArrayList<User>) mUsers));
    }

    @Subscribe
    public void onAddParticipants(ParticipantsEvent event) {
        Log.d(TAG, "onAddParticipants: Event called successfully");
        mParticipants = event.participants;
    }

}