package com.arinspect.randomfacts.rest;

import com.arinspect.randomfacts.rest.services.FactsService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String BASE_URL = "https://dl.dropboxusercontent.com/";
    private FactsService factsService;
    public RestClient() {
        Retrofit retrofit= new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        factsService= retrofit.create(FactsService.class);
    }

    public FactsService getFactsService() {
        return factsService;
    }
}
