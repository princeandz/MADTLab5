package com.example.madtlab5;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader extends AsyncTask<String, Void, String> {

    // Context for displaying error messages
    private Context context;
    private DataLoaderCallback callback;

    // Constructor to initialize context and callback
    public DataLoader(Context context, DataLoaderCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Show loading indicator if needed (e.g., in MainActivity)
        callback.onDataLoadingStarted();
    }

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urls[0]); // Get the URL from parameters
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000); // 10 seconds timeout
            urlConnection.setReadTimeout(10000);

            // Read data from the stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null) {
            // Pass the downloaded data to MainActivity for parsing and display
            callback.onDataLoaded(result);
        } else {
            // Show error message if data loading failed
            Toast.makeText(context, "Failed to load data. Please check your connection.", Toast.LENGTH_LONG).show();
            callback.onDataLoadingFailed();
        }
    }

    // Callback interface to communicate with MainActivity
    public interface DataLoaderCallback {
        void onDataLoadingStarted();
        void onDataLoaded(String data);
        void onDataLoadingFailed();
    }
}

