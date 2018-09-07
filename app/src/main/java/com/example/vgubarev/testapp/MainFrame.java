package com.example.vgubarev.testapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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


public class MainFrame extends AppCompatActivity {

    private AlertDialog.Builder ad;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_frame);
        TextView textViewCurr = findViewById(R.id.textViewCurrMonth);
        TextView textViewPast = findViewById(R.id.textViewPastMonth);
        TextView textView10Days = findViewById(R.id.textViewTenDays);
        TextView textView30Days = findViewById(R.id.textViewThirtyDays);
        final ListView listView = findViewById(R.id.listView);


        DbUpd upd = new DbUpd();
        upd.getJSON(textViewCurr, 99, null, null, null, "count");
        upd.getJSON(textViewPast, 98, null, null, null, "count");
        upd.getJSON(textView10Days, 10, null, null, null, "count");
        upd.getJSON(textView30Days, 30, null, null, null, "count");
        getJSON(listView, 777, "count", "current");
        final Intent intent = new Intent(this, MainFrame.class);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
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
                final String count = selectedFromList.substring(12);

                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);  // заголовок
                ad.setMessage(message); // сообщение
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.setNeutralButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        DbUpd db = new DbUpd();
                        db.postJSON(111, date, null, count, null);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Удалено!", Toast.LENGTH_LONG);
                    }
                });
                ad.setNegativeButton(button3String, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.date_new_dialog);
                        dialog.setTitle("Редактирование");

                        Button closeButton = (Button) dialog.findViewById(R.id.closeNewDateBtn);
                        Button okButton = (Button) dialog.findViewById(R.id.okNewDateBtn);

                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.newDatePicker);
                                EditText editText = (EditText) dialog.findViewById(R.id.editNewDate);
                                String day = Integer.toString(datePicker.getDayOfMonth());
                                String month = Integer.toString(datePicker.getMonth() + 1);
                                String year = Integer.toString(datePicker.getYear());
                                DbUpd db = new DbUpd();
                                db.postJSON(112, date, year + "-" + month + "-" + day, count, editText.getText().toString());
                                startActivity(intent);
                            }
                        });

                        dialog.show();
                    }
                });

                ad.setCancelable(true);
                ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                Bundle arg = getIntent().getExtras();
                if (arg != null) {
                    Boolean check = arg.getBoolean("check");

                    //ad.setTitle(String.valueOf(check));

                    if (check) {
                        ad.show();
                    }
                } else ad.show();
            }
        });

    }

    public void insertBtn(View v) {
        Bundle arg = getIntent().getExtras();
        if (arg!=null) {
            Boolean check = arg.getBoolean("check");
            if (check) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {

                AlertDialog.Builder adialog = new AlertDialog.Builder(this);
                adialog.setTitle("Внимание!").setMessage("Данная учетная запись только для просмотра.").setCancelable(true)
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

            }
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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


    private void getJSON(final ListView listView, final Integer sql, final String number, final String date) {

        class SendPostRequest extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
            }

            protected String doInBackground(String... arg0) {

                try {

                    URL url = new URL("http://46.149.225.24:8081/counter/testing.php");

                    //URL url = new URL("http://192.168.100.23:8081/counter/testing.php");
                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("sql", sql);

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

                        StringBuffer sb = new StringBuffer("");
                        String line;

                        while ((line = in.readLine()) != null) {

                            sb.append(line);
                            break;
                        }

                        in.close();
                        return sb.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }

            @Override
            protected void onPostExecute(String result) {

                try {
                    loadIntoTextView(result, listView, number, date);
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

    private void loadIntoTextView(String json, ListView listView, String number, String date) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] mass = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String inputDate = obj.getString(date).substring(0, 10);
            String getYear = inputDate.substring(0, 4);
            String getMonth = inputDate.substring(5, 7);
            String getDay = inputDate.substring(8, 10);
            String getCount = obj.getString(number);

            mass[i] = "" + getDay + "." + getMonth + "." + getYear;
            mass[i] += ": " + getCount;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item_view, mass);
        listView.setAdapter(arrayAdapter);
    }
}

