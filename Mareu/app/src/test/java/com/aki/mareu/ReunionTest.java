package com.aki.mareu;

import com.aki.mareu.di.DI;
import com.aki.mareu.models.Reunion;
import com.aki.mareu.models.Room;
import com.aki.mareu.service.DummyGenerator;
import com.aki.mareu.service.ReunionApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ReunionTest {

    private ReunionApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getReunionListWithSuccess() {
        List<Reunion> reunions = service.getReunions();
        List<Reunion> expectedReunions = DummyGenerator.DUMMY_REUNIONS;
        assertTrue(reunions.containsAll(expectedReunions));
    }

    @Test
    public void deleteReunionWithSuccess() {
        Reunion reunionToDelete = service.getReunions().get(0);
        service.deleteReunion(reunionToDelete);
        assertFalse(service.getReunions().contains(reunionToDelete));
    }

    @Test
    public void addReunionWithSuccess() {
        List<Reunion> reunions = service.getReunions();
        List<Reunion> expectedReunions = DummyGenerator.DUMMY_REUNIONS;

        assertTrue(expectedReunions.containsAll(reunions));

        service.createReunion(new Reunion(666, "reunionTest", DummyGenerator.DUMMY_ROOMS.get(0), "13H37", "01/01/2021", 15, "Akimitsu", service.getUsers()));

        reunions = service.getReunions();
        assertFalse(expectedReunions.containsAll(reunions));
    }

    @Test
    public void filterReunionByDateWithSuccess() {
        List<Reunion> reunions = service.getReunions();
        List<Reunion> expectedReunions = DummyGenerator.DUMMY_REUNIONS;

        assertTrue(reunions.containsAll(expectedReunions));

        String filteringDate = "12/10/2020";
        service.setFilteringDate(filteringDate);
        reunions.removeAll(service.filterByDate());

        for (Reunion r : reunions) {
            assertTrue(r.getDate().equals(filteringDate));
        }
    }

    @Test
    public void filterReunionByRoomWithSuccess() {
        List<Reunion> reunions = service.getReunions();
        List<Reunion> expectedReunions = DummyGenerator.DUMMY_REUNIONS;

        assertTrue(reunions.containsAll(expectedReunions));

        Room filteringRoom = service.getRooms().get(2);
        service.setFilteringRoom(filteringRoom);
        reunions.removeAll(service.filterByRoom());

        for (Reunion r : reunions) {
            assertTrue(r.getRoom() == filteringRoom.getId());
        }
    }
}