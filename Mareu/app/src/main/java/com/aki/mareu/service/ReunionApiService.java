package com.aki.mareu.service;

import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.models.User;

import java.util.List;

public interface ReunionApiService {

    List<Reunion> filterByRoom(Room room);

    List<Reunion> filterByDate(String date);

    long getNewId();

    List<Reunion> getReunions();

    void deleteReunion(Reunion reunion);

    void createReunion(Reunion reunion);

    List<User> getUsers();

    void deleteUser(User user);

    void createUser(User user);

    List<Room> getRooms();
}
