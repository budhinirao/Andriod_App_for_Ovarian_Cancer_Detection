package com.example.calender.fragments;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;
import com.example.calender.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private Button addSymptomButton;
    private String selectedDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        addSymptomButton = view.findViewById(R.id.addSymptomButton);

        // Initialize selectedDate with the current date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date(calendarView.getDate()));

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Month value is 0-based. Add 1 for consistency.
            selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, month + 1, year);
            Toast.makeText(getActivity(), "Date selected: " + selectedDate, Toast.LENGTH_SHORT).show();
        });

        addSymptomButton.setOnClickListener(v -> showSymptomSelectionFragment());

        return view;
    }

    private void showSymptomSelectionFragment() {
        SymptomSelectionFragment fragment = new SymptomSelectionFragment();
        fragment.setSymptomSelectionListener((selectedSymptoms, note) -> {
            // Show a toast for demonstration, this can be replaced with actual functionality as needed
            String symptoms = String.join(", ", selectedSymptoms);
            Toast.makeText(getActivity(), "Selected Symptoms: " + symptoms + "\nNote: " + note, Toast.LENGTH_LONG).show();

            // Save symptoms and note to file
            saveSymptomsToFile(selectedDate, selectedSymptoms, note);
        });
        fragment.show(getChildFragmentManager(), fragment.getTag());
    }


    private void saveSymptomsToFile(String date, List<String> selectedSymptoms, String note) {
        FileOutputStream fos = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", date);
            jsonObject.put("symptoms", new JSONArray(selectedSymptoms));
            jsonObject.put("note", note); // Add the note here

            fos = getActivity().openFileOutput("symptoms_data.txt", Context.MODE_APPEND);
            String entry = jsonObject.toString() + "\n";
            fos.write(entry.getBytes());
            Toast.makeText(getActivity(), "Symptoms and note saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to save symptoms and note", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
