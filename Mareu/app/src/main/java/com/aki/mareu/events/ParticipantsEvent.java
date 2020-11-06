package com.aki.mareu.events;

import com.aki.mareu.models.User;

import java.util.List;

public class ParticipantsEvent {

    public List<User> participants;


    public ParticipantsEvent(List<User> participant) {
        this.participants = participant;
    }
}
