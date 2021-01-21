package com.example.notemanagersystem.ui.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notemanagersystem.R;
import com.example.notemanagersystem.ui.category.Category;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private Context mContext;
    int mResource;

    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String category = getItem(position).getCategory();
        String priority = getItem(position).getPriority();
        String status = getItem(position).getStatus();
        String planDate = getItem(position).getPlanDate();
        String createDate = getItem(position).getCreateDate();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.name_RowNote);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.category_RowNote);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.priority_RowNote);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.status_RowNote);
        TextView tvPlandate = (TextView) convertView.findViewById(R.id.plandate_RowNote);
        TextView tvCreatedate = (TextView) convertView.findViewById(R.id.createdate_RowNote);

        tvName.setText("Name: " + name);
        tvCategory.setText("Category: " + category);
        tvPriority.setText("Priority: " + priority);
        tvStatus.setText("Status: " + status);
        tvPlandate.setText("Plan date: " + planDate);
        tvCreatedate.setText("Created Date: " + createDate);

        return convertView;
    }



}
