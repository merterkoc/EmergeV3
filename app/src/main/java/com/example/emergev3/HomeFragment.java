package com.example.emergev3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap map;
    Button logoutBtn;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferenceRead;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String uid = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    String eventTitle,eventDescription;
    Double latitude, longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        return view;
    }


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
                    Log.e("TAG", "Döndü");
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