package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewReportActivity extends AppCompatActivity {

    private ImageView imageView;
    TextView titletextView;
    TextView datetextView;
    Button btnDelete;
    DatabaseReference ref,DataRef;
    StorageReference StorageRef;
    boolean isImageFitToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        imageView = findViewById(R.id.image_single_view_activity);
        titletextView = findViewById(R.id.titleView_activity);
        datetextView = findViewById(R.id.dateView_activity);
        btnDelete = findViewById(R.id.btnDelete);
        ref = FirebaseDatabase.getInstance().getReference().child("Report");
        String ReportKey = getIntent().getStringExtra("ReportKey");
        DataRef = FirebaseDatabase.getInstance().getReference().child("Report").child(ReportKey);
        StorageRef = FirebaseStorage.getInstance().getReference().child("ReportImage").child(ReportKey+".jpg");

        ref.child(ReportKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   String reportTitle = dataSnapshot.child("title").getValue().toString();
                   String reportDate = dataSnapshot.child("date").getValue().toString();
                   String ImageUrl = dataSnapshot.child("ImageUrl").getValue().toString();

                   Picasso.get().load(ImageUrl).into(imageView);
                   titletextView.setText(reportTitle);
                   datetextView.setText(reportDate);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                            StorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startActivity(new Intent(getApplicationContext(),ReportListActivity.class));
                                }
                            });
                    }
                });

            }
        });

    }
}