package com.example.photogalleryapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import com.bumptech.glide.Glide;
import java.util.ArrayList;



import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public interface OnImageClickListener {
        void onImageClick(File file);
    }

    private final Context context;
    private final ArrayList<File> imageFiles;
    private final OnImageClickListener listener;

    public ImageAdapter(Context context, ArrayList<File> imageFiles, OnImageClickListener listener) {
        this.context = context;
        this.imageFiles = imageFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = imageFiles.get(position);
        Glide.with(context)
                .load(file)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> listener.onImageClick(file));
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
