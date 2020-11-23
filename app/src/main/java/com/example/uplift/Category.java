package com.example.uplift;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class Category {
    private String name;
//    private String parentCategory;
    private Category[] subCategories;
    private int imageResourceId;
    private boolean isSelected;

    private Category(String name, Category[] subCategories, int imageResourceId) {
        this.name = name;
        this.subCategories = subCategories;
        this.subCategories = null;
        this.imageResourceId = imageResourceId;
        this.isSelected = false;
    }

    public static final Category[] categories = {
            new Category("Animals",
                    new Category[] {
                            new Category("Cats", null, R.drawable.cat),
                            new Category("Dogs", null, R.drawable.dog)
                    },
                    R.drawable.animals),
            new Category("Nature", null, R.drawable.nature),
            new Category("Wellness", null, R.drawable.wellness4),
            new Category("Travel", null, R.drawable.travel3),
            new Category("Good News", null, R.drawable.happy_news2),
            new Category("Exercise", null,R.drawable.exercise)
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

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) { this.isSelected = isSelected; }

    public static Category[] getAllCategories() {
        return categories;
    }

    public static Category[] getSubCategories(String categoryName) {
        ArrayList<Category> categoryList = new ArrayList<>();
        categoryList.addAll(Arrays.asList(categories[0].subCategories));
        for (int i = 0; i < categories.length; i++ ) {
            String parentName = categories[i].getName();
            Log.d("DEBUG", "HI");
            if (parentName.equals(categoryName)) {
                categoryList.addAll(Arrays.asList(categories[i].subCategories));
                Log.d("DEBUG", "HELLO");
            }
        }

        Category[] subCategories = categoryList.toArray(new Category[categoryList.size()]);
        return subCategories;
    }


}
