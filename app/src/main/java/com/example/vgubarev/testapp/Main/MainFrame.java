package com.example.vgubarev.testapp.Main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vgubarev.testapp.DatebaseParsing.DbUpd;
import com.example.vgubarev.testapp.R;
import com.example.vgubarev.testapp.Settings.SettsActivity;
import com.example.vgubarev.testapp.Starting.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class MainFrame extends BaseActivity {

    private AlertDialog.Builder ad;
    private Context context;
    private FirebaseUser user;
    private StringBuilder strb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();


        setContentView(R.layout.activity_main_frame);
        final TextView textViewCurr = findViewById(R.id.textViewCurrMonth);
        TextView textViewPast = findViewById(R.id.textViewPastMonth);
        TextView textView10Days = findViewById(R.id.textViewTenDays);
        TextView textView30Days = findViewById(R.id.textViewThirtyDays);
        final ListView listView = findViewById(R.id.listView);

        DbUpd upd = new DbUpd();
        upd.getCountWithoutDate(textViewCurr, 99, "count", true);
        upd.getCountWithoutDate(textViewPast, 98, "count", true);
        upd.getCountWithoutDate(textView10Days, 10, "count", true);
        upd.getCountWithoutDate(textView30Days, 30, "count", true);
        getCountToListView(listView);
        final Intent intent = new Intent(this, MainFrame.class);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, final long id) {
                final DbUpd db = new DbUpd();
                String selectedFromList = (String) listView.getItemAtPosition(position);

                context = MainFrame.this;
                String title = "Посетители";
                String message = "Дата: " + selectedFromList.substring(0, 10) + "\nПосетителей: " + selectedFromList.substring(12);
                String button1String = "OK";
                String button2String = "Удалить";
                String button3String = "Изменить";
                String dateDialog = selectedFromList.substring(0, 10);
                String getDay = dateDialog.substring(0, 2);
                String getMonth = dateDialog.substring(3, 5);
                String getYear = dateDialog.substring(6, 10);
                final String date = getYear + "-" + getMonth + "-" + getDay;
                final TextView textView = findViewById(R.id.invisibleTV);
                textView.setText("");
                db.getCountWithDate(textView, 101, date, date, null, "count", false);

                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);  // заголовок
                ad.setMessage(message); // сообщение
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.setNeutralButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                        db.postCount(111, date, null, textView.getText().toString(), null);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Удалено!", Toast.LENGTH_LONG).show();
                    }
                });
                ad.setNegativeButton(button3String, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.date_new_dialog);
                        dialog.setTitle("Редактирование");

                        Button closeButton = dialog.findViewById(R.id.closeNewDateBtn);
                        Button okButton = dialog.findViewById(R.id.okNewDateBtn);

                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePicker datePicker = dialog.findViewById(R.id.newDatePicker);
                                EditText editText = dialog.findViewById(R.id.editNewDate);

                                if (editText.getText().length() > 1) {


                                    showProgressDialog();
                                    String str = editText.getText().toString();
                                    String[] numbers = str.split("-");

                                    int res = 0;

                                    for (String s : numbers) {
                                        res += Integer.parseInt(s);
                                    }

                                    String day = Integer.toString(datePicker.getDayOfMonth());
                                    String month = Integer.toString(datePicker.getMonth() + 1);
                                    String year = Integer.toString(datePicker.getYear());
                                    DbUpd db = new DbUpd();

                                    db.postCount(112, date, year + "-" + month + "-" + day, textView.getText().toString(), String.valueOf(res));
                                    hideProgressDialog();
                                    startActivity(intent);
                                } else Toast.makeText(getApplicationContext(), "Введите показания!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                    }
                });

                ad.setCancelable(true);
                ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) { }
                });

                DatabaseReference dbR = FirebaseDatabase.getInstance().getReference("users");
                dbR.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String val = dataSnapshot.child(user.getUid()).child("access").getValue(String.class);
                        if (val.equals("admin")) {
                            ad.show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void insertBtn(View v) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String val = dataSnapshot.child(user.getUid()).child("access").getValue(String.class);
                    if (val.equals("user")) {
                        AlertDialog.Builder adialog = new AlertDialog.Builder(MainFrame.this);
                        adialog.setTitle("Внимание!").setMessage("Операция требует повышения прав.").setCancelable(true)
                                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) { }
                                }).show();
                    } else if (val.equals("admin")) {
                        Intent intent = new Intent(MainFrame.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }


    public void updBtn(View v) {
        Intent intent = new Intent(this, CounterActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getCountToListView(final ListView listView) {

        @SuppressLint("StaticFieldLeak")
        class SendPostRequest extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
            }

            protected String doInBackground(String... arg0) {

                try {

                    URL url = new URL("http://46.149.225.24:8081/counter/testing.php");

                    //URL url = new URL("http://192.168.100.23:8081/counter/testing.php");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("sql", (Integer) 777);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000);

                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(new
                                InputStreamReader(
                                conn.getInputStream()));

                        StringBuilder sb = new StringBuilder("");
                        String line;

                        while ((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();
                        return sb.toString();

                    } else {
                        return "false : " + responseCode;
                    }
                } catch (Exception e) {
                    return "Exception: " + e.getMessage();
                }

            }

            @Override
            protected void onPostExecute(String result) {

                try {
                    loadIntoListView(result, listView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        SendPostRequest getJSON = new SendPostRequest();
        getJSON.execute();
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString().trim();
    }

    private void loadIntoListView(String json, ListView listView) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] mass = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String inputDate = obj.getString("current").substring(0, 10);
            String getYear = inputDate.substring(0, 4);
            String getMonth = inputDate.substring(5, 7);
            String getDay = inputDate.substring(8, 10);
            String getCount = obj.getString("count");
            int newCount = Integer.valueOf(getCount)/2;
            String rightCount = String.valueOf(newCount);

            mass[i] = "" + getDay + "." + getMonth + "." + getYear;
            mass[i] += ": " + rightCount;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item_view, mass);
        listView.setAdapter(arrayAdapter);
    }

    public void onDestroy() {
        moveTaskToBack(true);

        super.onDestroy();

        System.runFinalizersOnExit(true);
        System.exit(0);
    }
}

