package com.aki.mareu;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.aki.mareu.databinding.FragmentParticipantsDetailBinding;
import com.aki.mareu.models.User;

import java.util.List;


public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ParticipantsViewHolder> {

    private final List<User> mUsers;
    ViewGroup parents;

    public DetailRecyclerViewAdapter(List<User> items) {
        this.mUsers = items;
    }

    @Override
    public ParticipantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentParticipantsDetailBinding item = FragmentParticipantsDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        parents = parent;
        return new ParticipantsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ParticipantsViewHolder viewHolder, int position) {
        User user = mUsers.get(position);
        viewHolder.bind(user);
    }

    @Override
    public int getItemCount() {
        if (mUsers != null)
            return mUsers.size();
        else return 0;
    }

    public class ParticipantsViewHolder extends RecyclerView.ViewHolder {

        private FragmentParticipantsDetailBinding binding;

        public ParticipantsViewHolder(FragmentParticipantsDetailBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(final User user) {
            binding.participantsDetailMail.setText(user.getEmail());
        }
    }
}
