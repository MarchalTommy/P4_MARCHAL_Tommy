package com.aki.mareu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.aki.mareu.events.DeleteReunionEvent;
import com.aki.mareu.events.FilterByDateEvent;
import com.aki.mareu.events.FilterByRoomEvent;
import com.aki.mareu.events.GetReunionDetail;
import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.models.User;
import com.aki.mareu.service.ReunionApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Reunion extends Fragment implements View.OnClickListener {

    private static final String TAG = "Fragment_ReunionRecycle";

    NavController navController = null;
    ReunionApiService mApiService = DI.getReunionApiService();
    List<Reunion> mReunions = new ArrayList<>();
    Reunion reunion;

    //UI
    RecyclerView mRecyclerView;
    FloatingActionButton unfilterFab;

    public static Fragment_Reunion newInstance() {
        return new Fragment_Reunion();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reunion_list, container, false);

        Context context = view.getContext();

        //Prepare the RecyclerView
        mRecyclerView = view.findViewById(R.id.list_reunions);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        FloatingActionButton newReunionFab = view.findViewById(R.id.fab);
        newReunionFab.setOnClickListener(this);

        unfilterFab = view.findViewById(R.id.delete_filter_fab);
        unfilterFab.setVisibility(View.GONE);
        unfilterFab.setOnClickListener(view1 -> {
            Fragment_Reunion.this.initList();
            unfilterFab.setVisibility(View.GONE);
        });
    }

    private void initList() {
        mReunions.clear();
        mReunions.addAll(mApiService.getReunions());
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.setAdapter(new ReunionRecyclerViewAdapter(mReunions));
        Log.d(TAG, "initList: List initialised  mApiSize : " + mApiService.getReunions().size() + "");
    }

    @Override
    public void onClick(View view) {
        navController.navigate(R.id.action_mainFragment_to_addReunionFragment);
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
        mApiService = DI.getNewInstanceApiService();
        mReunions.clear();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDeleteReunion(DeleteReunionEvent event) {
        mApiService.deleteReunion(event.reunion);
        mReunions.remove(event.reunion);

        mRecyclerView.getAdapter().notifyDataSetChanged();
        Toast.makeText(getContext(), "" + event.reunion.getName() + " has been deleted successfully.", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onFilterByDate(FilterByDateEvent event) {
        mApiService.setFilteringDate(event.date);
        mReunions.removeAll(mApiService.filterByDate());

        Log.d(TAG, "onFilterByDate: Filter Applied successfully from the API");
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        unfilterFab.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onFilterByRoom(FilterByRoomEvent event) {
        mApiService.setFilteringRoom(event.room);
        mReunions.removeAll(mApiService.filterByRoom());

        Log.d(TAG, "onFilterByDate: Filter Applied successfully from the API");
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        unfilterFab.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onReunionDetail(GetReunionDetail event) {
        reunion = event.reunion;
        DetailPopup popup = new DetailPopup(getActivity());
        popup.build();
    }

    //Class for the detail popup
    public class DetailPopup extends Dialog {

        public DetailPopup(Activity activity) {
            super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
            setContentView(R.layout.popup_reuniondetails);
        }

        public void build() {
            show();

            Room mRoom = null;
            TextView reunionName = findViewById(R.id.reunion_name);
            TextView creatorName = findViewById(R.id.creator_name);
            TextView date = findViewById(R.id.date_detail);
            TextView time = findViewById(R.id.time_detail);
            TextView room = findViewById(R.id.room_detail);

            RecyclerView participantsRV = findViewById(R.id.participants_detail_recyclerview);
            participantsRV.setLayoutManager(new LinearLayoutManager(this.getContext()));
            participantsRV.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            reunionName.setText(reunion.getName());
            creatorName.setText(reunion.getCreator());
            date.setText(reunion.getDate());
            time.setText(reunion.getHour());

            for (Room r : mApiService.getRooms()) {
                if (r.getId() == reunion.getRoom()) {
                    mRoom = r;
                }
            }
            room.setText(mRoom.getName());

            List<User> mParticipants = reunion.getParticipants();
            participantsRV.setAdapter(new DetailRecyclerViewAdapter((ArrayList<User>) mParticipants));
        }
    }
}