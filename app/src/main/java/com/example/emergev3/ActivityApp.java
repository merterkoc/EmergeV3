package com.example.emergev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        //Bottom navigation view
        BottomNavigationView btnNav = findViewById(R.id.bottomNavigationview);
        btnNav.setOnNavigationItemSelectedListener(navListener);
        //set home navigation
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, new HomeFragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.item1:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.item2:
                    selectedFragment = new FriendsFragment();
                    break;
                case R.id.item3:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.item4:
                    selectedFragment = new SettingsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout
                            , selectedFragment).commit();
            return true;
        }
    };
}