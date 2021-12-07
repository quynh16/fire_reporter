package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView navbar;
    LinearLayout report_history;
    FirebaseDatabase database;
    DatabaseReference reference;

    private ImageView profilePicture;
    private TextView name, email, password;
    private ImageButton editButton;

    private String user_id, user_name, user_email;

    private static final String USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton editBtn = findViewById(R.id.edit_profile_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("id",user_id);
                intent.putExtra("name", user_name);
                intent.putExtra("email", user_email);
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        navbar = findViewById(R.id.bottom_navbar);
        report_history = findViewById(R.id.report_history);

        // user information
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        profilePicture = findViewById(R.id.profile_picture);
        editButton = (ImageButton)findViewById(R.id.edit_profile_btn);


        database = FirebaseDatabase.getInstance();
        reference = database.getReference(USERS);

        showAllUserData();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                String abc = "hello";
                intent.putExtra("name", user_name);
                intent.putExtra("email", user_email);
                intent.putExtra("id", user_id);
                startActivity(intent);
            }
        });

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
        for (int i = 0; i < 5; i++) {
            createReportCard();
        }
    }

    private void showAllUserData() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");
//        user_name = intent.getStringExtra("name");
//        user_email = intent.getStringExtra("email");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
//                    if (ds.getKey().equals(user_id)) {
//                        user_name = ds.child("name").getValue(String.class);
//                        user_email = ds.child("email").getValue(String.class);
//                    }
                    user_name = ds.getKey().toString();
                    user_email = ds.child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name.setText(user_name);
        email.setText(user_email);
    }

    public void createReportCard() {
        View view = getLayoutInflater().inflate(R.layout.report_history_card, null);
        report_history.addView(view);
    }
}