package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edcityName;
    Button btnSubmit;
    TextView tvResult;
    String cityName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edcityName = findViewById(R.id.city_name_et);
        btnSubmit = findViewById(R.id.submit_btn);
        tvResult = findViewById(R.id.result);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn) {
            cityName = edcityName.getText().toString();
            // Call the method to fetch UV index data
            fetchUVIndexData(cityName);
        }
    }

    // Method to fetch UV index data using AsyncTask
    private void fetchUVIndexData(String city) {
        URL url = NetworkUtils.buildUrl();
        new UVIndexTask().execute(url, city);
    }

    // AsyncTask to fetch UV index data
    private class UVIndexTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            URL url = (URL) params[0];
            String city = (String) params[1];
            String data = null;
            try {
                data = NetworkUtils.getDatafromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            setCityData(s);
        }
    }

    // Parse the UV index data and update the UI
    private void setCityData(String data) {
        try {
            JSONObject myObject = new JSONObject(data);
            JSONObject currentData = myObject.getJSONObject("current");
            String uvIndex = currentData.optString("uvi");

            if (!uvIndex.isEmpty()) {
                // Update the TextView with UV index
                tvResult.setText("UV Index for " + cityName + ": " + uvIndex);
                // TODO: Set an image based on the UV index value
                int imageResource = getImageResourceForUVIndex(Double.parseDouble(uvIndex));

                ImageView imageView = findViewById(R.id.uvImage);
                imageView.setImageResource(imageResource);
            } else {
                // UV index not found in the response
                tvResult.setText("UV Index data not available for " + cityName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error here
        }
    }

    private int getImageResourceForUVIndex(double uvIndexValue) {
        int[] uvResources = {
                R.drawable.uv0, R.drawable.uv1, R.drawable.uv2, R.drawable.uv3,
                R.drawable.uv4, R.drawable.uv5, R.drawable.uv6, R.drawable.uv7,
                R.drawable.uv8, R.drawable.uv9, R.drawable.uv10, R.drawable.uv11
        };

        int index = (int) Math.min(Math.floor(uvIndexValue), 11);
        return uvResources[index];
    }
}
