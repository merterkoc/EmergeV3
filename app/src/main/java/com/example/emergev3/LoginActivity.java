package com.example.emergev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    EditText mEmail, mPassword;
    Button mLoginBtn, mRegisterBtn;
    TextView mForgotPassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.logMail);
        mPassword = findViewById(R.id.logPassword);
        mLoginBtn = findViewById(R.id.logBtn);
        mForgotPassword = findViewById(R.id.forgotPassword);
        fAuth = FirebaseAuth.getInstance();
        //Kullancıı giriş yapmış mı?
//        if(fAuth.getCurrentUser()!= null){
//            Intent intent = new Intent(LoginActivity.this, ActivityHome.class);
//            startActivity(intent);
//            finish();
//        }
        //Kullancıı giriş yapmış mı?
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Boş!");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Parola Boş!");
                    return;
                }
                if(password.length()<6){
                    mPassword.setError("Parola 6 karakterden küçük!");
                    return;
                }
                //E mail ve parola ile giriş yapma
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String userId=fAuth.getCurrentUser().getUid();
                            Log.d(""+userId+"", "Giriş Yapıldı:success");

                            Toast.makeText(LoginActivity.this, "Giriş Yapıldı!" + userId, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, ActivityHome.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Log.d("Hata! ", "createUserWithEmail:Notsuccess");
                            Toast.makeText(LoginActivity.this, "Hatalı mail adresi veya parola! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                //E mail ve parola ile giriş yapma
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Yeni bir parola edin");
                passwordResetDialog.setMessage("Kayıtlı e mail adresinizi giriniz");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(resetMail.getText().toString().trim())) {
                            //extract the email and send reset link
                            String mail = resetMail.getText().toString();
                            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LoginActivity.this, "Sıfırlama linkiniz mail adersinize gönderildi ", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Gönderilemedi " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });

                passwordResetDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
    //Üye ol butonu
    public void signup(View view) {
        Intent intent = new Intent(LoginActivity.this,ActivityRegister.class);
        startActivity(intent);
    }
}