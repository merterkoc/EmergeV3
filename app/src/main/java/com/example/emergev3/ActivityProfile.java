package com.example.emergev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityProfile extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String nameView, userId = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button verifyButton = findViewById(R.id.emailGonder);


        TextView verifyMsg = findViewById(R.id.verifyMsg);

        if (!user.isEmailVerified()) {
            verifyMsg.setVisibility(View.VISIBLE);
            //resendCode.setVisibility(View.VISIBLE);

          /*  resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send verification link
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Do??rulama e postas?? g??nderili!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Data","OnFailture: Email not sent "+e.getMessage());
                        }
                    });
                }
            });*/

        }
       // Button resendCode = findViewById(R.id.resendCode);
        TextView textView = findViewById(R.id.textName);
        ImageView avatarImage = findViewById(R.id.avatarImage);
        databaseReference = firebaseDatabase.getReference().child("Users");
        ValueEventListener valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child(""+userId+"").child("name").getValue().toString();
               // String avatar = snapshot.child(""+userId+"").child("avatar").getValue().toString();
                textView.setText(name);
            //    avatarImage.setImageURI(Uri.parse(avatar));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(), "Do??rulama e postas?? g??nderili!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Data", "OnFailture: Email not sent " + e.getMessage());
                    }
                });
            }
        });
    }
}