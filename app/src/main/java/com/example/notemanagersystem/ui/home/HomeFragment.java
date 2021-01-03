package com.example.notemanagersystem.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.notemanagersystem.DatabaseHelper;
import com.example.notemanagersystem.R;
import com.example.notemanagersystem.ui.note.Note;
import com.example.notemanagersystem.ui.status.Status;
import static com.example.notemanagersystem.DashBoard.currentEmail;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    AnyChartView anyChartView;

    String[] month = {"Jan", "Feb", "Mar", "Apr"};
    int[] earnings = {500, 800, 2000, 500};

    DatabaseHelper db ;
    List<Status> listStatus;
    List<Note> listNote;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        anyChartView = root.findViewById(R.id.char_Dashboar);

        db = new DatabaseHelper(getActivity());
        //Lấy dữ liệu Status
        listStatus = db.getAllDataStatus(currentEmail);
        String[] nameListStatus = new String[listStatus.size()];
        int[] valueListStatus = new int[listStatus.size()];
        int sum = 0;

        for(int i=0; i<listStatus.size(); i++){
            nameListStatus[i]=listStatus.get(i).getName(); //create array of name
            valueListStatus[i]=db.getValueByStatus(nameListStatus[i]);
            sum += valueListStatus[i];
        }
        setupPieChart(nameListStatus, valueListStatus, sum);

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