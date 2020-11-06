package com.aki.mareu.events;

import com.aki.mareu.models.Reunion;

public class AddReunionEvent {

    public Reunion reunion;

    public AddReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
