package com.example.vgubarev.testapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText etPass = findViewById(R.id.pass);
        etPass.setTransformationMethod(new PasswordTransformationMethod());

    }


    @SuppressLint("ShowToast")
    public void onLoginClick(View v) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("login.db", MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("CREATE TABLE IF NOT EXISTS users (name TEXT, pass TEXT)");
        db.execSQL("INSERT INTO users VALUES ('Admin', '9999');");
        db.execSQL("INSERT INTO users VALUES ('User', '1111');");

        EditText etLogin = findViewById(R.id.login);
        EditText etPass = findViewById(R.id.pass);


        Cursor query = db.rawQuery("SELECT * from users", null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        String name;
        String pass;

        query.moveToFirst();
        name = query.getString(0);
        pass = query.getString(1);
        if (etLogin.getText().length() > 0 && etPass.getText().length() > 0) {
            if (etLogin.getText().toString().equals(name)) {
                if (etPass.getText().toString().equals(pass)) {
                    Intent intent = new Intent(this, MainFrame.class);
                    intent.putExtra("check", true);
                    startActivity(intent);
                    //builder.setTitle("Ошибка!").setMessage(String.valueOf(getBool())).setCancelable(true);
                    //builder.create();
                    //builder.show();

                } else {
                    builder.setTitle("Ошибка!").setMessage("Неверный пароль").setCancelable(true);
                    builder.create();
                    builder.show();
                }
            } else {
                if (query.moveToFirst()) {
                    do {
                        name = query.getString(0);
                        pass = query.getString(1);
                        if (etLogin.getText().length() > 0 && etPass.getText().length() > 0) {
                            if (etLogin.getText().toString().equals(name)) {
                                if (etPass.getText().toString().equals(pass)) {
                                    //setBool(false);
                                    Intent intent = new Intent(this, MainFrame.class);
                                    intent.putExtra("check", false);
                                    startActivity(intent);
                                    //builder.setTitle("Ошибка!").setMessage(String.valueOf(getBool())).setCancelable(true);
                                    //builder.create();
                                    //builder.show();
                                } else {
                                    builder.setTitle("Ошибка!").setMessage("Неверный пароль").setCancelable(true);
                                    builder.create();
                                    builder.show();
                                }
                            }
                        }
                        } while (query.moveToNext());
                }
                return;
            }
        } else {
            builder.setTitle("Ошибка!").setMessage("Комбинация логина/пароля не распознана").setCancelable(true);
            builder.create();
            builder.show();
            return;
        }
        query.close();
        db.close();
    }
}
