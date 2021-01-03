package com.example.notemanagersystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.notemanagersystem.ui.editProfile.EditProfileFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText euser, epassword;
    boolean isOK = false;

    private String username, password;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainlayout = (LinearLayout) findViewById(R.id.layout_Main);
        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });

        db = new DatabaseHelper(this);
        euser = (EditText)findViewById(R.id.etxtEmail_DangNhap);
        epassword = (EditText)findViewById(R.id.etxtPassword_DangNhap);
        Button btnSignIn = (Button)findViewById(R.id.btnSignIn_DangNhap);

        //remember
        CheckBox saveLoginCheckBox = (CheckBox)findViewById(R.id.chkRememberMe_DangNhap);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            euser.setText(loginPreferences.getString("username", ""));
            epassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = euser.getText().toString();
                String s2 = epassword.getText().toString();
                if (s1.equals("") || s2.equals(""))
                    builder.setMessage("Không được để trống");
                else
                {
                    boolean checkEmail = db.checkEmail(s1);
                    if (checkEmail == true) {
                        boolean checkPassword = db.checkCurrentPassowrd(s1, s2);
                        if (checkPassword == true) {
                            if (saveLoginCheckBox.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("username", s1);
                                loginPrefsEditor.putString("password", s2);
                                loginPrefsEditor.commit();
                            } else {
                                saveLogin = false;
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }

                            isOK = true;
                            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                            intent.putExtra("EMAIL", s1);
                            intent.putExtra("PASSWORD", s2);
                            List<String> listProfile = db.getAllDataProfile(s1);

                            finish();
                            startActivity(intent);
                        }
                        else
                            builder.setMessage("Sai mật khẩu");
                    }
                    else
                        builder.setMessage("Tài khoản không tồn tại");
                }
                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                if (isOK == false)
                    alert.show();
            }
        });
    }

    public void onDangKy(View view) {
        finish();
        Intent intent = new Intent(this, DangKy.class);
        startActivity(intent);
    }

    public void onExit(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exit_caption)
                .setMessage(R.string.exit_content)
                .setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                    }).show();
    }
    public void closeKeyBoard()
    {
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager)getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}