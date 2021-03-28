package com.example.letstravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.letstravel.User.AccountFragment;
import com.example.letstravel.User.DiscoverTripsFragment;
import com.example.letstravel.User.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class DashboardUserActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    String mUID = "uid";
    ActionBar actionBar;

    ChipNavigationBar top_nav;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        firebaseAuth = FirebaseAuth.getInstance();

        actionBar = getSupportActionBar();
        firebaseAuth = FirebaseAuth.getInstance();

        //actionBar.setTitle("Home");
        top_nav = findViewById(R.id.top_nav);

        if (savedInstanceState == null){
            top_nav.setItemSelected(R.id.home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, homeFragment)
                    .commit();
        }

        top_nav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.discover:
                        fragment = new DiscoverTripsFragment();
                        break;
                    case R.id.account:
                        fragment = new AccountFragment();
                        break;
                }

                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }else {
                    Toast.makeText(DashboardUserActivity.this, "Error creating fragment", Toast.LENGTH_SHORT).show();
                }
            }
        });


        checkUserStatus();
    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            mUID = user.getUid();

        } else {
            startActivity(new Intent(DashboardUserActivity.this, LoginActivity.class));
           finish();
        }
    }

}