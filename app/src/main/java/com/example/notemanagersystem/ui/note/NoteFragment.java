package com.example.notemanagersystem.ui.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.R;
import com.example.notemanagersystem.ui.category.Category;
import com.example.notemanagersystem.ui.category.CategoryAdapter;
import com.example.notemanagersystem.ui.note.Note;
import com.example.notemanagersystem.ui.note.NoteAdapter;
import com.example.notemanagersystem.ui.priority.Priority;
import com.example.notemanagersystem.ui.priority.PriorityAdapter;
import com.example.notemanagersystem.ui.status.Status;
import com.example.notemanagersystem.ui.status.StatusAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.notemanagersystem.DashBoard.currentUserId;

public class NoteFragment extends Fragment {

    DatabaseHelper db;
    NoteAdapter noteAdapter = null;
    ListView listView;
    AlertDialog dialog;
    static int posItem;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceCategory;
    DatabaseReference databaseReferencePriority;
    DatabaseReference databaseReferenceStatus;
    List<Note> noteList;
    List<Category> categoryList;
    List<Priority> priorityList;
    List<Status> statusList;
    String[] nameListCategory;
    String[] nameListPriority;
    String[] nameListStatus;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);

        listView = (ListView) root.findViewById(R.id.lstvNote);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        databaseReferenceCategory = FirebaseDatabase.getInstance().getReference().child("categories");
        databaseReferencePriority = FirebaseDatabase.getInstance().getReference().child("priorities");
        databaseReferenceStatus = FirebaseDatabase.getInstance().getReference().child("statuses");
        noteList = new ArrayList<Note>();
        categoryList = new ArrayList<Category>();
        priorityList = new ArrayList<Priority>();
        statusList = new ArrayList<Status>();

        databaseReference.addValueEventListener(valueEventListener);
        databaseReferenceCategory.addValueEventListener(valueEventListenerCategory);
        databaseReferencePriority.addValueEventListener(valueEventListenerPriority);
        databaseReferenceStatus.addValueEventListener(valueEventListenerStatus);

        //Khi list view Click
        listView.setOnItemLongClickListener(listClick);

        // Khi fab Click
        FloatingActionButton fab = root.findViewById(R.id.btnFab_Note);
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
            new AlertDialog.Builder(getActivity()).setTitle("Delete Note").setMessage("Bạn có muốn xóa hay không?").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = noteList.get(posItem).getName();
                    Query query = FirebaseDatabase.getInstance().getReference().child("notes").orderByChild("name").equalTo(name);
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
            }).setNegativeButton("Canncel", null).show();
            dialog.dismiss();
        }
    };

    //Edit click
    private View.OnClickListener editClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            View mView = getLayoutInflater().inflate(R.layout.customdialog_note_edit, null);

            //Lấy dữ liệu cho spinner Category
            nameListCategory=new String[categoryList.size()+1];
            nameListCategory[0] = "Select Category";
            for(int i=1; i<=categoryList.size(); i++){
                nameListCategory[i]=categoryList.get(i-1).getName(); //create array of name
            }
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListCategory);
            Spinner spnCategory = (Spinner) mView.findViewById(R.id.spnCategory_EditNote);
            spnCategory.setAdapter(categoryAdapter);
            spnCategory.setPrompt(nameListCategory[0]);

            //Lấy dữ liệu cho spinner Priority
            nameListPriority=new String[priorityList.size()+1];
            nameListPriority[0] = "Select Priority";
            for(int i=1; i<=priorityList.size(); i++){
                nameListPriority[i]=priorityList.get(i-1).getName(); //create array of name
            }
            ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListPriority);
            Spinner spnPriority = (Spinner) mView.findViewById(R.id.spnPriority_EditNote);
            spnPriority.setAdapter(priorityAdapter);
            spnPriority.setPrompt(nameListPriority[0]);

            //Lấy dữ liệu cho spinner Status
            nameListStatus=new String[statusList.size()+1];
            nameListStatus[0] = "Select Status";
            for(int i=1; i<=statusList.size(); i++){
                nameListStatus[i]=statusList.get(i-1).getName(); //create array of name
            }
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListStatus);
            Spinner spnStatus = (Spinner) mView.findViewById(R.id.spnStatus_EditNote);
            spnStatus.setAdapter(statusAdapter);
            spnStatus.setPrompt(nameListStatus[0]);

            EditText textDialog = (EditText) mView.findViewById(R.id.etxtText_EditNote);
            Button change = (Button) mView.findViewById(R.id.btnChange_EditNote);
            Button setPlandate = (Button) mView.findViewById(R.id.btn3cham_EditNote);
            TextView textViewPlandate = (TextView) mView.findViewById(R.id.txtvTitle_PlanDate_EditNote);
            String strPlanDate = textViewPlandate.getText().toString();

            setPlandate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder plandateBuilder = new AlertDialog.Builder(getActivity());
                    View plandateView = getLayoutInflater().inflate(R.layout.customdialog_note_plandate, null);
                    plandateBuilder.setView(plandateView);
                    final AlertDialog dialogPlandate = plandateBuilder.create();
                    dialogPlandate.show();

                    Button btnOK = (Button)plandateView.findViewById(R.id.btnOK_Note_Plandate);
                    Button btnCancel = (Button)plandateView.findViewById(R.id.btnCancel_Note_Plandate);

                    CalendarView calendarViewPlandate = (CalendarView)plandateView.findViewById(R.id.calendarView_Note_Plandate);
                    calendarViewPlandate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            String date = year+"/"+(month+1)+"/"+dayOfMonth;
                            textViewPlandate.setText(date);
                        }
                    });
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { dialogPlandate.dismiss(); }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogPlandate.dismiss();
                        }
                    });
                }
            });
            EditText txtName = (EditText) mView.findViewById(R.id.etxtText_EditNote);
            TextView txtPlanDate = (TextView) mView.findViewById(R.id.txtvTitle_PlanDate_EditNote);

            builder.setView(mView);
            dialog = builder.create();
            dialog.show();
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nameItem = noteList.get(posItem).getName();

                    String currentTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss aa", new Date());

                    String name = txtName.getText().toString();
                    String category = spnCategory.getSelectedItem().toString();
                    String priority = spnPriority.getSelectedItem().toString();
                    String status = spnStatus.getSelectedItem().toString();
                    String planDate = txtPlanDate.getText().toString();
                    String createDate = currentTime;
                    if (name.equals("") || category.equals(nameListCategory[0]) || priority.equals(nameListPriority[0]) || status.equals(nameListStatus[0]) || planDate.equals(strPlanDate)) {
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
                        Query query = FirebaseDatabase.getInstance().getReference().child("notes").orderByChild("name").equalTo(nameItem);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Map<String, Object> updates = new HashMap<String, Object>();
                                    updates.put("name", name);
                                    updates.put("category", category);
                                    updates.put("priority", priority);
                                    updates.put("status", status);
                                    updates.put("planDate", planDate);
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
                    dialog.dismiss();
                }
            });
            Button close = (Button) mView.findViewById(R.id.btnClose_EditNote);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
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
            View mView = getLayoutInflater().inflate(R.layout.customdialog_note, null);

            //Lấy dữ liệu cho spinner Category
            nameListCategory=new String[categoryList.size()+1];
            nameListCategory[0] = "Select Category";
            for(int i=1; i<=categoryList.size(); i++){
                nameListCategory[i]=categoryList.get(i-1).getName(); //create array of name
            }
            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListCategory);
            Spinner spnCategory = (Spinner) mView.findViewById(R.id.spnCategory_Note);
            spnCategory.setAdapter(categoryAdapter);
            spnCategory.setPrompt(nameListCategory[0]);

            //Lấy dữ liệu cho spinner Priority
            nameListPriority=new String[priorityList.size()+1];
            nameListPriority[0] = "Select Priority";
            for(int i=1; i<=priorityList.size(); i++){
                nameListPriority[i]=priorityList.get(i-1).getName(); //create array of name
            }
            ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListPriority);
            Spinner spnPriority = (Spinner) mView.findViewById(R.id.spnPriority_Note);
            spnPriority.setAdapter(priorityAdapter);
            spnPriority.setPrompt(nameListPriority[0]);

            //Lấy dữ liệu cho spinner Status
            nameListStatus=new String[statusList.size()+1];
            nameListStatus[0] = "Select Status";
            for(int i=1; i<=statusList.size(); i++){
                nameListStatus[i]=statusList.get(i-1).getName(); //create array of name
            }
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListStatus);
            Spinner spnStatus = (Spinner) mView.findViewById(R.id.spnStatus_Note);
            spnStatus.setAdapter(statusAdapter);
            spnStatus.setPrompt(nameListStatus[0]);

            EditText textDialog = (EditText) mView.findViewById(R.id.etxtText_Note);
            Button add = (Button) mView.findViewById(R.id.btnAdd_Note);
            Button setPlandate = (Button) mView.findViewById(R.id.btn3cham_Note);
            TextView textViewPlandate = (TextView) mView.findViewById(R.id.txtvTitle_Plandate);
            String strPlanDate = textViewPlandate.getText().toString();

            setPlandate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder plandateBuilder = new AlertDialog.Builder(getActivity());
                    View plandateView = getLayoutInflater().inflate(R.layout.customdialog_note_plandate, null);
                    plandateBuilder.setView(plandateView);
                    final AlertDialog dialogPlandate = plandateBuilder.create();
                    dialogPlandate.show();

                    Button btnOK = (Button)plandateView.findViewById(R.id.btnOK_Note_Plandate);
                    Button btnCancel = (Button)plandateView.findViewById(R.id.btnCancel_Note_Plandate);

                    CalendarView calendarViewPlandate = (CalendarView)plandateView.findViewById(R.id.calendarView_Note_Plandate);
                    calendarViewPlandate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                            String date = year+"/"+(month+1)+"/"+dayOfMonth;
                            textViewPlandate.setText(date);
                        }
                    });
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { dialogPlandate.dismiss(); }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogPlandate.dismiss();
                        }
                    });
                }
            });

            EditText txtName = (EditText) mView.findViewById(R.id.etxtText_Note);
            TextView txtPlanDate = (TextView) mView.findViewById(R.id.txtvTitle_Plandate);

            builder.setView(mView);
            dialog = builder.create();
            dialog.show();
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss aa", new Date());

                    String name = txtName.getText().toString();
                    String category = spnCategory.getSelectedItem().toString();
                    String priority = spnPriority.getSelectedItem().toString();
                    String status = spnStatus.getSelectedItem().toString();
                    String planDate = txtPlanDate.getText().toString();
                    String createDate = currentTime;
                    if (name.equals("") || category.equals(nameListCategory[0]) || priority.equals(nameListPriority[0]) || status.equals(nameListStatus[0]) || planDate.equals(strPlanDate)) {
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
                        for(int i=0; i<noteList.size(); i++){
                            if (noteList.get(i).getName().equals(name))
                                isOK = false;
                        }
                        if (isOK == false) {
                            mBuilder.setMessage("Note đã tồn tại");
                            mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                            mBuilder.show();
                        }
                        else{
                            Note note = new Note();
                            note.setName(name);
                            note.setCategory(category);
                            note.setPriority(priority);
                            note.setStatus(status);
                            note.setPlanDate(planDate);
                            note.setCreateDate(createDate);
                            note.setUserId(currentUserId);
                            databaseReference.push().setValue(note);
                        }
                    }
                    dialog.dismiss();
                }
            });
            Button close = (Button) mView.findViewById(R.id.btnClose_Note);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    };

    //Get value Note
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            noteList = new ArrayList<Note>();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                Note note = dataSnapshot.getValue(Note.class);
                if (note.getUserId().equals(currentUserId))
                    noteList.add(note);
            }
            noteAdapter = new NoteAdapter(getActivity(), R.layout.row_note, noteList);
            listView.setAdapter(noteAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    //Get value Category
    ValueEventListener valueEventListenerCategory = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            categoryList = new ArrayList<Category>();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                Category category = dataSnapshot.getValue(Category.class);
                if (category.getUserId().equals(currentUserId))
                    categoryList.add(category);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    //Get value Priority
    ValueEventListener valueEventListenerPriority = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            priorityList = new ArrayList<Priority>();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                Priority priority = dataSnapshot.getValue(Priority.class);
                if (priority.getUserId().equals(currentUserId))
                    priorityList.add(priority);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    //Get value Status
    ValueEventListener valueEventListenerStatus = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            statusList = new ArrayList<Status>();
            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                Status status = dataSnapshot.getValue(Status.class);
                if (status.getUserId().equals(currentUserId))
                    statusList.add(status);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}