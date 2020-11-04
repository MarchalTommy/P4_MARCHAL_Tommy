package com.aki.mareu.service;

import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.models.User;

import java.util.List;

public interface ReunionApiService {

    void setFilteringRoom(Room filteringRoom);

    void setFilteringDate(String filteringDate);

    List<Reunion> filterByRoom();

    List<Reunion> filterByDate();

    long getNewId();

    List<Reunion> getReunions();

    void deleteReunion(Reunion reunion);

    void createReunion(Reunion reunion);

    List<User> getUsers();

    void deleteUser(User user);

    void createUser(User user);

    List<Room> getRooms();
}
