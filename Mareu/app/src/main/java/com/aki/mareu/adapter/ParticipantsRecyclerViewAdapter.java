package com.aki.mareu.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.aki.mareu.databinding.FragmentParticipantsBinding;
import com.aki.mareu.events.AddParticipantEvent;
import com.aki.mareu.events.RemoveParticipantEvent;
import com.aki.mareu.models.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class ParticipantsRecyclerViewAdapter extends RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.ParticipantsViewHolder> {

    private final List<User> mUsers;
    public List<User> mParticipants = new ArrayList<>();
    ViewGroup parents;

    public ParticipantsRecyclerViewAdapter(List<User> items) {
        this.mUsers = items;
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentParticipantsBinding item = FragmentParticipantsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        parents = parent;
        return new ParticipantsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ParticipantsViewHolder viewHolder, int position) {
        User user = mUsers.get(position);
        viewHolder.bind(user);
        if (mParticipants.contains(user))
            viewHolder.binding.checkBox.setChecked(true);
        else
            viewHolder.binding.checkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ParticipantsViewHolder extends RecyclerView.ViewHolder {

        private FragmentParticipantsBinding binding;

        public ParticipantsViewHolder(FragmentParticipantsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(final User user) {
            binding.participantName.setText(user.getName());
            binding.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) EventBus.getDefault().post(new AddParticipantEvent(user));
                 else EventBus.getDefault().post(new RemoveParticipantEvent(user));

            });
        }
    }
}
