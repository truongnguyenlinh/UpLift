package com.example.uplift;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private String[] categoryNames;
    private int[] imageIds;

    public CategoriesAdapter(String[] categoryNames, int[] imageIds) {
        this.categoryNames = categoryNames;
        this.imageIds = imageIds;
    }

    @Override
    public int getItemCount() {
        return categoryNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_image_layout, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final CardView cardView = holder.cardView;

        ImageView ivCategoryImage = cardView.findViewById(R.id.category_image);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(),
                imageIds[position]);
        ivCategoryImage.setImageDrawable(drawable);
        ivCategoryImage.setContentDescription(categoryNames[position]);

        TextView textView = cardView.findViewById(R.id.category_name);
        textView.setText(categoryNames[position]);

        ImageView ivCheckMark = cardView.findViewById(R.id.check_mark);
        ivCheckMark.setVisibility(View.INVISIBLE);

        cardView.setOnClickListener(new View.OnClickListener() {
            public Boolean selected = false;
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    selected = !selected;
                    listener.onClick(view, selected, categoryNames[position]);
                }
            }
        });

}

    private Listener listener;
    interface Listener {
        void onClick(View item, Boolean selected, String categoryName);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

}

