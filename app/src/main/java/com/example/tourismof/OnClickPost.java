package com.example.tourismof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OnClickPost extends AppCompatActivity {

    private ImageView imageView;
    private TextView post_description, post_price, post_location;
    private String postKey;
    private DatabaseReference postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click_post);

        imageView = findViewById(R.id.click_post_image);
        post_description = findViewById(R.id.click_post_description);
        post_price = findViewById(R.id.clck_post_price);
        post_location = findViewById(R.id.click_post_location);

        postKey = getIntent().getExtras().get("postKey").toString();


        postKey = getIntent().getExtras().get("postkey").toString();
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("description").getValue().toString();
                String image = dataSnapshot.child("postimage").getValue().toString();
                String price = dataSnapshot.child("price").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();

                post_description.setText(description);
                post_price.setText(price);
                post_location.setText(location);
                Picasso.get().load(image).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
