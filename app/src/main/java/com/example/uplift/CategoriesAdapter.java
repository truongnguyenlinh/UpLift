package com.example.uplift;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private String[] categoryNames;
    private int[] imageIds;
    int mExpandedPosition = -1;

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
//        super.onBindViewHolder(holder, position, payloads);

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



//        final boolean isExpanded = position==mExpandedPosition;
//        cardView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
//        cardView.setActivated(isExpanded);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mExpandedPosition = isExpanded ? -1 : position;
//                TransitionManager.beginDelayedTransition(cardView);
//                notifyDataSetChanged();
//            }
//        });


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

