package com.aki.mareu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aki.mareu.adapter.ParticipantsRecyclerViewAdapter;
import com.aki.mareu.databinding.FragmentAddreunionBinding;
import com.aki.mareu.di.DI;
import com.aki.mareu.events.AddParticipantEvent;
import com.aki.mareu.events.RemoveParticipantEvent;
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
    List<User> mParticipants = new ArrayList<>();
    public AlertDialog dialog = null;
    Activity activity;

    //Ui
    private FragmentAddreunionBinding binding;
    ArrayAdapter<CharSequence> adapter;

    public static Fragment_AddReunion newInstance() {
        Fragment_AddReunion fragment = new Fragment_AddReunion();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity  = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_addreunion, container, false);

        adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.Room, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAddreunionBinding.bind(view);
        navController = Navigation.findNavController(view);

        //Room Selection
        binding.roomSpinnerNewReunion.setAdapter(adapter);

        //Users participating RecyclerView
        binding.participantsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.participantsRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //DatePicker
        binding.dateEdittext.setInputType(InputType.TYPE_NULL);
        setDateListener();

        //TimePicker
        binding.timeEdittext.setInputType(InputType.TYPE_NULL);
        setTimeListener();

        //Create a new reunion
        createReunion();
    }

    public void setDateListener() {
        binding.dateEdittext.setOnClickListener(view13 -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            DatePickerDialog picker = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                if (month1 < 9) {
                    if (day1 < 10) {
                        binding.dateEdittext.setText("0" + day1 + "/0" + (month1 + 1) + "/" + year1);
                    } else binding.dateEdittext.setText(day1 + "/0" + (month1 + 1) + "/" + year1);
                } else if (day1 < 10) {
                    binding.dateEdittext.setText("0" + day1 + "/" + (month1 + 1) + "/" + year1);
                } else binding.dateEdittext.setText(day1 + "/" + (month1 + 1) + "/" + year1);
            }, year, month, day);
            picker.show();
        });
    }

    public void setTimeListener() {
        binding.timeEdittext.setOnClickListener(view12 -> {
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
    }

    public void getNewParticipants() {
        String newParticipants;
        newParticipants = binding.newParticipant.getText().toString();
        if (!newParticipants.isEmpty()) {
            String[] mails = newParticipants.split("\n");
            int nbrNewParticipants = mails.length;
            for (String s : mails) {
                int nameEnd = s.indexOf("@");
                mParticipants.add(new User(mApiService.getNewId(), s.substring(0, nameEnd), s));
            }
            for (User u : mParticipants) {
                System.out.println(u.getName());
            }
        }
    }

    public List<User> getParticpantsList() {
        return mParticipants;
    }

    private AlertDialog errorDialog() {
        Context context;
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this.getContext());

        alertBuilder.setTitle("Error");
        alertBuilder.setPositiveButton("Ok", null);
        alertBuilder.setMessage("In order to create a reunion, please fill up every field. You forgot to choose a room.");

        dialog = alertBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    public void createReunion() {
        Toast.makeText(activity,"THIS IS A TEST PLEASE WORK", Toast.LENGTH_LONG).show();
        binding.createNewReunionButton.setOnClickListener(view1 -> {
            Room room = null;
            getNewParticipants();
            switch (binding.roomSpinnerNewReunion.getSelectedItem().toString().trim()) {
                case "Select a room":
                    final AlertDialog dialog = errorDialog();
                    dialog.show();
                    break;
                default:
                    for (Room r : mApiService.getRooms()) {
                        if (r.getId() == binding.roomSpinnerNewReunion.getSelectedItemPosition()) {
                            room = r;
                        }
                    }
                    if (binding.nameEdittext.getText().toString().isEmpty()) {
                        binding.nameEdittext.setError("Your reunion needs to have a name.");
                    } else if (binding.dateEdittext.getText().toString().isEmpty()) {
                        binding.dateEdittext.setError("You need to choose a date for your reunion.");
                    } else if (binding.timeEdittext.getText().toString().isEmpty()) {
                        binding.timeEdittext.setError("Please select a time as to when you reunion is gonna takes place.");
                    } else if (binding.creatornameEdittext.getText().toString().isEmpty()) {
                        binding.creatornameEdittext.setError("You must enter your name");
                    } else if (mParticipants == null || mParticipants.isEmpty()) {
                        binding.newParticipant.setError("Please add at least 1 participant.");
                    } else {
                        Reunion newReunion = new Reunion(
                                mApiService.getNewId(),
                                binding.nameEdittext.getText().toString(),
                                room,
                                binding.timeEdittext.getText().toString(),
                                binding.dateEdittext.getText().toString(),
                                mParticipants.size(),
                                binding.creatornameEdittext.getText().toString(),
                                getParticpantsList());
                        mApiService.createReunion(newReunion);
                        Log.d(TAG, "onViewCreated: Reunion passed successfully to the list");
                        navController.navigate(R.id.action_addReunionFragment_to_mainFragment);
                        break;
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
        binding.participantsRecyclerview.scheduleLayoutAnimation();
        binding.participantsRecyclerview.setAdapter(new ParticipantsRecyclerViewAdapter((ArrayList<User>) mUsers));
    }

    @Subscribe
    public void onAddParticipant(AddParticipantEvent event) {
        Log.d(TAG, "onAddParticipants: Event called successfully");
        mParticipants.add(event.participant);
    }

    @Subscribe
    public void onDeleteParticipant(RemoveParticipantEvent event) {
        mParticipants.remove(event.participant);
    }

    class TimeListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            if (i1 < 10) {
                binding.timeEdittext.setText(i + "H0" + i1);
            } else binding.timeEdittext.setText(i + "H" + i1);
        }
    }

}