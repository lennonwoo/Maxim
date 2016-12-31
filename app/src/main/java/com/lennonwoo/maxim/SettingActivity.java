package com.lennonwoo.maxim;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lennonwoo.maxim.service.MaximService;

public class SettingActivity extends Activity implements View.OnClickListener {
    TextView startTime;
    TextView endTime;
    Switch toggle;
    Switch toggleRandom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_setting);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        toggle = (Switch) findViewById(R.id.maxim_toggle);
        toggleRandom = (Switch) findViewById(R.id.maxim_toggle_random);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(MaximPreference.PREF_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean(MaximPreference.MAXIM_TOGGLE, isChecked);
                editor.apply();
                Intent intent = new Intent(SettingActivity.this, MaximService.class);
                if (isChecked) startService(intent);
                else stopService(intent);
            }
        });

        toggleRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(MaximPreference.PREF_NAME, Context.MODE_PRIVATE).edit();
                editor.putBoolean(MaximPreference.MAXIM_TOGGLE_RANDOM, isChecked);
                editor.apply();
            }
        });

        init();
    }

    @Override
    public void onClick(View v) {
        final SharedPreferences.Editor editor = getSharedPreferences(MaximPreference.PREF_NAME, Context.MODE_PRIVATE).edit();
        switch (v.getId()) {
            case R.id.start_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String minuteOptimized = minute + "";
                        if (minute < 10)
                            minuteOptimized = "0" + minuteOptimized;
                        String s = hourOfDay + ":" + minuteOptimized;
                        startTime.setText(s);
                        editor.putString(MaximPreference.START_TIME, s);
                        editor.apply();
                    }
                }, 0, 0, true).show();
                break;
            case R.id.end_time:
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String minuteOptimized = minute + "";
                        if (minute < 10)
                            minuteOptimized = "0" + minuteOptimized;
                        String s = hourOfDay + ":" + minuteOptimized;
                        endTime.setText(s);
                        editor.putString(MaximPreference.END_TIME, s);
                        editor.apply();
                    }
                }, 0, 0, true).show();
                break;
            default:
                break;
        }
    }

    private void init() {
        SharedPreferences pref = getSharedPreferences(MaximPreference.PREF_NAME, Context.MODE_PRIVATE);
        boolean switchToggle = pref.getBoolean(MaximPreference.MAXIM_TOGGLE, false);
        boolean switchToggleRandom = pref.getBoolean(MaximPreference.MAXIM_TOGGLE_RANDOM, false);
        String textStartTime = pref.getString(MaximPreference.START_TIME, "0:00");
        String textEndTime = pref.getString(MaximPreference.END_TIME, "24:00");
        toggle.setChecked(switchToggle);
        toggleRandom.setChecked(switchToggleRandom);
        startTime.setText(textStartTime);
        endTime.setText(textEndTime);
    }
}
