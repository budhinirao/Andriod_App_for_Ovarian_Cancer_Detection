package com.example.calender.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.calender.R;
import com.github.mikephil.charting.charts.LineChart;

public class GraphDisplayFragment extends Fragment {

    private LineChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph_display, container, false);
        chart = view.findViewById(R.id.chart); // Ensure you have a LineChart with this ID in your layout

        // Optionally, get data passed from chartFragment
        Bundle args = getArguments();
        if (args != null) {
            String selectedSymptom = args.getString("selectedSymptom");
            String selectedDateRange = args.getString("selectedDateRange");
            // Use this data to display the graph
        }

        return view;
    }
}
