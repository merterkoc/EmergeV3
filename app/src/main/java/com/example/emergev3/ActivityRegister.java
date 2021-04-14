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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;

public class ActivityRegister extends AppCompatActivity {
    EditText mName, mEmail, mPassword, mAgainPassword, mPhone;
    Button mRegisterBtn, goLoginBtn, mGoogleRegisterBtn;
    FirebaseAuth fAuth;
    DatabaseReference databaseReference;
    String userId = null;
    private GoogleSignInClient mGoogleSignInClient;
    public final static int RC_SIGN_IN = 123;

    @Override
    protected void onStart() {
        super.onStart();
        //Kullanıcı giriş yapmış mı?
        if (fAuth.getCurrentUser() != null) {

            Intent intent = new Intent(ActivityRegister.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //Kullanıcı giriş yapmış mı?
    }

    private void init() {
        mName = findViewById(R.id.regName);
        mEmail = findViewById(R.id.regMail);
        mPassword = findViewById(R.id.regPassword);
        mAgainPassword = findViewById(R.id.regAgainPassword);
        mPhone = findViewById(R.id.phoneNumber);
        mRegisterBtn = findViewById(R.id.registerBtn);
        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mGoogleRegisterBtn = findViewById(R.id.regGoogleBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim(); //email'in textini al
                String password = mPassword.getText().toString().trim();//password'ün textini al
                String name = mName.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
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

                            userId = fUser.getUid();//kullanıcının idsini al
                            databaseReference = databaseReference.child("Users").child(userId);
                            //user idsi ile users tablosuna kullanıcının adını alacağız
                            HashMap<String, String> tableUser = new HashMap<>();
                            tableUser.put("email", email);
                            tableUser.put("name", name);
                            tableUser.put("phone", phone);
                            tableUser.put("profileImg", "gs://emerge-d43ae.appspot.com/avatar.png");
                            tableUser.put("status", "online");
                            databaseReference.setValue(tableUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        Toast.makeText(ActivityRegister.this, "Veri Tabanına Ekleme İşlemi Başarılı!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Intent intent = new Intent(ActivityRegister.this, ConnectActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ActivityRegister.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //Kullanıcı oluşturma
                // Configure Google Sign In
                createRequest();
            }
        });
        mGoogleRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void createRequest() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = fAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }


    public void sigIn(View view) {
        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
        startActivity(intent);
        finish();
    }
}