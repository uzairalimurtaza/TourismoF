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
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");

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
                        holder.setprice(model.getprice());
                        holder.setlocation(model.getlocation());
                        holder.setDescription(model.getDescription());
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
        postList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class PostsVIewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PostsVIewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescription(String description) {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setprice(String price) {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_price);
            PostDescription.setText(price);
        }

        public void setlocation(String location) {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_location);
            PostDescription.setText(location);
        }

        public void setPostimage(Context ctx, String postimage) {
            ImageView PostImage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).into(PostImage);
        }
    }
}

