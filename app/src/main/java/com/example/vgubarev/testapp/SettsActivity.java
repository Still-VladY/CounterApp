package com.example.vgubarev.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SettsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setts);
    }

    public void onEditedClick(View view) {
        boolean on = ((ToggleButton) view).isChecked(

        );

        if (on) {
            Intent intent = new Intent(this, MainFrame.class);
            startActivity(intent);
        }

    }
}
