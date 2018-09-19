package com.example.vgubarev.testapp.Main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vgubarev.testapp.DatebaseParsing.DbUpd;
import com.example.vgubarev.testapp.R;
import com.example.vgubarev.testapp.Starting.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public void onClick(View v) {
        showProgressDialog();
        DatePicker datePicker = findViewById(R.id.dateCount);
        EditText editText = findViewById(R.id.visitCountEdit);
        TextView tv = new TextView(this);

        String day = Integer.toString(datePicker.getDayOfMonth());
        String month = Integer.toString(datePicker.getMonth() + 1);
        String year = Integer.toString(datePicker.getYear());

        String str = editText.getText().toString();
        String[] numbers = str.split("-");

        int res = 0;


        for (String s : numbers) {
            res += Integer.parseInt(s);
        }

        if (editText.length()>0) {
            DbUpd db = new DbUpd();
            db.getCountWithDate(tv,999, year+"-"+month+"-"+day, null, String.valueOf(res),"count");
            Intent intent = new Intent(this, MainFrame.class);
            hideProgressDialog();
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
