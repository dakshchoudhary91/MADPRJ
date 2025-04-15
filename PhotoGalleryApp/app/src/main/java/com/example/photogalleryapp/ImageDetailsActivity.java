package com.example.photogalleryapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class ImageDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewName, textViewPath, textViewSize, textViewDate;
    private Button btnDelete;
    private File imageFile;
    private static final String TAG = "ImageDetailsActivity"; // For logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);

        imageView = findViewById(R.id.imageView);
        textViewName = findViewById(R.id.textViewName);
        textViewPath = findViewById(R.id.textViewPath);
        textViewSize = findViewById(R.id.textViewSize);
        textViewDate = findViewById(R.id.textViewDate);
        btnDelete = findViewById(R.id.btnDelete);

        String imagePath = getIntent().getStringExtra("imagePath");

        // Log the received image path
        Log.d(TAG, "Received image path: " + imagePath);

        if (imagePath == null || imagePath.isEmpty()) {
            Toast.makeText(this, "Invalid image path", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Image path is null or empty");
            finish(); // Close the activity
            return;
        }

        imageFile = new File(imagePath);

        // Log file details
        Log.d(TAG, "Image file exists: " + imageFile.exists() + ", Path: " + imageFile.getAbsolutePath());

        if (!imageFile.exists()) {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Image file does not exist");
            finish(); // Close the activity
            return;
        }

        // Use Glide to load the image into the ImageView
        try {
            Glide.with(this).load(imageFile).into(imageView);
        } catch (Exception e) {
            Log.e(TAG, "Error loading image with Glide: " + e.getMessage());
            e.printStackTrace();
        }

        // Set the image details in the TextViews
        textViewName.setText("Name: " + imageFile.getName());
        textViewPath.setText("Path: " + imageFile.getAbsolutePath());
        textViewSize.setText("Size: " + imageFile.length() / 1024 + " KB");

        long lastModified = imageFile.lastModified();
        textViewDate.setText("Date Taken: " + new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(lastModified)));

        // Delete image on button click
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Image")
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (imageFile.delete()) {
                            Toast.makeText(ImageDetailsActivity.this, "Image deleted!", Toast.LENGTH_SHORT).show();
                            // Notify the GalleryActivity to refresh
                            setResult(RESULT_OK);
                            finish(); // Close the activity and go back to gallery
                        } else {
                            Toast.makeText(ImageDetailsActivity.this, "Failed to delete image.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
