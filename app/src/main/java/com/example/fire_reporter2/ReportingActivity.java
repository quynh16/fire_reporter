package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ReportingActivity extends AppCompatActivity {

    ImageView imageView;
    Button openCamera;
    Button submitReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        imageView = findViewById(R.id.image_view);
        openCamera = findViewById(R.id.open_camera);
        openCamera.setText("OPEN CAMERA");
        submitReport = findViewById(R.id.submit_report);
        submitReport.setVisibility(View.INVISIBLE);

        BottomNavigationView navbar = findViewById(R.id.bottom_navbar);
        navbar.setSelectedItemId(R.id.home);

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.reporting:
                        startActivity(new Intent(getApplicationContext(), ReportingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        // request for camera permission
        if (ContextCompat.checkSelfPermission(ReportingActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReportingActivity.this,
                    new String[] {
                            Manifest.permission.CAMERA
                    },
                    100);
        }

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                // TODO: make it automatically open the rear camera
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityIfNeeded(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // get captured image
            Bitmap capturedImage = (Bitmap) data.getExtras().get("data");

            submitReport.setVisibility(View.VISIBLE);
            openCamera.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(capturedImage);
            openCamera.setText("RETAKE IMAGE");
        }
    }
}