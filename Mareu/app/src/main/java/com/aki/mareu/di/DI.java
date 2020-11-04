package com.aki.mareu.di;

import com.aki.mareu.service.DummyReunionApiService;
import com.aki.mareu.service.ReunionApiService;

public class DI {

    private static ReunionApiService service = new DummyReunionApiService();

    public static ReunionApiService getReunionApiService() {
        return service;
    }


    public static ReunionApiService getNewInstanceApiService() {
        return new DummyReunionApiService();
    }
}
