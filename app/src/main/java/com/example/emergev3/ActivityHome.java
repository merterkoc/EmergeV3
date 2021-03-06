package com.example.emergev3;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityHome extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    Button logoutBtn;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferenceRead;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String uid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        firebaseAuth = FirebaseAuth.getInstance();

        uid = firebaseAuth.getCurrentUser().getUid();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

    }
    String eventTitle,eventDescription;
    Double latitude, longitude;
    // Double latitudeDouble, longitudeDouble;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        databaseReferenceRead = firebaseDatabase.getReference().child("Events");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    latitude = Double.parseDouble(dataSnapshot.child("event_latitude").getValue().toString());
                    longitude = Double.parseDouble(dataSnapshot.child("event_longitude").getValue().toString());
                    eventTitle = dataSnapshot.child("event_title").getValue().toString();
                    eventDescription = dataSnapshot.child("event_des").getValue().toString();
                    LatLng event = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions()
                            .snippet(eventDescription)
                            .alpha(0.7f)
                            .position(event).icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar_map))
                            .title(eventTitle));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(event));
                    googleMap.setMinZoomPreference(14.0f);
                    googleMap.setMaxZoomPreference(16.0f);
                    Log.e("TAG", "D??nd??");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        databaseReferenceRead.addValueEventListener(valueEventListener);

        /*LatLng olay = new LatLng(39.92801090675935, 32.9104688490613);
        map.addMarker(new MarkerOptions().position(olay).title("Olay"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(olay));
        googleMap.setMinZoomPreference(14.0f);
        googleMap.setMaxZoomPreference(16.0f);*/

    }

}