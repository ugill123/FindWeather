package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText enterCityName;
    Button checkWeather;
    TextView cityName;
    TextView cityTemperature;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enterCityName = findViewById(R.id.enter_city);
        checkWeather = findViewById(R.id.check_weather);
        cityName = findViewById(R.id.city_name);
        cityTemperature = findViewById(R.id.temperature);
        description = findViewById(R.id.description);
        checkWeather.setOnClickListener(this);

        enterCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String checkEnterText = enterCityName.getText().toString().trim();
                checkWeather.setEnabled(!checkEnterText.isEmpty());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String result = null;

        DownloadTask task = new DownloadTask();

        try {


            result = task.execute("http://api.openweathermap.org/data/2.5/weather?q=Lahore Past key here").get();
            //  task.onPostExecute(result);
            parsJsonData(result);
            parseJsonTemperature(result);


        } catch (ExecutionException e) {
            Toast.makeText(this, "Could not find Weather", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(this, "Could not find Weather", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    public void parsJsonData(String result) {

        String weather = "";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            String info = jsonObject.getString("weather");
            Log.i("info", info);


            JSONArray array = new JSONArray(info);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonSubPart = array.getJSONObject(i);
                weather = jsonSubPart.getString("main");


            }
        } catch (JSONException e) {
            Toast.makeText(this, "Could not find Weather", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        description.setText(weather);

    }

    public void parseJsonTemperature(String result) {
        Double temperature;
        JSONObject jsonObject;

        try {

            jsonObject = new JSONObject(result);

            JSONObject object2 = jsonObject.getJSONObject("main");

            temperature = (Double) object2.get("temp_max");
            temperature = temperature - 273.15;
            int convertTemperatureInt = temperature.intValue();
            cityTemperature.setText(convertTemperatureInt + "\u00B0");


        } catch (JSONException e) {
            Toast.makeText(this, "Could not find Weather", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        // cityTemperature.setText(""+temperature);


    }


    @Override
    public void onClick(View v) {


        if (v == checkWeather) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(cityName.getWindowToken(), 0);


            cityName.setText(enterCityName.getText().toString());
            String encodeCityName = null;
            try {
                encodeCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "Could not find Weather", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            String result = null;
            DownloadTask task = new DownloadTask();


            try {
                result = task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodeCityName + "past key here").get();
            } catch (ExecutionException e) {
                Toast.makeText(this, "Could not find Weather", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (InterruptedException e) {
                Toast.makeText(this, "Could not find Weather", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            //  task.onPostExecute(result);
            parsJsonData(result);
            parseJsonTemperature(result);


        }


    }


}
