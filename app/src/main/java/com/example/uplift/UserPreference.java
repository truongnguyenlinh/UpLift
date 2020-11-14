package com.example.uplift;

import java.util.ArrayList;
import java.util.List;

public class UserPreference {

    public String name;
    public int frequency; // in minutes
    public List<String> categories;

    public UserPreference(){
    }


    // constructor when categories are unknown
    public UserPreference(String name){
        this.name = name;
        this.frequency = 60;
        this.categories = new ArrayList<>();
    }

    // constructor when categories are unknown
    public UserPreference(String name, int frequency){
        this.name = name;
        this.frequency = frequency;
        this.categories = new ArrayList<>();
    }

    // constructor with categories as param
    public UserPreference(String name, int frequency, List<String> categories){
        this.name = name;
        this.frequency = frequency;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void addCategory(String category) {
        this.categories.add(category);
    }

}

