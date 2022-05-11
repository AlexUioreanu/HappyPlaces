package com.example.happyplaces.retrofit;

import com.google.gson.internal.LinkedTreeMap;

public class Weather {

    private LinkedTreeMap main;

    private LinkedTreeMap wind;

    private LinkedTreeMap clouds;

    public int getCurrentTemperature() {
        return (int) Math.ceil((double) main.get("temp"));
    }

}
