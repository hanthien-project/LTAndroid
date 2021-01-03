package com.example.notemanagersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DangKy extends AppCompatActivity {

    DatabaseHelper db;
    EditText etxtEmail, etxtPassword, etxtCpassword;
    boolean isOK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        db = new DatabaseHelper(this);
        etxtEmail = (EditText)findViewById(R.id.etxtEmail_DangKy);
        etxtPassword = (EditText)findViewById(R.id.etxtPassword_DangKy);
        etxtCpassword = (EditText)findViewById(R.id.etxtConfirmPassword_DangKy);
        Button btnSignUp = (Button)findViewById(R.id.btnSignUp_DangKy);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etxtEmail.getText().toString();
                String password = etxtPassword.getText().toString();
                String cpassword = etxtCpassword.getText().toString();
                if (email.equals("") || password.equals("") || cpassword.equals(""))
                    builder.setMessage("Không được để trống");
                else
                {
                    if (email.indexOf("@gmail.com") != -1 && email.length()-email.indexOf("@gmail.com")== 10) {
                        if (password.equals(cpassword)) {
                            boolean checkEmail = db.checkEmail(email);
                            if (checkEmail == false) {
                                boolean insertAccount = db.insertAccout(email, password);
                                if (insertAccount == true) {
                                    boolean insertProfile = db.insertProfile(email, "thien", "thien");
                                    if (insertProfile == true) {
                                        isOK = true;
                                        finish();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            } else
                                builder.setMessage("Tài khoản đã tồn tại");
                        } else
                            builder.setMessage("Mật khẩu không khớp");
                    }
                    else
                        builder.setMessage("Không đúng cú pháp. VD:string@gmail.com");
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

    public void onDangNhap(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}