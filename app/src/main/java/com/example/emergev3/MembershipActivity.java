package com.example.emergev3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class MembershipActivity extends AppCompatActivity {
        //Initialize variable
    MeowBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_login));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_register));
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                //Initilize fragment
                Fragment fragment = null;
                //Check condition
                switch (item.getId()){
                    case 1:
                        //When id is 1
                        //Initialize login fragment
                        fragment= new LoginFragment();
                        break;
                    case 2:
                        //When id is 2
                        //Initialize login fragment
                        fragment= new RegisterFragment();
                        break;
                }
                loadFragment(fragment);
            }
        });
        //Set nottification count
        //bottomNavigation.setCount(1,"10");
        bottomNavigation.show(2,true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext()
                ,"You clicked" + getTaskId()
                        ,Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Display toast
                Toast.makeText(getApplicationContext(),
                        "You resclacetd" + item.getId()
                        ,Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadFragment(Fragment fragment) {
        //Replace fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();

    }
}