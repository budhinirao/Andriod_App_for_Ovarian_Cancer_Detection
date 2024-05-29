package com.example.calender.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;
import java.util.List;
import com.example.calender.R;

public class SymptomSelectionFragment extends BottomSheetDialogFragment {
    public interface SymptomSelectionListener {
        void onSymptomsSelected(List<String> selectedSymptoms, String note);
    }

    private SymptomSelectionListener mListener;

    public void setSymptomSelectionListener(SymptomSelectionListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_symptom_selection, container, false);

        Button btnCancel = v.findViewById(R.id.btnCancel);
        Button btnSave = v.findViewById(R.id.btnSave);

        CheckBox cbBloating = v.findViewById(R.id.cbBloating);
        CheckBox cbPain = v.findViewById(R.id.cbPain);
        CheckBox cbBleeding = v.findViewById(R.id.cbBleeding);
        CheckBox cbDischarge = v.findViewById(R.id.cbDischarge);
        CheckBox cbMoody = v.findViewById(R.id.cbMoody);
        CheckBox cbHeadache = v.findViewById(R.id.cbHeadache);
        EditText etNote = v.findViewById(R.id.etNote);

        btnCancel.setOnClickListener(view -> dismiss());

        btnSave.setOnClickListener(view -> {
            List<String> selectedSymptoms = new ArrayList<>();
            if (cbBloating.isChecked()) selectedSymptoms.add("Bloating");
            if (cbPain.isChecked()) selectedSymptoms.add("Pain");
            if (cbBleeding.isChecked()) selectedSymptoms.add("Bleeding");
            if (cbDischarge.isChecked()) selectedSymptoms.add("Discharge");
            if (cbMoody.isChecked()) selectedSymptoms.add("Moody");
            if (cbHeadache.isChecked()) selectedSymptoms.add("Headache");
            String note = etNote.getText().toString();

            if (mListener != null) {
                mListener.onSymptomsSelected(selectedSymptoms, note);
            }
            dismiss();
        });

        return v;
    }
}
