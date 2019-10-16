package com.example.tourismof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickPost extends AppCompatActivity {

    private TextView text1, text2, text3,text4;
    private ImageView imageView;
    private String image, price, description, location,postKey,title;
    private DatabaseReference postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        text1 = findViewById(R.id.location_post);
        text2 = findViewById(R.id.price_post);
        text3 = findViewById(R.id.description_post);
        text4 = findViewById(R.id.title_post);
        imageView = findViewById(R.id.image_post);

        postKey = getIntent().getExtras().get("postKey").toString();

        Toast.makeText(this, postKey, Toast.LENGTH_SHORT).show();



        postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey);

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    description = dataSnapshot.child("description").getValue().toString();
                    price = dataSnapshot.child("price").getValue().toString();
                    location = dataSnapshot.child("location").getValue().toString();
                    image = dataSnapshot.child("postimage").getValue().toString();
                    title = dataSnapshot.child("title").getValue().toString();

                    text1.setText(location);
                    text2.setText(price);
                    text3.setText(description);
                    text4.setText(title);
                    Picasso.get().load(image).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
