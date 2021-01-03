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
import com.example.notemanagersystem.ui.status.Status;
import com.example.notemanagersystem.ui.status.StatusAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.notemanagersystem.DashBoard.currentEmail;

public class NoteFragment extends Fragment {

    DatabaseHelper db ;
    NoteAdapter categoryAdapter = null;
    NoteAdapter priorityAdapter = null;
    NoteAdapter statusAdapter = null;
    NoteAdapter noteAdapter = null;
    List<Note> listNote;
    List<Category> listCategory;
    List<Priority> listPriority;
    List<Status> listStatus;
    boolean isOK = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);


        db = new DatabaseHelper(getActivity());
        listNote = db.getAllDataNote(currentEmail);
        ListView list = (ListView) root.findViewById(R.id.lstvNote);
        noteAdapter = new NoteAdapter(getActivity(), R.layout.row_note, listNote);
        list.setAdapter(noteAdapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View nView = getLayoutInflater().inflate(R.layout.dialog_delete_edit, null);
                Button Dele = (Button) nView.findViewById(R.id.btnDelete_Category);
                Button Edit = (Button) nView.findViewById(R.id.btnEdit_Category);

                mBuilder.setView(nView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                db = new DatabaseHelper(getActivity());

                Dele.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getActivity()).setTitle("Delete Note").setMessage("Bạn có muốn xóa hay không?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String name = listNote.get(position).getName();
                                        db.deleteNoteByName(name);

                                        listNote = db.getAllDataNote(currentEmail);
                                        noteAdapter = new NoteAdapter(getActivity(), R.layout.row_note, listNote);
                                        list.setAdapter(noteAdapter);
                                    }
                                }).setNegativeButton("Canncel", null).show();

                        dialog.dismiss();

                    }
                });

                Edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(getActivity());
                        View mView1 = getLayoutInflater().inflate(R.layout.customdialog_category, null);
                        final EditText textDialog1 = (EditText) mView1.findViewById(R.id.etxtText_Category);

                        Button add1 = (Button) mView1.findViewById(R.id.btnAdd_Category);

                        mBuilder1.setView(mView1);
                        final AlertDialog dialog1 = mBuilder1.create();
                        dialog1.show();
                        db = new DatabaseHelper(getActivity());

                    }
                });

                return false;
            }
        });





        FloatingActionButton fab = root.findViewById(R.id.btnFab_Note);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Note Form");

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.customdialog_note, null);

                //Lấy dữ liệu cho spinner Category
                listCategory = db.getAllDataCategory(currentEmail);

                String[] nameListCategory=new String[listCategory.size()+1];
                nameListCategory[0] = "Select Category";
                for(int i=1; i<=listCategory.size(); i++){
                    nameListCategory[i]=listCategory.get(i-1).getName(); //create array of name
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListCategory);
                Spinner spnCategory = (Spinner) mView.findViewById(R.id.spnCategory_Note);
                spnCategory.setAdapter(categoryAdapter);
                spnCategory.setPrompt(nameListCategory[0]);

                //Lấy dữ liệu cho spinner Priority
                listPriority = db.getAllDataPriority(currentEmail);

                String[] nameListPriority=new String[listPriority.size()+1];
                nameListPriority[0] = "Select Priority";
                for(int i=1; i<=listPriority.size(); i++){
                    nameListPriority[i]=listPriority.get(i-1).getName(); //create array of name
                }
                ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListPriority);
                Spinner spnPriority = (Spinner) mView.findViewById(R.id.spnPriority_Note);
                spnPriority.setAdapter(priorityAdapter);
                spnPriority.setPrompt(nameListPriority[0]);

                //Lấy dữ liệu cho spinner Status
                listStatus = db.getAllDataStatus(currentEmail);

                String[] nameListStatus=new String[listStatus.size()+1];
                nameListStatus[0] = "Select Status";
                for(int i=1; i<=listStatus.size(); i++){
                    nameListStatus[i]=listStatus.get(i-1).getName(); //create array of name
                }
                ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nameListStatus);
                Spinner spnStatus = (Spinner) mView.findViewById(R.id.spnStatus_Note);
                spnStatus.setAdapter(statusAdapter);
                spnStatus.setPrompt(nameListStatus[0]);


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

                Button add = (Button) mView.findViewById(R.id.btnAdd_Note);
                Button setPlandate = (Button) mView.findViewById(R.id.btn3cham_Note);
                TextView textViewPlandate = (TextView) mView.findViewById(R.id.txtvTitle_Plandate);
                final String strPlandate = textViewPlandate.getText().toString();
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



                final EditText txtName = (EditText) mView.findViewById(R.id.etxtText_Note);
                final TextView txtPlandate = (TextView) mView.findViewById(R.id.txtvTitle_Plandate);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                db = new DatabaseHelper(getActivity());
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String currentTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss aa", new Date());

                        String s1 = txtName.getText().toString();
                        String s2 = spnCategory.getSelectedItem().toString();
                        String s3 = spnPriority.getSelectedItem().toString();
                        String s4 = spnStatus.getSelectedItem().toString();
                        String s5 = txtPlandate.getText().toString();
                        String s6 = currentTime.toString();
                        if (s1.equals("") || s2.equals(nameListCategory[0]) || s3.equals(nameListPriority[0]) || s4.equals(nameListStatus[0]) || s5.equals(strPlandate)) {
                            builder1.setMessage("Không được để trống");
                        }
                        else
                        {
                            boolean insert = db.insertNote(currentEmail, s1, s2, s3, s4, s5, s6);
                            if (insert == true)
                                isOK = true;
                            else
                                builder1.setMessage("Không hợp lệ");
                            listNote= db.getAllDataNote(currentEmail);
                            ListView list = (ListView) root.findViewById(R.id.lstvNote);
                            noteAdapter = new NoteAdapter(getActivity(), R.layout.row_note, listNote);
                            list.setAdapter(noteAdapter);
                            dialog.dismiss();
                        }
                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog alert = builder1.create();
                        if (isOK == false)
                            alert.show();
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
        });
        return root;
    }
}