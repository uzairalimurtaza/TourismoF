package com.example.tourismof.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tourismof.FirstActivity;
import com.example.tourismof.PostActivity;
import com.example.tourismof.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private Button button;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private CircleImageView profile_image;
    private TextView userName, FullName, Email, ph_no;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        String currentUser = mAuth.getCurrentUser().getUid();
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");
        profile_image = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.profile_name);
        FullName = view.findViewById(R.id.Full_Name);
        Email = view.findViewById(R.id.email);
        ph_no = view.findViewById(R.id.Phone_no);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendusertoPostActivity();
            }
        });

        mRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("fullname")){
                    String Full_name = dataSnapshot.child("fullname").getValue().toString();
                    FullName.setText(Full_name);
                }

                if(dataSnapshot.hasChild("username")){
                    String user_name = dataSnapshot.child("username").getValue().toString();
                    userName.setText(user_name);
                }

                if(dataSnapshot.hasChild("email")){
                    String useremail = dataSnapshot.child("email").getValue().toString();
                    Email.setText(useremail);
                }

                if(dataSnapshot.hasChild("ph_no")){
                    String phone = dataSnapshot.child("ph_no").getValue().toString();
                    ph_no.setText(phone);
                }

                if(dataSnapshot.hasChild("Profile")){

                    String ProfileImage = dataSnapshot.child("Profile").getValue().toString();
                    Picasso.get().load(ProfileImage).placeholder(R.drawable.profile).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void sendusertoPostActivity() {
        Intent intent = new Intent(getActivity(), PostActivity.class);
        startActivity(intent);
    }
}
