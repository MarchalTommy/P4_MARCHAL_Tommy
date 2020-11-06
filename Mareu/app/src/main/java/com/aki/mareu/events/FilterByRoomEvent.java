package com.aki.mareu.events;

import com.aki.mareu.di.DI;
import com.aki.mareu.models.Room;
import com.aki.mareu.service.ReunionApiService;

public class FilterByRoomEvent {

    public Room room;
    private ReunionApiService mApiService = DI.getReunionApiService();

    public FilterByRoomEvent(String room) {
        for (Room r : mApiService.getRooms()) {
            if (r.getName().equals(room)) {
                this.room = r;
            }
        }
    }
}
