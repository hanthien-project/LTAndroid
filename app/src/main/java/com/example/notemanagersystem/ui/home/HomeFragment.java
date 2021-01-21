package com.example.notemanagersystem.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.R;
import com.example.notemanagersystem.Table.User;
import com.example.notemanagersystem.ui.note.Note;
import com.example.notemanagersystem.ui.status.Status;
import com.example.notemanagersystem.ui.status.StatusAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.notemanagersystem.DashBoard.currentUserId;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    AnyChartView anyChartView;

    List<Status> statusList;
    List<Note> noteList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceNote;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        anyChartView = root.findViewById(R.id.char_Dashboar);

        statusList = new ArrayList<Status>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("statuses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusList = new ArrayList<Status>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Status status = dataSnapshot.getValue(Status.class);
                    if (status.getUserId().equals(currentUserId))
                        statusList.add(status);
                }
                noteList = new ArrayList<Note>();
                databaseReferenceNote = FirebaseDatabase.getInstance().getReference().child("notes");
                databaseReferenceNote.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        noteList = new ArrayList<Note>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Note note = dataSnapshot.getValue(Note.class);
                            if (note.getUserId().equals(currentUserId))
                                noteList.add(note);
                        }
                        String[] nameListStatus = new String[statusList.size()];
                        int[] valueListStatus = new int[statusList.size()];
                        int sum = 0;

                        for(int i=0; i<statusList.size(); i++){
                            nameListStatus[i]=statusList.get(i).getName(); //create array of name
                            int value = 0;
                            for(int j=0; j<noteList.size(); j++)
                                if (noteList.get(j).getStatus().equals(nameListStatus[i]))
                                    value++;
                            valueListStatus[i]=value;

                            sum += valueListStatus[i];
                        }
                        setupPieChart(nameListStatus, valueListStatus, sum);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    public void setupPieChart(String[] nameListStatus, int[] valueListStatus, int sum){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        if (sum != 0 ) {
            for (int i = 0; i < nameListStatus.length; i++) {
                dataEntries.add(new ValueDataEntry(nameListStatus[i], valueListStatus[i]));
            }
        }
        else
            dataEntries.add(new ValueDataEntry("OK", 1));
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }

}