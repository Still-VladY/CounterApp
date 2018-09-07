package com.example.vgubarev.testapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        DatePicker datePicker = findViewById(R.id.dateCount);
        EditText editText = findViewById(R.id.visitCountEdit);
        TextView tv = new TextView(this);

        String day = Integer.toString(datePicker.getDayOfMonth());
        String month = Integer.toString(datePicker.getMonth()+1);
        String year = Integer.toString(datePicker.getYear());
        if (editText.length()>0) {
            DbUpd db = new DbUpd();
            db.getCountWithDate(tv,999, year+"-"+month+"-"+day, null, editText.getText().toString(),"count");
            Intent intent = new Intent(this, MainFrame.class);
            startActivity(intent);
        } else {
            Context context = MainActivity.this;
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle("Внимание!");  // заголовок
            ad.setMessage("Не введено количество посетителей за выбранную дату"); // сообщение
            String button1String = "OK";
            ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                }
            });
            ad.show();
        }
    }
}
