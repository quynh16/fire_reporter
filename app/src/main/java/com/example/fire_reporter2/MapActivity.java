package com.example.fire_reporter2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MapActivity extends AppCompatActivity {
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setVisibility(View.GONE);

        BottomNavigationView navbar = findViewById(R.id.bottom_navbar);
        navbar.setSelectedItemId(R.id.map);

        navbar.setOnItemSelectedListener((@NonNull MenuItem item) -> {
            Intent intent;
            int id = item.getItemId();
            switch (id){
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
}