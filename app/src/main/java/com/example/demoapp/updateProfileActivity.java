package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demoapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class updateProfileActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextInputEditText uName;
    AutoCompleteTextView uGender;
    TextInputEditText uAge;
    TextInputEditText uHeight;
    TextInputEditText uWeight;
    TextInputEditText uOccupation;
    AutoCompleteTextView uBloodPressure;
    TextInputEditText uNumber;
    Button btnUpdateDetails;

    FirebaseFirestore fstore;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    String userId;

    ImageView updateProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        uName = findViewById(R.id.updateName);
        uAge = findViewById(R.id.updateAge);
        uHeight = findViewById(R.id.updateHeight);
        uWeight = findViewById(R.id.updateWeight);
        uOccupation = findViewById(R.id.updateOccupation);
        uNumber = findViewById(R.id.updatePhone);
        btnUpdateDetails = findViewById(R.id.updateDetails);
        uGender = findViewById(R.id.updateGender);
        uBloodPressure = findViewById(R.id.updateBp);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        String[] gender = new String[]{"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, gender);
        uGender.setAdapter(adapter);

        String[] bloodPressure = new String[]{"Low", "High", "Normal"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, bloodPressure);
        uBloodPressure.setAdapter(adapter1);

        btnUpdateDetails.setOnClickListener(v -> {
            String fName = uName.getText().toString();
            String fAge = uAge.getText().toString();
            String fHeight = uHeight.getText().toString();
            String fWeight = uWeight.getText().toString();
            String fOccupation = uOccupation.getText().toString();
            String fNumber = uNumber.getText().toString();
            String fGender = uGender.getText().toString();
            String fBp = uBloodPressure.getText().toString();

            if(TextUtils.isEmpty(fName)){
                uName.setError("User Name is Required");
                return;
            }
            if(TextUtils.isEmpty(fGender)){
                uGender.setError("Gender is Required");
                return;
            }
            if(TextUtils.isEmpty(fAge)){
                uAge.setError("Age is Required");
                return;
            }
            if(TextUtils.isEmpty(fHeight)){
                uHeight.setError("Height is Required");
                return;
            }

            if(TextUtils.isEmpty(fWeight)){
                uWeight.setError("Weight is Required");
                return;
            }
            if(TextUtils.isEmpty(fBp)){
                uBloodPressure.setError("BP is Required");
                return;
            }
            if(TextUtils.isEmpty(fNumber)){
                uNumber.setError("Number is Required");
                return;
            }
            if(TextUtils.isEmpty(fOccupation)){
                uOccupation.setError("Occupation is Required");
                return;
            }

            userId = firebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fstore.collection("User").document(userId);

            Map<String,Object> user= new HashMap<>();
            user.put("Name",fName);
            user.put("Gender",fGender);
            user.put("Age",fAge);
            user.put("Height",fHeight);
            user.put("Weight",fWeight);
            user.put("BP",fBp);
            user.put("Contact",fNumber);
            user.put("Occupation",fOccupation);

            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "onSuccess: User Data is stored Successfully "+ userId );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: "+ e.toString());
                }
            });
            startActivity(new Intent(getApplicationContext(),HealthReportActivity.class));
        });


        updateProfileImage = findViewById(R.id.updateImage);

        updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery;
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                startActivityForResult(openGallery,1000);
            }
        });

        StorageReference profileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(updateProfileImage);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                updateProfileImage.setImageURI(imageUri);
                uploadImageToDatabase(imageUri);

            }
        }
    }

    private void uploadImageToDatabase(Uri imageUri) {
        //uploading image to firebase;
        StorageReference fileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(updateProfileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(updateProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}