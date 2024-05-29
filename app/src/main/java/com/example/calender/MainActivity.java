package com.example.calender;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.calender.fragments.AwarenessFragment;
import com.google.android.material.tabs.TabLayout;
import com.example.calender.fragments.adapters.ViewPagerAdapter;
import com.example.calender.fragments.CalendarFragment;
import com.example.calender.fragments.chartFragment; // Assuming ChartFragment is the correct class name

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpTabs();
    }

    private void setUpTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CalendarFragment(), "Calendar");
        adapter.addFragment(new AwarenessFragment(), "Info");
        adapter.addFragment(new chartFragment(), "Chart");

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tabs); // Make sure you have a TabLayout with the ID 'tabs' in your layout
        tabs.setupWithViewPager(viewPager);

        // Setting icons for each tab
        if (tabs.getTabAt(0) != null) {
            tabs.getTabAt(0).setIcon(R.drawable.baseline_edit_calendar_24);
        }
        if (tabs.getTabAt(1) != null) {
            tabs.getTabAt(1).setIcon(R.drawable.baseline_camera_24);
        }
        if (tabs.getTabAt(2) != null) {
            tabs.getTabAt(2).setIcon(R.drawable.baseline_auto_graph_24);
        }
    }
}
