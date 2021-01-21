package com.example.notemanagersystem.ui.editProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.core.pert.Tasks;
import com.example.notemanagersystem.DashBoard;
import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.MainActivity;
import com.example.notemanagersystem.R;
import com.example.notemanagersystem.Table.Profile;
import com.example.notemanagersystem.ui.category.Category;
import com.example.notemanagersystem.ui.category.CategoryAdapter;
import com.example.notemanagersystem.ui.home.HomeFragment;
import com.example.notemanagersystem.ui.note.NoteAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.notemanagersystem.DashBoard.currentEmail;
import static com.example.notemanagersystem.DashBoard.currentFirstName;
import static com.example.notemanagersystem.DashBoard.currentLastName;
import static com.example.notemanagersystem.DashBoard.currentUserId;

public class EditProfileFragment extends Fragment {

    DatabaseReference databaseReference;
    List<Profile> profileList;

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

        EditText email = (EditText)root.findViewById(R.id.etxtEmail_EditProfile);
        EditText firstName = (EditText)root.findViewById(R.id.etxtFirstName_EditProfile);
        EditText lastName = (EditText)root.findViewById(R.id.etxtLastName_EditProfile);
        Button change = (Button)root.findViewById(R.id.btnChange_EditProfile);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("profiles");
        profileList = new ArrayList<Profile>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileList = new ArrayList<Profile>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    if (profile.getUserId().equals(currentUserId))
                        profileList.add(profile);
                }
                firstName.setText(profileList.get(0).getFirstName());
                lastName.setText(profileList.get(0).getLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        email.setText(currentEmail);



        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(email, firstName, lastName);
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
                Query query = FirebaseDatabase.getInstance().getReference().child("profiles").orderByChild("userId").equalTo(currentUserId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Map<String, Object> updates = new HashMap<String, Object>();
                            updates.put("userId", currentUserId);
                            updates.put("firstName", etxtFirstName.getText().toString());
                            updates.put("lastName", etxtLastName.getText().toString());
                            dataSnapshot.getRef().updateChildren(updates);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                builder.setMessage("Đổi thông tin thành công");
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

    //Get value profile
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            profileList = new ArrayList<Profile>();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                Profile profile = dataSnapshot.getValue(Profile.class);
                if (profile.getUserId().equals(currentUserId))
                    profileList.add(profile);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

}