package com.aki.mareu.service;

import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DummyReunionApiService implements ReunionApiService {

    /**
     * Generate a new ID
     */
    long newId = new Random().nextInt(1000);

    private List<Reunion> reunions = DummyGenerator.generateReunions();
    private List<User> users = DummyGenerator.generateUsers();
    private List<Room> rooms = DummyGenerator.generateRooms();

    public List<Reunion> filteredReunion;
    private Room filteringRoom = null;
    private String filteringDate = null;

    @Override
    public void setFilteringRoom(Room filteringRoom) {
        this.filteringRoom = filteringRoom;
    }

    @Override
    public void setFilteringDate(String filteringDate){
        this.filteringDate = filteringDate;
    }

    @Override
    public List filterByRoom(){
        filteredReunion = new ArrayList<>();
        for(Reunion r : getReunions()){
            if(r.getRoom() != filteringRoom.getId()){
                filteredReunion.add(r);
            }
        }
        return filteredReunion;
    }

    @Override
    public List filterByDate() {
        filteredReunion = new ArrayList<>();
        for(Reunion r : getReunions()){
            if(!r.getDate().equals(filteringDate)){
                filteredReunion.add(r);
            }
        }
        return filteredReunion;
    }

    @Override
    public long getNewId() {
        return newId;
    }

    @Override
    public List<Reunion> getReunions() {
        return reunions;
    }

    @Override
    public void deleteReunion(Reunion reunion) {
        reunions.remove(reunion);
    }

    @Override
    public void createReunion(Reunion reunion) {
        reunions.add(reunion);
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user);
    }

    @Override
    public void createUser(User user) {
        users.add(user);
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }
}
