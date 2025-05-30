package com.example.ecoplant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecoplant.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<String> imageList;

    public ImageAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imageList.get(position);

        android.util.Log.d("ImageAdapter", "onBindViewHolder imagePath = " + imagePath);

        if (!imagePath.contains("/") && !imagePath.contains("storage")) {
            int resId = holder.imageView.getContext().getResources()
                    .getIdentifier(imagePath, "drawable", holder.imageView.getContext().getPackageName());
            android.util.Log.d("ImageAdapter", "Drawable resource id = " + resId);
            Glide.with(holder.imageView.getContext())
                    .load(resId)
                    .into(holder.imageView);
        } else {
            Glide.with(holder.imageView.getContext())
                    .load(imagePath)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ImageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imagePhoto);
        }
    }
}
