package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demoapp.model.User;
import com.example.demoapp.util.CustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    Button loginBtn1;
    Button signupBtn1;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        emailId = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        signupBtn1 = findViewById(R.id.btnSignUp1);
        loginBtn1 = findViewById(R.id.btnLogin1);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    moveToHomeActivity(mFirebaseUser);
                }else
                {
                    CustomToast.createToast(LoginActivity.this,"Please Login",false);
                }
                firebaseAuth.addAuthStateListener(mAuthStateListener);
            }
        };

        loginBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();


                if (email.isEmpty()) {
                    emailId.setError("Please Provide Email");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please Provide Password");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(LoginActivity.this,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(LoginActivity.this, "Login Error, Try Again", Toast.LENGTH_SHORT).show();
                                            } else {
                                                moveToHomeActivity(task.getResult().getUser());
                                            }
                                        }
                                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                }

            }
        });

        signupBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void moveToHomeActivity(FirebaseUser mFirebaseUser) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String name = user.getU_name();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("name", name);
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}