package com.example.vgubarev.testapp.Main;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.vgubarev.testapp.DatebaseParsing.DbUpd;
import com.example.vgubarev.testapp.R;
import com.example.vgubarev.testapp.Starting.BaseActivity;

import java.util.Calendar;

public class CounterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        final DbUpd db = new DbUpd();
        final TextView tv = findViewById(R.id.text);

        DatePicker datePicker = findViewById(R.id.dateCount);

        Calendar calendar = Calendar.getInstance();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        showProgressDialog();
                        tv.setText("");
                        db.getCountWithDate(tv, 101, year+"-"+(month+1)+"-"+day,
                                year+"-"+(month+1)+"-"+day, null, "count", true);
                        hideProgressDialog();
                    }
                });
    }

}
