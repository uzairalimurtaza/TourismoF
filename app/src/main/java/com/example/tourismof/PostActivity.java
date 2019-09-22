package com.example.tourismof;
//
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//
//public class PostActivity extends AppCompatActivity {
//
//    private Toolbar mtoolBar;
//
//    private ImageButton imgButton;
//    private TextView postDescition;
//    private Button btn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post);
//
//        mtoolBar= findViewById(R.id.update_post_page_toolbar);
//        setSupportActionBar(mtoolBar);
//        getSupportActionBar().setTitle("Home");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id= item.getItemId();
//        if(id == android.R.id.home)
//        {
//            SendUserToMainActivity();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void SendUserToMainActivity() {
//        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
//
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//
//        }
//}



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private ImageView SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private String Description;

    private StorageReference PostsImagesRefrence;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostsImagesRefrence = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");


        SelectPostImage = findViewById(R.id.post_image);
        UpdatePostButton = findViewById(R.id.Add_post_button);
        PostDescription = findViewById(R.id.post_description);
        loadingBar = new ProgressDialog(this);


//        mToolbar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Update Post");


        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });


        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ValidatePostInfo();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void ValidatePostInfo()
    {
        Description = PostDescription.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Please select post image...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please say something about your image...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Please wait, while we are updating your new post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void StoringImageToFirebaseStorage()
    {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;


        StorageReference filePath = PostsImagesRefrence.child("Post Images").child(ImageUri.getLastPathSegment() +"image "+ postRandomName + ".jpg");

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                    Toast.makeText(PostActivity.this, "image uploaded successfully to Storage...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

//                    SavingPostInformationToDatabase();

                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }


//
//
//    private void SavingPostInformationToDatabase()
//    {
//        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                if(dataSnapshot.exists())
//                {
//                    String userFullName = dataSnapshot.child("fullname").getValue().toString();
//                    String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();
//
//                    HashMap postsMap = new HashMap();
//                    postsMap.put("uid", current_user_id);
//                    postsMap.put("date", saveCurrentDate);
//                    postsMap.put("time", saveCurrentTime);
//                    postsMap.put("description", Description);
//                    postsMap.put("postimage", downloadUrl);
//                    postsMap.put("profileimage", userProfileImage);
//                    postsMap.put("fullname", userFullName);
//                    PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
//                            .addOnCompleteListener(new OnCompleteListener() {
//                                @Override
//                                public void onComplete(@NonNull Task task)
//                                {
//                                    if(task.isSuccessful())
//                                    {
//                                        SendUserToMainActivity();
//                                        Toast.makeText(New_Post.this, "New Post is updated successfully.", Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//                                    }
//                                    else
//                                    {
//                                        Toast.makeText(New_Post.this, "Error Occured while updating your post.", Toast.LENGTH_SHORT).show();
//                                        loadingBar.dismiss();
//                                    }
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }
    }




//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        int id = item.getItemId();
//
//        if(id == android.R.id.home)
//        {
//            SendUserToMainActivity();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//

//
//    private void SendUserToMainActivity()
//    {
//        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
//        startActivity(mainIntent);
//    }
}

