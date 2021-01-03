package com.example.notemanagersystem.ui.category;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import static com.example.notemanagersystem.DashBoard.currentEmail;

import java.util.Date;
import java.util.List;

public class CategoryFragment extends Fragment {

    DatabaseHelper db ;
    CategoryAdapter adapter = null;
    List<Category> listCategory;
    boolean isOK = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_category, container, false);

        db = new DatabaseHelper(getActivity());
        listCategory = db.getAllDataCategory(currentEmail);
        ListView list = (ListView) root.findViewById(R.id.lstvCategory);
        adapter = new CategoryAdapter(getActivity(), R.layout.row_category, listCategory);
        list.setAdapter(adapter);

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
                        new AlertDialog.Builder(getActivity()).setTitle("Delete Category").setMessage("Bạn có muốn xóa hay không?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String name = listCategory.get(position).getName();
                                        db.deleteCategoryByName(name);
                                        //listCategory.remove(position);
                                        //adapter.notifyDataSetChanged();
                                        listCategory= db.getAllDataCategory(currentEmail);
                                        adapter = new CategoryAdapter(getActivity(), R.layout.row_category, listCategory);
                                        list.setAdapter(adapter);
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





        FloatingActionButton fab = root.findViewById(R.id.btnFab_Category);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Category Form");

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.customdialog_category, null);
                final EditText textDialog = (EditText) mView.findViewById(R.id.etxtText_Category);
                Button add = (Button) mView.findViewById(R.id.btnAdd_Category);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                db = new DatabaseHelper(getActivity());
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String currentTime = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss aa", new Date());

                        String s1 = textDialog.getText().toString();
                        String s2 = currentTime;
                        if (s1.equals(""))
                            builder1.setMessage("Không được để trống");
                        else
                        {
                            boolean insert = db.insert3Value("category", currentEmail, s1, s2);
                            if (insert == true)
                                isOK = true;
                            else
                                builder1.setMessage("Không hợp lệ");
                        }

                        listCategory= db.getAllDataCategory(currentEmail);
                        ListView list = (ListView) root.findViewById(R.id.lstvCategory);
                        adapter = new CategoryAdapter(getActivity(), R.layout.row_category, listCategory);
                        list.setAdapter(adapter);
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
                Button close = (Button) mView.findViewById(R.id.btnClose_Category);
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