package com.arinspect.randomfacts.rest.services;

import com.arinspect.randomfacts.entities.CountryFact;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FactsService {
    @GET("s/2iodh4vg0eortkl/facts.json")
    Call<CountryFact> getCountryFacts();
}
