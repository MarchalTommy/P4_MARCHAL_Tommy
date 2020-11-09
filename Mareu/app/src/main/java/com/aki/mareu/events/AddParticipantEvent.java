package com.aki.mareu.events;

import com.aki.mareu.models.User;

public class AddParticipantEvent {

    public User participant;

    public AddParticipantEvent(User user) { this.participant = user; }
}
