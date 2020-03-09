package com.example.weather;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends AsyncTask<String,Void,String> {
    MainActivity mainActivity;






    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;
        HttpURLConnection urlConnection;
        try {
            url=new URL(urls[0]);
            urlConnection=(HttpURLConnection) url.openConnection();

            InputStream inputStream=urlConnection.getInputStream();
            InputStreamReader reader=new InputStreamReader(inputStream);

            int data=reader.read();

            while (data!=-1){
                char current=(char) data;
                result+=current;
                data=reader.read();



            }
            return result;







        } catch (MalformedURLException e) {
            Toast.makeText(mainActivity,"Could not find Weather",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        } catch (IOException e) {
            Toast.makeText(mainActivity,"Could not find Weather",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }


        return null;
    }


}
