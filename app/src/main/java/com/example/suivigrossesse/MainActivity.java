package com.example.suivigrossesse;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.suivigrossesse.fragments.AppoitmentFragment;
import com.example.suivigrossesse.fragments.HomeFragment;
import com.example.suivigrossesse.fragments.SecondFragment;
import com.example.suivigrossesse.fragments.ProfileFragment;
import com.example.suivigrossesse.fragments.ToolsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Suivi Grossesse";
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;
    private String userID;
    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set default showing fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
        }

        setupToolbar();
        setupDrawer();

//        // This will display an Up icon (<-), we will replace it with hamburger later
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //-- Update drawer user name and user email
        setDrawerUserNameEmail(db);
        //-- test update User Phone
        updateUser("987456322", db);

//        MenuItem itemLogout = header.findViewById(R.id.nav_logout);
//        itemLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
////                Intent intent = new Intent(SelectActivity.this, DownActivity.class);
////                startActivity(intent);
//                Toast.makeText(MainActivity.this, "Test logout",
//                        Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        } );
        // --- My Code End ---
    }

    private void setupToolbar() {
        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawer() {
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
    }

    public void setDrawerUserNameEmail(FirebaseFirestore db) {
        // --- My Code ---
        View header = nvDrawer.getHeaderView(0);
        TextView textViewUserName = header.findViewById(R.id.user_name);
        TextView textViewUserEmail = header.findViewById(R.id.user_email);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("email");
//        userID =  intent.getStringExtra("userID");
//        userName = intent.getStringExtra("userName");
        Log.d(TAG, userID + " - " + userName + " - " + userEmail);

        db.collection("User")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // Document found, you can access its data
                                userID = document.getId();

                                Map<String, Object> userData = document.getData();
                                userName = (String) userData.get("name");
                                Log.d(TAG, "Welcome in MainActivity" + userData);

                                textViewUserName.setText(userName);
                                textViewUserEmail.setText(userEmail);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void updateUser(String phone, FirebaseFirestore db) {
        //-- test
        // Update an existing document
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String current = user.getUid();//getting unique user id

        DocumentReference docRef = db.collection("User").document(current);
        docRef.update("phone", phone).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Successful !!!!" );
                } else {
                    Log.d(TAG, "Failure !!!!" );
                }
            }
        });
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;

        int id = menuItem.getItemId();

        if (id == R.id.nav_logout){
            Toast.makeText(getApplicationContext(), "Test logout",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_appoitement) {
//            fragmentClass = SecondFragment.class;
            fragmentClass = AppoitmentFragment.class;
        } else if (id == R.id.nav_tools) {
            fragmentClass = ToolsFragment.class;
        } else if (id == R.id.nav_account) {
            fragmentClass = ProfileFragment.class;
        } else {
            fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle presses on the action bar items
        int id = item.getItemId();
        if (id == R.id.miCompose) {
            Toast.makeText(getApplicationContext(), "Icon Compose",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.miSetting) {
            Toast.makeText(getApplicationContext(), "Icon Profile",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.miDateConception) {
            showDatePicker();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TextView tvDateConception = findViewById(R.id.tvDateConception);
                        tvDateConception.setText("Date de Conception : " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setTitle("Date de Conception");
        datePickerDialog.show();
    }
}