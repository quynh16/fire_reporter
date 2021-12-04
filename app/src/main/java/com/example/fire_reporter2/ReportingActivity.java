package com.example.fire_reporter2;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ReportingActivity extends AppCompatActivity {
    ImageView imageView;
    Button submitReport;
    BottomNavigationView navbar;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    String user_id;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        imageView = findViewById(R.id.image_view);
        submitReport = findViewById(R.id.submit_report);
        submitReport.setVisibility(View.INVISIBLE);
        navbar = findViewById(R.id.bottom_navbar);

        getUserID();
        openCamera();

        navbar.setSelectedItemId(R.id.reporting);
        navbar.setOnItemSelectedListener((@NonNull MenuItem item) -> {
            Intent intent;
            int id = item.getItemId();
            switch (id) {
                case R.id.home:
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.reporting:
                    intent = new Intent(getApplicationContext(), ReportingActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.map:
                    intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }

    public void openSomeActivityForResult() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        someActivityResultLauncher.launch(intent);
    }

    private void openCamera() {
        // request for camera permission
        if (ContextCompat.checkSelfPermission(ReportingActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReportingActivity.this, new String[] {
                    Manifest.permission.CAMERA
            }, CAMERA_PERMISSION_CODE);
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        // TODO: make it automatically open the rear camera
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityIfNeeded(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getUserID() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == AppCompatActivity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            submitReport.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(photo);
        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 100) {
                // get captured image
                Bitmap capturedImage = (Bitmap) data.getExtras().get("data");


                imageView.setImageBitmap(capturedImage);
                Log.w("ReportingActivity", "result not canceled");
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("id", user_id);
            startActivity(intent);
            overridePendingTransition(0,0);
            Log.w("ReportingActivity", "result canceled");
        }
    }
}