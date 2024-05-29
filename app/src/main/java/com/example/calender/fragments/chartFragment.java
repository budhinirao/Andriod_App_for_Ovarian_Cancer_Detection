package com.example.calender.fragments;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import com.example.calender.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class chartFragment extends Fragment {

    private Spinner spinnerSymptom;
    private Button btnFromDate, btnToDate, btnGetTrend;
    private Date fromDate, toDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        spinnerSymptom = view.findViewById(R.id.spinnerSymptom);
        btnFromDate = view.findViewById(R.id.btnFromDate);
        btnToDate = view.findViewById(R.id.btnToDate);
        btnGetTrend = view.findViewById(R.id.btnGetTrend);
        FrameLayout chartContainer = view.findViewById(R.id.chartContainer);

        setupSpinner();
        setupDatePickers();

        btnGetTrend.setOnClickListener(v -> plotData(chartContainer));

        return view;
    }

    private void setupSpinner() {
        List<String> symptoms = new ArrayList<>();
        symptoms.add("Bloating");
        symptoms.add("Pain");
        symptoms.add("Bleeding");
        symptoms.add("Discharge");
        symptoms.add("Moody");
        symptoms.add("Headache");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, symptoms);
        spinnerSymptom.setAdapter(adapter);
    }

    private void setupDatePickers() {
        btnFromDate.setOnClickListener(v -> showDatePickerDialog(true));
        btnToDate.setOnClickListener(v -> showDatePickerDialog(false));
    }

    private void showDatePickerDialog(boolean isFromDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);
            Date date = selectedDate.getTime();
            if (isFromDate) {
                fromDate = date;
                btnFromDate.setText(sdf.format(date));
            } else {
                toDate = date;
                btnToDate.setText(sdf.format(date));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void plotData(FrameLayout chartContainer) {
        if (fromDate == null || toDate == null) {
            // Optionally show an error message or a toast
            return;
        }
        List<Entry> entries = fetchSymptomData(spinnerSymptom.getSelectedItem().toString(), fromDate, toDate);
        LineChart chart = new LineChart(getContext());
        LineDataSet dataSet = new LineDataSet(entries, "Symptom Trend");
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chartContainer.removeAllViews();  // Clear old chart views
        chartContainer.addView(chart);  // Add new chart to the layout
        chart.invalidate(); // Refresh the chart
    }
    private List<Entry> fetchSymptomData(String symptom, Date fromDate, Date toDate) {
        List<Entry> entries = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            FileInputStream fis = getContext().openFileInput("symptoms_data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            int xValue = 0; // This will be our X-axis index

            while ((line = reader.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);
                String dateStr = jsonObject.getString("date");
                Date date = sdf.parse(dateStr);

                if ((date.equals(fromDate) || date.after(fromDate)) && (date.equals(toDate) || date.before(toDate))) {
                    JSONArray symptomsArray = jsonObject.getJSONArray("symptoms");
                    for (int i = 0; i < symptomsArray.length(); i++) {
                        if (symptomsArray.getString(i).equals(symptom)) {
                            // Count occurrences on each date
                            entries.add(new Entry(xValue++, 1)); // Assuming each line represents a day
                        }
                    }
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // have to handle the situation where entries are empty
        if (entries.isEmpty()) {
            entries.add(new Entry(0, 0)); // To show that no data is available
        }

        return entries;
    }

}
