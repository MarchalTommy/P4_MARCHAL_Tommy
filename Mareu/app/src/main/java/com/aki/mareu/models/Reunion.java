package com.aki.mareu.models;

import com.aki.mareu.di.DI;
import com.aki.mareu.service.ReunionApiService;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Reunion implements Serializable {

    ReunionApiService mApiService = DI.getReunionApiService();
    /**
     * Identifier
     */
    private long id;
    /**
     * Name of the reunion
     */
    private String name;
    /**
     * Room where it is
     */
    private Room room;
    /**
     * Time when the reunion will take place
     */
    private String hour;
    /**
     * Date where the reunion will take place
     */
    private String date;
    /**
     * Number of person participating
     */
    private int nbrParticipating;
    /**
     * Creator of the reunion
     */
    private String creator;
    private List<User> participants;


    /**
     * Constructor
     *
     * @param id
     * @param name
     * @param room
     */

    public Reunion(long id, String name, Room room, String hour, String date,
                   int nbrParticipating, String creator, List<User> participants) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.hour = hour;
        this.date = date;
        this.nbrParticipating = nbrParticipating;
        this.creator = creator;
        this.participants = participants;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRoom() {
        return room.getId();
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public String getHour() {
        return hour;
    }

    public void setTime(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNbrParticipating() {
        return nbrParticipating;
    }

    public void setNbrParticipating(int nbrParticipating) {
        this.nbrParticipating = nbrParticipating;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reunion reunion = (Reunion) o;
        return Objects.equals(id, reunion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}