package com.example.emergev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConnectActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String uId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        uId = firebaseUser.getUid();


        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(uId);
                HashMap<String, String> eventMap = new HashMap<>();
                eventMap.put("user_id", "" + uId + "");
                eventMap.put("event_title", "Title");
                eventMap.put("event_des", "Description here!");
                eventMap.put("event_img", "defould");
                eventMap.put("event_latitude", "39.92866217381201");
                eventMap.put("event_longitude", "32.92951071973272");
                eventMap.put("event_date", "date");
                databaseReference.setValue(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ConnectActivity.this, "Event Eklendi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, ActivityProfile.class);
                startActivity(intent);
            }
        });


        Button home = findViewById(R.id.buttonHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, ActivityHome.class);
                startActivity(intent);
            }
        });

        //Çıkış yapma butonu
        Button logoutBtn = findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ConnectActivity.this, "Çıkış yapıldı!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConnectActivity.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });

        Button button3 = findViewById(R.id.activityApp);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, ActivityApp.class);
                startActivity(intent);
            }
        });

    }
}