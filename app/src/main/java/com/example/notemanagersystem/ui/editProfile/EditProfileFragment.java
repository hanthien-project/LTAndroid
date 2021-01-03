package com.example.notemanagersystem.ui.editProfile;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.R;
import com.example.notemanagersystem.ui.note.NoteAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static com.example.notemanagersystem.DashBoard.currentEmail;
import static com.example.notemanagersystem.DashBoard.currentFirstName;
import static com.example.notemanagersystem.DashBoard.currentLastName;

public class EditProfileFragment extends Fragment {

    DatabaseHelper db ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        LinearLayout mainlayout = (LinearLayout) root.findViewById(R.id.layout_EditProfile);
        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }
        });

        EditText etxtEmail = (EditText)root.findViewById(R.id.etxtEmail_EditProfile);
        EditText etxtFirstName = (EditText)root.findViewById(R.id.etxtFirstName_EditProfile);
        EditText etxtLastName = (EditText)root.findViewById(R.id.etxtLastName_EditProfile);
        Button btnChange = (Button)root.findViewById(R.id.btnChange_EditProfile);
        etxtEmail.setText(currentEmail);


        db = new DatabaseHelper(getActivity());

        List<String> profileList = db.getAllDataProfile(currentEmail);
        etxtFirstName.setText(profileList.get(0));
        etxtLastName.setText(profileList.get(1));

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(etxtEmail, etxtFirstName, etxtLastName);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }
        });

        return root;
    }

    public void dialog(EditText etxtEmail, EditText etxtFirstName, EditText etxtLastName)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setCancelable(true);
        if (etxtEmail.getText().toString().equals("") || etxtFirstName.getText().toString().equals("") || etxtLastName.getText().toString().equals(""))
            builder.setMessage("Không được để trống");
        else {
            if (etxtEmail.getText().toString().equals(currentEmail)) {
                boolean checkEmail = db.checkEmail(etxtEmail.getText().toString());
                if (checkEmail == true) {
                    boolean changeProfile = db.changeProfile(etxtEmail.getText().toString(),
                            etxtFirstName.getText().toString(), etxtLastName.getText().toString());
                    if (changeProfile == true)
                        builder.setMessage("Đổi thông tin thành công");
                    else
                        builder.setMessage("Không thành công");
                } else
                    builder.setMessage("Email không đúng");
            }
            else
                builder.setMessage("Email không khớp với Email bạn đang dùng");
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