package com.example.tourismof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tourismof.Fragments.FavoriteFragment;
import com.example.tourismof.Fragments.HomeFragment;
import com.example.tourismof.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FirstActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Home_button:
                    FragmentManager fmh=getSupportFragmentManager();
                    FragmentTransaction fth=fmh.beginTransaction();
                    Fragment fragmenth= new HomeFragment();
                    fth.replace(R.id.frame, fragmenth);
                    fth.commit();
                    return true;

                case R.id.favourite_posts_button:
//                    mTextMessage.setText(R.string.title_notifications);
                    FragmentManager fmp=getSupportFragmentManager();
                    FragmentTransaction ftp=fmp.beginTransaction();
                    Fragment fragmentpost= new FavoriteFragment();
                    ftp.replace(R.id.frame, fragmentpost);
                    ftp.commit();
                    return true;
                case R.id.profile_button:
//                    mTextMessage.setText(R.string.title_profile);
                    FragmentManager fmn=getSupportFragmentManager();
                    FragmentTransaction ftn=fmn.beginTransaction();
                    Fragment fragmentprofile= new ProfileFragment();
                    ftn.replace(R.id.frame, fragmentprofile);
                    ftn.commit();
                    return  true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentManager fmh=getSupportFragmentManager();
        FragmentTransaction fth=fmh.beginTransaction();
        Fragment fragmenth= new HomeFragment();
        fth.replace(R.id.frame, fragmenth);
        fth.commit();
    }



























































































































//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first);
//
//        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
//        bottomNavigationView.setSelectedItemId(R.id.Home_button);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame,new HomeFragment());
//        transaction.commit();
//
//
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                switch (menuItem.getItemId())
//                {
//                    case R.id.Home_button:
//                        transaction.replace(R.id.frame,new HomeFragment());
//                        break;
//                    case R.id.favourite_posts_button:
//                        transaction.replace(R.id.frame,new FavoriteFragment());
//                        break;
//                    case R.id.profile_button:
//                        transaction.replace(R.id.frame,new ProfileFragment());
//                        break;
//                }
//                transaction.commit();
//                return true;
//            }
//        });
//
//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
//                if(currentFragment instanceof HomeFragment)
//                    bottomNavigationView.setSelectedItemId(R.id.Home_button);
//                else if(currentFragment instanceof FavoriteFragment)
//                    bottomNavigationView.setSelectedItemId(R.id.favourite_posts_button);
//                else if(currentFragment instanceof ProfileFragment)
//                    bottomNavigationView.setSelectedItemId(R.id.profile_button);
//
//            }
//        });
//
//    }
}
