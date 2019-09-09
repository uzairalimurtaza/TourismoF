package com.example.tourismof;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;




public class setupActivity extends AppCompatActivity
{
    private EditText UserName, FullName, CountryName;
    private Button SaveInformationbuttion;
    private CircleImageView ProfileImage;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    String currentUserID;
    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        UserName = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_userFullName);
        CountryName = (EditText) findViewById(R.id.setup_Coutry);
        SaveInformationbuttion = (Button) findViewById(R.id.save_button);
        ProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        loadingBar = new ProgressDialog(this);


        SaveInformationbuttion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SaveAccountSetupInformation();
            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });


        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(setupActivity.this).load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    else
                    {
                        Toast.makeText(setupActivity.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(setupActivity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();

                            UsersRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Intent selfIntent = new Intent(setupActivity.this, setupActivity.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(setupActivity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(setupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Error Occured: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }



    private void SaveAccountSetupInformation()
    {
        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String country = CountryName.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(country))
        {
            Toast.makeText(this, "Please write your country...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("country", country);
            userMap.put("status", "Hey there, i am using Poster Social Network, developed by Coding Cafe.");
            userMap.put("gender", "none");
            userMap.put("dob", "none");
            userMap.put("relationshipstatus", "none");
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(setupActivity.this, "your Account is created Successfully.", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message =  task.getException().getMessage();
                        Toast.makeText(setupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }



    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(setupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}

























//public class setupActivity extends AppCompatActivity {
//
//    private EditText Username, FullName, Country;
//    private Button savebutton,uploadButton, choseButton;
//    private CircleImageView profileImage;
//    private FirebaseAuth mAuth;
//    private DatabaseReference mRef;
//    String currentCurrentUserID;
//    private ProgressDialog loadingbar;
//    final static int gallary_pic = 1;
//    private StorageReference userProfileRef;
//    public Uri imageUri;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setup);
//        mAuth = FirebaseAuth.getInstance();
//        currentCurrentUserID = mAuth.getCurrentUser().getUid();
//        mRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentCurrentUserID);
//        loadingbar = new ProgressDialog(this);
//        userProfileRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
//
//        Username = findViewById(R.id.setup_username);
//        FullName = findViewById(R.id.setup_userFullName);
//        Country = findViewById(R.id.setup_Coutry);
//        savebutton = findViewById(R.id.save_button);
//        profileImage = findViewById(R.id.profile_image);
//        uploadButton=findViewById(R.id.upload_button);
//        choseButton=findViewById(R.id.Chose_Button);
//
//        savebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SaveUserInformation();
//            }
//        });
//
//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent gallaryIntent=new Intent();
//                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                gallaryIntent.setType("image/*");
//                startActivityForResult(gallaryIntent,gallary_pic);
//
//            }
//
//        });
//
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    String img= dataSnapshot.child("profileImage").getValue().toString();
//                    Toast.makeText(getApplicationContext(), img, Toast.LENGTH_SHORT).show();
//                    Picasso.with(setupActivity.this).load(img).placeholder(R.drawable.profile).into(profileImage);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//
//
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == gallary_pic && resultCode == RESULT_OK && data != null) {
//            Uri imageUri = data.getData();
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(this);
//        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                loadingbar.setTitle("Profiles Image");
//                loadingbar.setMessage("Please Wait while we are Updating your profile Image");
//                loadingbar.show();
//                loadingbar.setCanceledOnTouchOutside(true);
//
//
//                Uri resultUri = result.getUri();
//                StorageReference filePath = userProfileRef.child(currentCurrentUserID + ".jpg");
//                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Profile Image Updated Successfully", Toast.LENGTH_SHORT).show();
//                            final String DownloadURL = task.getResult().getStorage().getDownloadUrl().toString();
//                            mRef.child("profileImage").setValue(DownloadURL)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                Intent selfIntent = new Intent(setupActivity.this, setupActivity.class);
////                                                selfIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                startActivity(selfIntent);
//                                                Toast.makeText(getApplicationContext(), "Profile Image Updated Successfully", Toast.LENGTH_SHORT).show();
//                                                loadingbar.dismiss();
//                                            } else {
//                                                String message = task.getException().getMessage();
//                                                Toast.makeText(getApplicationContext(), "Error Occured" + message, Toast.LENGTH_SHORT).show();
//                                                loadingbar.dismiss();
//                                            }
//
//                                        }
//                                    });
//                        }
//                    }
//                });
//            } else {
//                Toast.makeText(getApplicationContext(), "Profile can't be cropped Please Try Again", Toast.LENGTH_SHORT).show();
//                loadingbar.dismiss();
//            }
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//    private void SaveUserInformation() {
//        String username = Username.getText().toString();
//        String Fullname = FullName.getText().toString();
//        String country = Country.getText().toString();
//
//        if (TextUtils.isEmpty(username)) {
//            Toast.makeText(getApplicationContext(), "Please Enter userName", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(Fullname)) {
//            Toast.makeText(getApplicationContext(), "Please Enter Full Namw", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(country)) {
//            Toast.makeText(getApplicationContext(), "Please Enter your Country Name", Toast.LENGTH_SHORT).show();
//        } else {
//
//            loadingbar.setTitle("Saving Account Information");
//            loadingbar.setMessage("Please Wait while we are authenticating");
//            loadingbar.show();
//            loadingbar.setCanceledOnTouchOutside(true);
//            HashMap userMap = new HashMap();
//
//            userMap.put("Username", username);
//            userMap.put("Full Name", Fullname);
//            userMap.put("Country", country);
//            userMap.put("ProfileImage", profileImage);
//            userMap.put("Date Of Birth", "My dob");
//            userMap.put("Gender", "Male");
//            mRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//
//                    if (task.isSuccessful()) {
//                        sendUserToMainActivity();
//                        Toast.makeText(getApplicationContext(), "Account Created Successfully", Toast.LENGTH_LONG).show();
//                        loadingbar.dismiss();
//                    } else {
//                        String message = task.getException().getMessage();
//                        Toast.makeText(getApplicationContext(), "Error" + message, Toast.LENGTH_SHORT).show();
//                        loadingbar.dismiss();
//                    }
//                }
//            });
//
//        }
//
//    }
//
//    private void sendUserToMainActivity() {
//        Intent mainIntent = new Intent(setupActivity.this, MainActivity.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//        finish();
//    }
//
//
//}
