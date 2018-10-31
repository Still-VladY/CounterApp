package com.example.vgubarev.testapp.DatebaseParsing;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vgubarev.testapp.Main.MainFrame;

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
import java.util.Arrays;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class DbUpd {


    public void getCountWithDate(final TextView textView, final Integer sql, final String date1, final String date2,
                                final String cou, final String number, final Boolean fact_visitors) {

        @SuppressLint("StaticFieldLeak")
        class SendPostRequest extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
            }

            protected String doInBackground(String... arg0) {

                try {

                    URL url = new URL("http://46.149.225.24:8081/counter/testing.php");

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("sql", sql);
                    postDataParams.put("start", date1);
                    postDataParams.put("end", date2);
                    postDataParams.put("getcount", cou);

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
                    loadIntoTextView(result, textView, number, fact_visitors);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        SendPostRequest getJSON = new SendPostRequest();
        getJSON.execute();
    }

    public void getCountWithoutDate(final TextView textView, final Integer sql, final String number, final Boolean bool) {

        @SuppressLint("StaticFieldLeak")
        class SendPostRequest extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
            }

            protected String doInBackground(String... arg0) {

                try {

                    URL url = new URL("http://46.149.225.24:8081/counter/testing.php");

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
                    loadIntoTextView(result, textView, number, bool);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        SendPostRequest getJSON = new SendPostRequest();
        getJSON.execute();
    }

    public void postCount(final Integer sql, final String date1, final String date2,
                         final String cou, final String newCou) {

        @SuppressLint("StaticFieldLeak")
        class SendPostRequest extends AsyncTask<String, Void, String> {

            protected void onPreExecute() {
            }

            protected String doInBackground(String... arg0) {

                try {

                    URL url = new URL("http://46.149.225.24:8081/counter/testing.php");

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("sql", sql);
                    postDataParams.put("start", date1);
                    postDataParams.put("end", date2);
                    postDataParams.put("getcount", cou);
                    postDataParams.put("newcount", newCou);

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
            }
        }
        SendPostRequest getJSON = new SendPostRequest();
        getJSON.execute();
    }

    private String getPostDataString(JSONObject params) throws Exception {

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

    private void loadIntoTextView(String json, TextView textView, String number, Boolean fact_visitors) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            String count = obj.getString(number);
            if (!count.equals("null")) {
                if (fact_visitors) {
                    int newCount = Integer.valueOf(count) / 2;
                    String rightCount = String.valueOf(newCount);
                    textView.append(rightCount);
                } else textView.append(count);
            } else {
                count = "Нет показаний за эту дату";
                textView.append(count);
            }
        }
    }
}
