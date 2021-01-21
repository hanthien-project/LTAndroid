package com.example.notemanagersystem.ui.priority;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.notemanagersystem.DashBoard.currentEmail;
import static com.example.notemanagersystem.DashBoard.currentUserId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriorityFragment extends Fragment {

    PriorityAdapter priorityAdapter = null;
    List<Priority> priorityList;
    ListView listView;
    AlertDialog dialog;
    static int posItem;
    static boolean isOK = true;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_priority, container, false);

        listView = (ListView) root.findViewById(R.id.lstvPriority);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("priorities");
        priorityList = new ArrayList<Priority>();
        databaseReference.addValueEventListener(valueEventListener);

        //Khi list view Click
        listView.setOnItemLongClickListener(listClick);

        // Khi fab Click
        FloatingActionButton fab = root.findViewById(R.id.btnFab_Priority);
        fab.setOnClickListener(fabClick);
        return root;
    }

    private AdapterView.OnItemLongClickListener listClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            View nView = getLayoutInflater().inflate(R.layout.dialog_delete_edit, null);
            Button delete = (Button) nView.findViewById(R.id.btnDelete_All);
            Button edit = (Button) nView.findViewById(R.id.btnEdit_All);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setView(nView);
            dialog = mBuilder.create();
            dialog.show();

            //Khi click delete
            posItem = position;
            delete.setOnClickListener(deleteClick);

            //Khi click edit
            edit.setOnClickListener(editClick);
            return false;
        }
    };

    //Delete click
    private View.OnClickListener deleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(getActivity()).setTitle("Delete priority").setMessage("Bạn có muốn xóa hay không?").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = priorityList.get(posItem).getName();
                    Query query = FirebaseDatabase.getInstance().getReference().child("priorities").orderByChild("name").equalTo(name);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                dataSnapshot.getRef().removeValue();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }).setNegativeButton("Cancel", null).show();
            dialog.dismiss();
        }
    };

    //Edit click
    private View.OnClickListener editClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();

            AlertDialog.Builder nBuilder = new AlertDialog.Builder(getActivity());
            nBuilder.setCancelable(true);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setCancelable(true);
            View mView = getLayoutInflater().inflate(R.layout.customdialog_priority_edit, null);
            EditText textDialog = (EditText) mView.findViewById(R.id.etxtText_EditPriority);
            Button change = (Button) mView.findViewById(R.id.btnChange_EditPriority);

            mBuilder.setView(mView);
            AlertDialog mDialog;
            mDialog = mBuilder.create();
            mDialog.show();
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String nameItem = priorityList.get(posItem).getName();

                    String currentTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss aa", new Date());

                    String name = textDialog.getText().toString();
                    String createDate = currentTime;
                    if (name.equals("")) {
                        nBuilder.setMessage("Không được để trống");
                        nBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        nBuilder.show();
                    }
                    else
                    {
                        Query query = FirebaseDatabase.getInstance().getReference().child("priorities").orderByChild("name").equalTo(nameItem);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Map<String, Object> updates = new HashMap<String, Object>();
                                    updates.put("name", name);
                                    updates.put("createDate", createDate);
                                    updates.put("userId", currentUserId);
                                    dataSnapshot.getRef().updateChildren(updates);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    mDialog.dismiss();
                }
            });
            Button close = (Button) mView.findViewById(R.id.btnClose_EditPriority);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }
    };

    // Fab click (Add)
    private View.OnClickListener fabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setCancelable(true);

            View mView = getLayoutInflater().inflate(R.layout.customdialog_priority, null);
            EditText textDialog = (EditText) mView.findViewById(R.id.etxtText_Priority);
            Button add = (Button) mView.findViewById(R.id.btnAdd_Priority);

            builder.setView(mView);
            dialog = builder.create();
            dialog.show();

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss aa", new Date());

                    String name = textDialog.getText().toString();
                    String createDate = currentTime;
                    if (name.equals("")) {
                        mBuilder.setMessage("Không được để trống");
                        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        mBuilder.show();
                    }
                    else
                    {
                        boolean isOK = true;
                        for(int i=0; i<priorityList.size(); i++)
                            if (priorityList.get(i).getName().equals(name))
                                isOK = false;
                        if (isOK == false ) {
                            mBuilder.setMessage("Priority đã tồn tại");
                            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            mBuilder.show();
                        }
                        else{
                            Priority priority = new Priority();
                            priority.setName(name);
                            priority.setCreateDate(createDate);
                            priority.setUserId(currentUserId);
                            databaseReference.push().setValue(priority);
                        }
                    }
                    dialog.dismiss();
                }
            });

            Button close = (Button) mView.findViewById(R.id.btnClose_Priority);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    };

    //Get value priority
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            priorityList = new ArrayList<Priority>();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                Priority priority = dataSnapshot.getValue(Priority.class);
                if (priority.getUserId().equals(currentUserId))
                    priorityList.add(priority);
            }
            priorityAdapter = new PriorityAdapter(getActivity(), R.layout.row_priority, priorityList);
            listView.setAdapter(priorityAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}