package com.example.tourismof.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourismof.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {


    private RecyclerView postList;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, PostRef;
    private Context ctx;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postList = view.findViewById(R.id.recycler_view);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        DisplayAllUsersPosts();


        return view;


    }

    private void DisplayAllUsersPosts() {


        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(PostRef, Posts.class)
                .build();

        FirebaseRecyclerAdapter<Posts, PostsVIewHolder> adapter =
                new FirebaseRecyclerAdapter<Posts, PostsVIewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostsVIewHolder holder, int position, @NonNull Posts model) {
                        holder.setDate(model.getDate());
                        holder.setTime(model.getTime());
                        holder.setFullname(model.getFullname());
                        holder.setDescription(model.getDescription());
                        holder.setProfileimage(getActivity().getApplicationContext(), model.getProfileimage());
                        holder.setPostimage(getActivity().getApplicationContext(), model.getPostimage());
                    }

                    @NonNull
                    @Override
                    public PostsVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_posts_layout, parent, false);
                        return new PostsVIewHolder(view);
                    }
                };
    }


    public static class PostsVIewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PostsVIewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String fullname) {
            TextView username = mView.findViewById(R.id.post_username);
            username.setText(fullname);
        }

        public void setProfileimage(Context ctx, String profileimage) {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(profileimage).into(image);
        }

        public void setTime(String time) {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_Time);
            PostTime.setText("    " + time);
        }

        public void setDate(String date) {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_Date);
            PostDate.setText("    " + date);
        }

        public void setDescription(String description) {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setPostimage(Context ctx, String postimage) {
            ImageView PostImage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).into(PostImage);
        }
    }
}

