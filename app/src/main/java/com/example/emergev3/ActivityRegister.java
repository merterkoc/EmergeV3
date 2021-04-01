package com.example.emergev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityRegister extends AppCompatActivity {
    EditText mName, mEmail, mPassword, mAgainPassword;
    Button mRegisterBtn, goLoginBtn;
    FirebaseAuth fAuth;

    private void init() {
        mName = findViewById(R.id.regName);
        mEmail = findViewById(R.id.regMail);
        mPassword = findViewById(R.id.regPassword);
        mAgainPassword = findViewById(R.id.regAgainPassword);
        mRegisterBtn = findViewById(R.id.registerBtn);
        fAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        //Kullanıcı giriş yapmış mı?
        if (fAuth.getCurrentUser() != null) {

            Intent intent = new Intent(ActivityRegister.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //Kullanıcı giriş yapmış mı?
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim(); //E mail'in textini alma
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email Boş!");
                    return;
                }

                if (!mPassword.getText().toString().equals(mAgainPassword.getText().toString())) {
                    mAgainPassword.setError("Parolalar eşleşmiyor.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Parola Boş!");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Parola 6 karakterden küçük!");
                    return;
                }
                //Kullanıcı oluşturma
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser fUser = fAuth.getCurrentUser();
                            fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ActivityRegister.this, "E mail onayı gönderildi!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Hata", "E mail onayı gönderilemedi!" + e.getMessage());
                                }
                            });
                            Toast.makeText(ActivityRegister.this, "Kullanıcı Oluşturuldu!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityRegister.this, ActivityHome.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ActivityRegister.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Kullanıcı oluşturma
            }
        });

    }
}