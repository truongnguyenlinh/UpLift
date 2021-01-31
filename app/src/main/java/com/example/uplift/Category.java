package com.example.uplift;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class Category {
    private String name;
    private int imageResourceId;
    private boolean isSelected;
    private boolean isImage;



    private Category(String name, Boolean isImage, int imageResourceId) {
        this.name = name;
        this.isImage = isImage;
        this.imageResourceId = imageResourceId;
        this.isSelected = false;
    }

    public static final Category[] categories = {
            new Category("Animals", true, R.drawable.animals),
            new Category("Nature", true, R.drawable.nature),
            new Category("Wellness", false, R.drawable.wellness4),
            new Category("Travel", true, R.drawable.travel3),
            new Category("Good News", false, R.drawable.happy_news2),
            new Category("Exercise", false,R.drawable.exercise)
    };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return this.imageResourceId;
    }

    public boolean isImage() { return isImage;}

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) { this.isSelected = isSelected; }

    public static Category[] getAllCategories() {
        return categories;
    }


}
