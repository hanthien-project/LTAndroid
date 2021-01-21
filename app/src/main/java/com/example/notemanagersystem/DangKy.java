package com.example.notemanagersystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notemanagersystem.Functions.DBFirebase;
import com.example.notemanagersystem.Table.Profile;
import com.example.notemanagersystem.Table.User;
import com.example.notemanagersystem.ui.category.Category;
import com.example.notemanagersystem.ui.category.CategoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DangKy extends AppCompatActivity {

    EditText etxtEmail, etxtPassword, etxtCpassword;
    User user;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceProfile;
    AlertDialog.Builder builder;
    DBFirebase dbFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        dbFirebase = new DBFirebase();
        etxtEmail = (EditText)findViewById(R.id.etxtEmail_DangKy);
        etxtPassword = (EditText)findViewById(R.id.etxtPassword_DangKy);
        etxtCpassword = (EditText)findViewById(R.id.etxtConfirmPassword_DangKy);
        Button btnSignUp = (Button)findViewById(R.id.btnSignUp_DangKy);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReferenceProfile = FirebaseDatabase.getInstance().getReference().child("profiles");

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        btnSignUp.setOnClickListener(signUpClick);
    }

    private View.OnClickListener signUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = etxtEmail.getText().toString();
            String password = etxtPassword.getText().toString();
            String cpassword = etxtCpassword.getText().toString();
            if (email.equals("") || password.equals("") || cpassword.equals("")) {
                builder.setMessage("Không được để trống");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
            else
            {
                if (email.indexOf("@gmail.com") != -1 && email.length()-email.indexOf("@gmail.com") == 10) {
                    if (password.equals(cpassword)) {
                        Query query = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(etxtEmail.getText().toString());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getChildrenCount() > 0) {
                                    builder.setMessage("Tài khoản đã tồn tại");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                                else
                                {
                                    String uid = UUID.randomUUID().toString();
                                    user = new User();
                                    user.setEmail(email);
                                    user.setPassword(password);
                                    user.setUserId(uid);
                                    databaseReference.push().setValue(user);

                                    Profile profile = new Profile();
                                    String s = email.substring(0, email.length()-10);
                                    profile.setUserId(uid);
                                    profile.setFirstName(s);
                                    profile.setLastName(s);
                                    databaseReferenceProfile.push().setValue(profile);


                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else{
                        builder.setMessage("Mật khẩu không khớp");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
                else {
                    builder.setMessage("Không đúng cú pháp. VD:string@gmail.com");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        }
    };

    public void onDangNhap(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}