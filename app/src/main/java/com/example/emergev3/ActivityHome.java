package com.example.emergev3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityHome extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    Button logoutBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuth = FirebaseAuth.getInstance();

        logoutBtn = findViewById(R.id.logout);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        //Çıkış yapma butonu
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ActivityHome.this, "Çıkış yapıldı!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityHome.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        /*LatLng olay = new LatLng(39.92801090675935, 32.9104688490613);
        map.addMarker(new MarkerOptions().position(olay).title("Olay"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(olay));
        googleMap.setMinZoomPreference(14.0f);
        googleMap.setMaxZoomPreference(16.0f);*/

    }

}