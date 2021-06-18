package com.example.demoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HealthReportActivity extends AppCompatActivity {

    TextView userName;
    TextView userGender;
    TextView userAge;
    TextView userHeight;
    TextView userWeight;
    TextView userNumber;
    TextView userOccupation;
    TextView userBp;
    Button updateProfile;
    ImageView profileImage;

    FirebaseFirestore fStore;
    String userId;
    FirebaseAuth fAuth;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report);

        updateProfile = findViewById(R.id.editProfile);
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HealthReportActivity.this, updateProfileActivity.class);
                startActivity(intent);
            }
        });

        userName = findViewById(R.id.viewName);
        userGender = findViewById(R.id.viewGender);
        userAge = findViewById(R.id.viewAge);
        userHeight = findViewById(R.id.viewHeight);
        userWeight = findViewById(R.id.viewWeight);
        userBp = findViewById(R.id.viewBp);
        userNumber = findViewById(R.id.viewPhone);
        userOccupation = findViewById(R.id.viewOccupation);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = fAuth.getCurrentUser().getUid();
        profileImage = findViewById(R.id.userImage);
        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        DocumentReference documentReference = fStore.collection("User").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                userName.setText(documentSnapshot.getString("Name"));
                userGender.setText(documentSnapshot.getString("Gender"));
                userAge.setText(documentSnapshot.getString("Age"));
                userHeight.setText(documentSnapshot.getString("Height"));
                userWeight.setText(documentSnapshot.getString("Weight"));
                userBp.setText(documentSnapshot.getString("BP"));
                userNumber.setText(documentSnapshot.getString("Contact"));
                userOccupation.setText(documentSnapshot.getString("Occupation"));
            }
        });
    }


}