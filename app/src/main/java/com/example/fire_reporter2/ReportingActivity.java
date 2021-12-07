package com.example.fire_reporter2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fire_reporter2.ml.FireModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ReportingActivity extends AppCompatActivity {
    private static final String TAG = "ReportingActivity";
    ImageView imageView;
    Button submitReport;
    Bitmap photo;
    BottomNavigationView navbar;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    private FusedLocationProviderClient fusedLocationClient;

    String user_id;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting);

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        imageView = findViewById(R.id.image_view);
        submitReport = findViewById(R.id.submit_report);
        navbar = findViewById(R.id.bottom_navbar);

        submitReport.setVisibility(View.INVISIBLE);

        getUserID();

        submitReport.setOnClickListener((View v) -> {
            SendEmailService.getInstance(getApplicationContext()).emailExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        FireModel model = FireModel.newInstance(getBaseContext());

                        // Creates inputs for reference.
                        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
                        inputFeature0.loadBuffer(convertBitmapToByteBuffer(photo));

                        // Runs model inference and gets result.
                        FireModel.Outputs outputs = model.process(inputFeature0);
                        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                        Log.i("PicO", "" + outputFeature0.getFloatArray()[0]);

                        if(outputFeature0.getFloatArray()[0] < .5)
                            SendEmailService.getInstance(getApplicationContext()).SendEmail(photo);
                        // Releases model resources if no longer used.
                        model.close();
                    } catch (IOException e) {
                        // TODO Handle the exception
                        Log.e("Pic0", e.getMessage());
                    }

                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
                }
            });
        });

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

        openCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(cameraIntent);
            } else {
                Log.w(TAG, "camera permission denied");
                Toast.makeText(this, "Camera access not enabled", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("id", user_id);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        }
    }

    private void updateUserDB(String report_id, String date) {
        DatabaseReference usersRef = database.getReference("users");
        usersRef.child(user_id).child("reports").child(report_id).setValue(date);
    }

    private void updateDB() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("reports");
        DatabaseReference reportRef = reference.push();
        String report_id = reportRef.getKey();
        DateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Report report = new Report(report_id, user_id, "Reported", date);
        reportRef.setValue(report);
        updateUserDB(report_id, date);
    }

    private void uploadImage(String report_id) {
        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("reports");
        StorageReference imageRef = storageRef.child(report_id);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ReportingActivity.this, "Failed to upload image", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ReportingActivity.this, "Uploaded image", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void openCamera() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    photo = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(photo);
                    submitReport.setVisibility(View.VISIBLE);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            Log.w(TAG, "Requesting camera permissions");
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncher.launch(cameraIntent);
        }
    }

    private void getUserID() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer;
        byteBuffer = ByteBuffer.allocateDirect(
                4 * 256 * 256 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[256 * 256];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < 256; ++i) {
            for (int j = 0; j < 256; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat(
                        (((val >> 16) & 0xFF))/255);
                byteBuffer.putFloat(
                        (((val >> 8) & 0xFF))/255);
                byteBuffer.putFloat((((val) & 0xFF))/255);
            }
        }
        return byteBuffer;
    }
}