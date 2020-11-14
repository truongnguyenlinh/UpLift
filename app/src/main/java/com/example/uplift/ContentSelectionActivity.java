package com.example.uplift;

import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Arrays;

public class ContentSelectionActivity extends AppCompatActivity {

    RecyclerView categoryRecycler;
    RecyclerView subCategoryRecycler;

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_content_selection);

        categoryRecycler = findViewById(R.id.category_recycler);

        Category[] categories = Category.getAllCategories();

        String[] categoryNames = new String[categories.length];
        int[] categoryImages = new int[categories.length];

        for (int i = 0; i < categories.length; i ++) {
            categoryNames[i] = categories[i].getName();
            categoryImages[i] = categories[i].getImageResourceId();
        }

        CategoriesAdapter adapter = new CategoriesAdapter(categoryNames, categoryImages);
        categoryRecycler.setAdapter(adapter);

        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        categoryRecycler.setLayoutManager(lm);

        adapter.setListener(new CategoriesAdapter.Listener() {
            @Override
            public void onClick(View categoryView, Boolean selected, String categoryName) {
                ImageView ivCategory = categoryView.findViewById(R.id.category_image);
                TextView tvCategory = categoryView.findViewById(R.id.category_name);
                ImageView ivCheckMark = categoryView.findViewById(R.id.check_mark);

                if (selected) {
                    // Main category
                    ivCategory.setColorFilter(Color.argb(150, 0, 0, 0));
                    tvCategory.setTextColor(Color.argb(150, 255, 255, 255));
                    ivCheckMark.setVisibility(View.VISIBLE);

                } else {
                    ivCategory.setColorFilter(Color.argb(0, 0, 0, 0));
                    tvCategory.setTextColor(Color.argb(255, 255, 255, 255));
                    ivCheckMark.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

}
