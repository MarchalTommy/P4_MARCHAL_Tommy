package com.aki.mareu.events;

import com.aki.mareu.models.User;

public class RemoveParticipantEvent {

    public User participant;

    public RemoveParticipantEvent(User user) { this.participant = user; }
}
