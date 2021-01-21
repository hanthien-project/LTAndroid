package com.example.notemanagersystem;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.notemanagersystem.Table.Profile;
import com.example.notemanagersystem.Table.User;
import com.example.notemanagersystem.ui.category.Category;
import com.example.notemanagersystem.ui.category.CategoryAdapter;
import com.example.notemanagersystem.ui.editProfile.EditProfileFragment;
import com.facebook.AccessToken;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.notemanagersystem.R.id.imageView_Header;
import static com.example.notemanagersystem.R.id.nav_category;

public class DashBoard extends AppCompatActivity {

    public static String currentEmail = null;
    public static String currentPassword = null;
    public static String currentFirstName = null;
    public static String currentLastName = null;
    public static String currentUserId = null;
    FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_dieu_khien);

        NavigationView navigationView = findViewById(R.id.nav_view);

        //Set tên cho nav header = email login
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.vtxtEmail_Dashboard);
        TextView navHeader = (TextView) headerView.findViewById(R.id.title_header);
        ImageView imageView = (ImageView)findViewById(imageView_Header);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            currentEmail = personEmail;
            currentUserId = personId;
            navUsername.setText(personEmail);
            navHeader.setText("Xin chào: " + personName);
            //Picasso.get().load(acct.getPhotoUrl()).placeholder(R.mipmap.ic_launcher_round).into(imageView);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_edit_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_change_password).setVisible(false);
        }
        else if(accessToken!=null){
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            currentEmail = user.getEmail();
            currentUserId = user.getUid();
            navUsername.setText(currentEmail);
            navHeader.setText("Xin chào: " + user.getDisplayName());
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_edit_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_change_password).setVisible(false);
        }
        else {
            String email = getIntent().getStringExtra("EMAIL");
            String password = getIntent().getStringExtra("PASSWORD");
            String userId = getIntent().getStringExtra("USERID");
            currentEmail = email;
            currentPassword = password;
            navUsername.setText(email);
            Query query = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(currentEmail);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        currentUserId = user.getUserId();
                    }
                    Query query = FirebaseDatabase.getInstance().getReference().child("profiles").orderByChild("userId").equalTo(currentUserId);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Profile profile = dataSnapshot.getValue(Profile.class);
                                currentFirstName = profile.getFirstName();
                                currentLastName = profile.getLastName();
                            }
                            navHeader.setText("Xin chào: " + currentFirstName + " " + currentLastName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, nav_category, R.id.nav_priority, R.id.nav_status, R.id.nav_note
                , R.id.nav_edit_profile, R.id.nav_change_password)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bang_dieu_khien, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void onHome(View view) {
        finish();
        Intent intent = new Intent(this, DashBoard.class);
        startActivity(intent);
    }
    public void onDangNhap(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void getUserId()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(currentEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    currentUserId = user.getUserId();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getUserName()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("profiles").orderByChild("userId").equalTo(currentUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    currentFirstName = profile.getFirstName();
                    currentLastName = profile.getFirstName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}