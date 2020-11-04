package com.aki.mareu.events;

import com.aki.mareu.models.User;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsEvent {

    /**
     * Neighbour to delete
     */
    public List<User> participants = new ArrayList<>();


    public ParticipantsEvent(List<User> participant) {
        this.participants = participant;
    }
}
