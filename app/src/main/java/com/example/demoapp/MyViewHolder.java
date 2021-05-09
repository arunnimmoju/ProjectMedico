package com.example.demoapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewHolder extends RecyclerView.ViewHolder {

    //ImageView imageView;
    TextView titletextView;
    TextView datetextView;
    View v;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        //imageView= itemView.findViewById(R.id.image_single_view);
        titletextView= itemView.findViewById(R.id.titleView_single_view);
        datetextView= itemView.findViewById(R.id.dateView_single_view);
        v=itemView;

    }
}
