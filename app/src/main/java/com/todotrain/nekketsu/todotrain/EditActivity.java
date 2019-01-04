package com.todotrain.nekketsu.todotrain;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    private TextView dateText;
    private TextView stationText;
    private TimePicker timePicker;

    //Schedule
    private TextView taskText;
    private Calendar calendar = Calendar.getInstance();
    private RailWay railWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dateText = findViewById(R.id.dateText);
        stationText = findViewById(R.id.stationText);
        taskText = findViewById(R.id.taskText);
        timePicker = findViewById(R.id.time_picker);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String str = String.format(Locale.JAPAN, "%d/%d/%d",year, monthOfYear+1, dayOfMonth);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateText.setText(str);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePick();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void selectStationDialog(View view){
        final String[] items = new String[ShareData.railWays.size()];
        for(int i=0; i<items.length; i++){
            RailWay railWay = ShareData.railWays.get(i);
            items[i] = railWay.jp_name;
        }
        new AlertDialog.Builder(this)
                .setTitle("日比谷線")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item_which pressed
                        stationText.setText(ShareData.railWays.get(which).jp_name);
                        railWay = ShareData.railWays.get(which);
                    }
                })
                .show();
    }

    public void DoneTapped(View view){
        if(!taskText.getText().toString().isEmpty() && railWay!=null){
            Schedule schedule = new Schedule();
            schedule.taskName = taskText.getText().toString();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
            calendar.set(Calendar.MINUTE, timePicker.getMinute());
            schedule.calendar = calendar;
            schedule.railWay = railWay;
            ShareData.schedules.add(schedule);
            finish();
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("未入力の欄があります")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
