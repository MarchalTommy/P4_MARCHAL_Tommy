package com.aki.mareu.events;

/**
 * Event fired when a user deletes a Neighbour
 */
public class FilterByDateEvent {

    public String date;

    public FilterByDateEvent(String date) {
        this.date=date;
    }
}
