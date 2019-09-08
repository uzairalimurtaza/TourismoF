package com.example.tourismof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class setupActivity extends AppCompatActivity {

    private EditText Username, FullName, Country;
    private Button savebutton;
    private CircleImageView profileImage;
//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
//        mAuth = FirebaseAuth.getInstance();

        Username = findViewById(R.id.setup_username);
        FullName = findViewById(R.id.setup_userFullName);
        Country = findViewById(R.id.setup_Coutry);
        savebutton = findViewById(R.id.save_button);
        profileImage = findViewById(R.id.profile_image);
    }

//        savebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                mAuth.signOut();
////                sedUserToLoginActivity();
//            }
//        });
//    }
//
//    private void sedUserToLoginActivity() {
//        Intent loginIntent = new Intent(setupActivity.this,LoginActivity.class);
//        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(loginIntent);
//        finish();
//    }
}
