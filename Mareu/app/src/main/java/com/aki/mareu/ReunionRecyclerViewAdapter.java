package com.aki.mareu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aki.mareu.databinding.FragmentReunionBinding;
import com.aki.mareu.events.DeleteReunionEvent;
import com.aki.mareu.events.GetReunionDetail;
import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class ReunionRecyclerViewAdapter extends RecyclerView.Adapter<ReunionRecyclerViewAdapter.ReunionViewHolder> {

    private final List<Reunion> mReunions;
    ViewGroup parents;
    Reunion reunion;

    public ReunionRecyclerViewAdapter(List<Reunion> items) {
        this.mReunions = items;
    }

    @Override
    public ReunionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentReunionBinding item = FragmentReunionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        parents = parent;
        return new ReunionViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ReunionViewHolder viewHolder, int position) {
        reunion = mReunions.get(position);
        viewHolder.bind(reunion);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new GetReunionDetail(mReunions.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReunions.size();
    }

    public class ReunionViewHolder extends RecyclerView.ViewHolder {

        Context context = parents.getContext();
        private FragmentReunionBinding binding;

        public ReunionViewHolder(FragmentReunionBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(final Reunion reunion) {
            ImageView drawable = binding.itemListPicture;

            List<User> participants = reunion.getParticipants();
            List<String> participantsMail = new ArrayList<>();

            if (participants != null) {
                for (User u : participants) {
                    participantsMail.add(u.getEmail());
                }
            }
            binding.itemListName.setText(reunion.getName());
            binding.itemListCreator.setText(reunion.getCreator());
            binding.itemListTime.setText(reunion.getHour());
            binding.itemListRoomNbr.setText("" + reunion.getRoom());
            binding.itemListParticipants.setText(participantsMail.toString().toLowerCase().substring(1, (participantsMail.toString().length() - 1)));
            switch ((int) reunion.getRoom()) {
                case 1:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom1)));
                    break;
                case 2:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom2)));
                    break;
                case 3:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom3)));
                    break;
                case 4:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom4)));
                    break;
                case 5:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom5)));
                    break;
                case 6:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom6)));
                    break;
                case 7:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom7)));
                    break;
                case 8:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom8)));
                    break;
                case 9:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom9)));
                    break;
                case 10:
                    ImageViewCompat.setImageTintList(drawable, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRoom10)));
            }
            binding.itemListDeleteButton.setOnClickListener(view -> EventBus.getDefault().post(new DeleteReunionEvent(reunion)));
        }
    }

}
