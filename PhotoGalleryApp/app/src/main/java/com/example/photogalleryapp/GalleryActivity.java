package com.example.photogalleryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<File> imageFiles;
    ImageAdapter imageAdapter;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_IMAGE_DETAILS = 1; // Request code for image details activity
    private String[] requiredPermissions = {Manifest.permission.READ_MEDIA_IMAGES};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Check if we have permission to read media images
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {

            // Request permission for Android 13+ (READ_MEDIA_IMAGES)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_CODE_PERMISSIONS);
            } else {
                // For older versions, use READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSIONS);
                } else {
                    loadImages();
                }
            }
        } else {
            loadImages();
        }
    }

    private void loadImages() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns grid

        File imagesDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PhotoGalleryApp");

        if (!imagesDir.exists() || imagesDir.listFiles() == null) {
            Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();
            return;
        }

        imageFiles = new ArrayList<>();
        File[] files = imagesDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    imageFiles.add(file);
                }
            }
        }

        if (imageFiles.isEmpty()) {
            Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();
        }

        imageAdapter = new ImageAdapter(this, imageFiles, file -> {
            Intent intent = new Intent(GalleryActivity.this, ImageDetailsActivity.class);
            intent.putExtra("imagePath", file.getAbsolutePath());
            startActivityForResult(intent, REQUEST_CODE_IMAGE_DETAILS);  // Start with request code
        });

        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the request code matches the permission request
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // If permission is granted, proceed with loading images
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load images
                loadImages();
            } else {
                // Permission denied, show a message or handle gracefully
                Toast.makeText(this, "Permission denied to read media images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_DETAILS && resultCode == RESULT_OK) {
            loadImages(); // Refresh the gallery after an image has been deleted
        }
    }
}
