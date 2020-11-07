package com.aki.mareu;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aki.mareu.adapter.DetailRecyclerViewAdapter;
import com.aki.mareu.adapter.ReunionRecyclerViewAdapter;
import com.aki.mareu.databinding.FragmentReunionListBinding;
import com.aki.mareu.databinding.PopupReuniondetailsBinding;
import com.aki.mareu.di.DI;
import com.aki.mareu.events.DeleteReunionEvent;
import com.aki.mareu.events.FilterByDateEvent;
import com.aki.mareu.events.FilterByRoomEvent;
import com.aki.mareu.events.GetReunionDetail;
import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.models.User;
import com.aki.mareu.service.ReunionApiService;

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
    private FragmentReunionListBinding binding;


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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentReunionListBinding.bind(view);
        navController = Navigation.findNavController(view);

        //Prepare the RecyclerView
        binding.listReunions.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.listReunions.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //Setting the OnClickListener for the FAB to add a new reunion
        binding.fab.setOnClickListener(this);

        //Setting the FAB to cancel the filter
        binding.deleteFilterFab.setVisibility(View.GONE);
        binding.deleteFilterFab.setOnClickListener(view1 -> {
            Fragment_Reunion.this.initList();
            binding.deleteFilterFab.setVisibility(View.GONE);
        });
    }

    private void initList() {
        mReunions.clear();
        mReunions.addAll(mApiService.getReunions());
        binding.listReunions.scheduleLayoutAnimation();
        binding.listReunions.setAdapter(new ReunionRecyclerViewAdapter(mReunions));
        Log.d(TAG, "initList: List initialised  mApiSize : " + mApiService.getReunions().size() + "");
    }

    //OnClick for the FAB to change fragment to add a new reunion
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

        binding.listReunions.getAdapter().notifyDataSetChanged();
        Toast.makeText(getContext(), "" + event.reunion.getName() + " has been deleted successfully.", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onFilterByDate(FilterByDateEvent event) {
        mApiService.setFilteringDate(event.date);
        mReunions.removeAll(mApiService.filterByDate());

        Log.d(TAG, "onFilterByDate: Filter Applied successfully from the API");
        binding.listReunions.scheduleLayoutAnimation();
        binding.listReunions.getAdapter().notifyDataSetChanged();
        binding.deleteFilterFab.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onFilterByRoom(FilterByRoomEvent event) {
        mApiService.setFilteringRoom(event.room);
        mReunions.removeAll(mApiService.filterByRoom());

        Log.d(TAG, "onFilterByDate: Filter Applied successfully from the API");
        binding.listReunions.scheduleLayoutAnimation();
        binding.listReunions.getAdapter().notifyDataSetChanged();
        binding.deleteFilterFab.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void onReunionDetail(GetReunionDetail event) {
        reunion = event.reunion;
        DetailPopup popup = new DetailPopup(getActivity());
        popup.build();
    }

    //Class for the detail popup
    public class DetailPopup extends Dialog {

        private final PopupReuniondetailsBinding popupBinding;

        public DetailPopup(Activity activity) {
            super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
            popupBinding = PopupReuniondetailsBinding.inflate(getLayoutInflater());
            setContentView(popupBinding.getRoot());
        }

        public void build() {
            show();
            Room mRoom = null;
            popupBinding.participantsDetailRecyclerview.setLayoutManager(new LinearLayoutManager(this.getContext()));
            popupBinding.participantsDetailRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

            popupBinding.reunionName.setText(reunion.getName());
            popupBinding.creatorName.setText(reunion.getCreator());
            popupBinding.dateDetail.setText(reunion.getDate());
            popupBinding.timeDetail.setText(reunion.getHour());
            for (Room r : mApiService.getRooms()) {
                if (r.getId() == reunion.getRoom()) {
                    mRoom = r;
                }
            }
            popupBinding.roomDetail.setText(mRoom.getName());
            List<User> mParticipants = reunion.getParticipants();
            popupBinding.participantsDetailRecyclerview.setAdapter(new DetailRecyclerViewAdapter((ArrayList<User>) mParticipants));
        }
    }
}