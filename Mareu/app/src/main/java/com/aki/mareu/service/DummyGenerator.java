package com.aki.mareu.service;

import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class DummyGenerator {

    public static List<Room> DUMMY_ROOMS = Arrays.asList(
            new Room(1, "Room 1", 10),
            new Room(2, "Room 2", 10),
            new Room(3, "Room 3", 10),
            new Room(4, "Room 4", 10),
            new Room(5, "Room 5", 10),
            new Room(6, "Room 6", 10),
            new Room(7, "Room 7", 10),
            new Room(8, "Room 8", 10),
            new Room(9, "Room 9", 10),
            new Room(10, "Room 10", 10)
    );

    public static List<User> DUMMY_USERS = Arrays.asList(
            new User(1, "Anthony", "Anthony@lamzon.com"),
            new User(2, "Linda", "Linda@lamzon.com"),
            new User(3, "Marcel", "Marcel@lamzon.com"),
            new User(4, "Elodie", "Elodie@lamzon.com"),
            new User(5, "Alfred", "Alfred@lamzon.com"),
            new User(6, "Léa", "Lea@lamzon.com"),
            new User(7, "George", "George@lamzon.com"),
            new User(8, "Ginette", "Ginette@lamzon.com"),
            new User(9, "Hubert", "Hubert@lamzon.com"),
            new User(10, "Jean", "Jean@lamzon.com"),
            new User(11, "Jack", "Jack@lamzon.com"),
            new User(12, "Vincent", "Vincent@lamzon.com"),
            new User(13, "Lena", "Lena@lamzon.com"),
            new User(14, "Aria", "Aria@lamzon.com"),
            new User(15, "Louise", "Louise@lamzon.com")
    );

    public static List<Reunion> DUMMY_REUNIONS = Arrays.asList(
            new Reunion(1, "Réunion A", DUMMY_ROOMS.get(0), "14H00", "12/10/2020", 3, "Hubert", (generateRandomParticipants(3))),
            new Reunion(2, "Réunion B", DUMMY_ROOMS.get(1), "11H00", "12/10/2020", 2, "Alfred", (generateRandomParticipants(2))),
            new Reunion(3, "Réunion C", DUMMY_ROOMS.get(2), "17H00", "13/10/2020", 7, "Linda", (generateRandomParticipants(7))),
            new Reunion(4, "Réunion D", DUMMY_ROOMS.get(3), "21H00", "13/10/2020", 1, "Elodie", (generateRandomParticipants(1))),
            new Reunion(5, "Réunion E", DUMMY_ROOMS.get(4), "17H00", "13/10/2020", 4, "Marcel", (generateRandomParticipants(4))),
            new Reunion(6, "Réunion F", DUMMY_ROOMS.get(2), "19H00", "13/10/2020", 2, "Ginette", (generateRandomParticipants(2))),
            new Reunion(7, "Réunion G", DUMMY_ROOMS.get(8), "14H00", "12/10/2020", 3, "George", (generateRandomParticipants(3))),
            new Reunion(8, "Réunion H", DUMMY_ROOMS.get(3), "11H00", "12/10/2020", 2, "Léa", (generateRandomParticipants(2))),
            new Reunion(9, "Réunion I", DUMMY_ROOMS.get(9), "17H00", "25/10/2020", 7, "Anthony", (generateRandomParticipants(7))),
            new Reunion(10, "Réunion J", DUMMY_ROOMS.get(7), "21H00", "25/10/2020", 1, "Elodie", (generateRandomParticipants(1))),
            new Reunion(11, "Réunion K", DUMMY_ROOMS.get(6), "17H00", "15/10/2020", 4, "Marcel", (generateRandomParticipants(4))),
            new Reunion(12, "Réunion L", DUMMY_ROOMS.get(2), "19H00", "15/10/2020", 2, "Ginette", (generateRandomParticipants(2)))
    );

    private static List<User> generateRandomParticipants(int n) {
        List<User> users = new LinkedList<>(generateUsers());
        List<User> randomParticipants = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Random random = new Random();
            int r = random.nextInt(15);
            if (!randomParticipants.contains(users.get(r))) {
                randomParticipants.add(users.get(r));
            } else {
                n++;
            }
        }
        return randomParticipants;
    }

    public static List<Reunion> generateReunions() {
        return new ArrayList<>(DUMMY_REUNIONS);
    }

    public static List<Room> generateRooms() {
        return new ArrayList<>(DUMMY_ROOMS);
    }

    public static List<User> generateUsers() {
        return new ArrayList<>(DUMMY_USERS);
    }


}
