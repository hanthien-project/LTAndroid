package com.example.notemanagersystem.ui.changePassword;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.notemanagersystem.DashBoard.currentPassword;
import static com.example.notemanagersystem.DashBoard.currentEmail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_password, container, false);

        LinearLayout mainlayout = (LinearLayout) root.findViewById(R.id.layout_ChangePassword);
        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }
        });

        EditText currentPassword = (EditText)root.findViewById(R.id.etxtCurrentPassword_ChangePassword);
        EditText newPassword = (EditText)root.findViewById(R.id.etxtNewPassword_ChangePassword);
        EditText confirmPassword = (EditText)root.findViewById(R.id.etxtConfirmPassword_ChangePassword);
        Button btnChange = (Button)root.findViewById(R.id.btnChange_ChangePassword);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(currentPassword, newPassword, confirmPassword);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }
        });
        return root;
    }

    public void dialog(EditText etxtCurrentPassword, EditText etxtNewPassword, EditText etxtPasswordAgain)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        if (etxtCurrentPassword.getText().toString().equals("") || etxtNewPassword.getText().toString().equals("") || etxtPasswordAgain.getText().toString().equals(""))
            builder.setMessage("Không được để trống");
        else {
            if (etxtCurrentPassword.getText().toString().equals(currentPassword)) {
                if (etxtNewPassword.getText().toString().equals(etxtPasswordAgain.getText().toString())) {

                    Query query = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(currentEmail);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("email", currentEmail);
                                updates.put("password", etxtNewPassword.getText().toString());
                                dataSnapshot.getRef().updateChildren(updates);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    builder.setMessage("Đổi mật khẩu thành công");
                }
                else
                    builder.setMessage("Mật khẩu xác nhận không khớp");
            }
            else
                builder.setMessage("Mật khẩu hiện tại không đúng");
        }
        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}