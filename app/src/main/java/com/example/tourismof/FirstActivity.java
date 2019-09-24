package com.example.tourismof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tourismof.Fragments.FavoriteFragment;
import com.example.tourismof.Fragments.HomeFragment;
import com.example.tourismof.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame,new HomeFragment());
        transaction.commit();

        bottomNavigationView.setSelectedItemId(R.id.Home_button);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (menuItem.getItemId())
                {
                    case R.id.Home_button:
                        transaction.replace(R.id.frame,new HomeFragment());
                        break;
                    case R.id.favourite_posts_button:
                        transaction.replace(R.id.frame,new FavoriteFragment());
                        break;
                    case R.id.profile_button:
                        transaction.replace(R.id.frame,new ProfileFragment());
                        break;
                }
                transaction.commit();
                return true;
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
                if(currentFragment instanceof HomeFragment)
                    bottomNavigationView.setSelectedItemId(R.id.Home_button);
                else if(currentFragment instanceof FavoriteFragment)
                    bottomNavigationView.setSelectedItemId(R.id.favourite_posts_button);
                else if(currentFragment instanceof ProfileFragment)
                    bottomNavigationView.setSelectedItemId(R.id.profile_button);

            }
        });

    }
}
