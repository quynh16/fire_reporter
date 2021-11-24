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

    private String user_name, user_email;
    private int user_id;

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
                intent.putExtra("id",1);
                intent.putExtra("name", "Elizabeth Olsen");
                intent.putExtra("email", "email@gmail.com");
                startActivity(intent);
            }
        });

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        navbar = findViewById(R.id.bottom_navbar);
        report_history = findViewById(R.id.report_history);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(USERS);

        // user information
        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        profilePicture = findViewById(R.id.profile_picture);
        editButton = (ImageButton)findViewById(R.id.edit_profile_btn);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                String abc = "hello";
                intent.putExtra("name", abc);
                startActivity(intent);
            }
        });

//        showAllUserData();

        // get all values from text fields

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
        String user_id = intent.getStringExtra("id");
        String user_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");

        name.setText(user_name);
        email.setText(user_email);
    }

    public void createReportCard() {
        View view = getLayoutInflater().inflate(R.layout.report_history_card, null);
        report_history.addView(view);
    }
}