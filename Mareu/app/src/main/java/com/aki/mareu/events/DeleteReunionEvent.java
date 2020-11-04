package com.aki.mareu.events;

import com.aki.mareu.models.Reunion;

/**
 * Event fired when a user deletes a Neighbour
 */
public class DeleteReunionEvent {

    /**
     * Neighbour to delete
     */
    public Reunion reunion;

    public DeleteReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
