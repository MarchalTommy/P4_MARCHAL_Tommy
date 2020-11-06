package com.aki.mareu.events;

import com.aki.mareu.models.Reunion;

public class DeleteReunionEvent {

    public Reunion reunion;

    public DeleteReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
