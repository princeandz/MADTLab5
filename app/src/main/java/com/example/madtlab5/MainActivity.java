package com.example.madtlab5;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataLoader.DataLoaderCallback {

    private ListView currencyListView;
    private EditText filterEditText;
    private ProgressBar loadingProgressBar;
    private ArrayAdapter<String> adapter;
    private List<String> displayList = new ArrayList<>();
    private List<CurrencyRate> currencyRates = new ArrayList<>(); // List to hold parsed data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        currencyListView = findViewById(R.id.currencyListView);
        filterEditText = findViewById(R.id.filterEditText);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        // Start data loading
        String apiUrl = "https://www.floatrates.com/daily/usd.json"; // You can change this to an XML URL if needed
        new DataLoader(this, this).execute(apiUrl);

        // Set up filtering for the EditText
        setupFilter();
    }

    // DataLoaderCallback implementation
    @Override
    public void onDataLoadingStarted() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataLoaded(String data) {
        loadingProgressBar.setVisibility(View.GONE);

        // Parse the data (choose between JSON and XML parsing as needed)
        Parser parser = new Parser();
        currencyRates = parser.parseJson(data); // Use parseXml(data) for XML data

        // Prepare data for display in ListView
        displayList.clear();
        for (CurrencyRate rate : currencyRates) {
            displayList.add(rate.getCode() + " - " + rate.getRate());
        }

        // Set up the adapter to display data in ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        currencyListView.setAdapter(adapter);
    }

    @Override
    public void onDataLoadingFailed() {
        loadingProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Failed to load data. Please check your connection.", Toast.LENGTH_LONG).show();
    }

    // Method to set up filtering functionality
    private void setupFilter() {
        filterEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) { }
        });
    }

    // Filter the currency list based on user input
    private void filterList(String query) {
        List<String> filteredList = new ArrayList<>();
        for (CurrencyRate rate : currencyRates) {
            if (rate.getCode().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(rate.getCode() + " - " + rate.getRate());
            }
        }

        // Update ListView with the filtered list
        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}