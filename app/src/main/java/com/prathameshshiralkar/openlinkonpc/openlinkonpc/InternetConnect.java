package com.prathameshshiralkar.openlinkonpc.openlinkonpc;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Prathamesh Shiralkar
 */
public class InternetConnect extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;
    InternetConnect (Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        Integer num_params = 0;
        if(params[0] != null){num_params=Integer.parseInt(params[0]);}
        String login_url = params[1];
        String res = null;

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = "";
                for (int i = 1; i <= num_params; i++) {
                    post_data += (URLEncoder.encode(params[2 * i], "UTF-8") + "=" + URLEncoder.encode(params[2 * i + 1], "UTF-8"));
                    if (i != num_params) {
                        post_data += "&";
                    }
                }

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                res = result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Error";
            } catch (IOException e) {

                e.printStackTrace();
                return "Error";
            }
        return res;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Loading");
        alertDialog.setMessage("Loading");
        alertDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.cancel();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
