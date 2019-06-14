package com.arinspect.randomfacts.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryFact {

    private String title;

    @SerializedName("rows")
    private List<Fact> facts;

    public String getTitle() {
        return title;
    }

    public List<Fact> getFacts() {
        return facts;
    }
}
