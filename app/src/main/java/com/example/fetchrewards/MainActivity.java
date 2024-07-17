package com.example.fetchrewards;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @brief This class sets up a user interface that includes a TableLayout for
 * displaying items, an EditText for filtering items, a ProgressBar for
 * indicating data loading, and a list of Item objects (allItems and
 * filteredItems) to manage data.
 *
 * @details Fetch Data Task Executes an asynchronous task to retrieve data,
 * presumably from a remote server or database.
 *
 * Filtring Defines an applyFilter() method to filter Item objects based on user
 * input in filterEditText aka searchbar.
 *
 */
public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private EditText filterEditText;
    private ProgressBar progressBar;
    private List<Item> allItems = new ArrayList<>();
    private List<Item> filteredItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        filterEditText = findViewById(R.id.filterEditText);
        Button filterButton = findViewById(R.id.filterButton);
        progressBar = findViewById(R.id.progressBar);

        // Fetch data
        // Show progress bar while fetching data
        progressBar.setVisibility(View.VISIBLE);
        new FetchDataTask(this).execute();

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });
    }

    /**
     * @brief The purpose of updateList is to synchronize the application's
     * internal lists (allItems and filteredItems) with new data fetched from an
     * external source. It ensures that both lists are updated simultaneously
     * and then triggers a UI update to reflect the changes to the user.
     *
     * @param items
     * @return public
     */
    public void updateList(List<Item> items) {
        allItems.clear();
        allItems.addAll(items);
        filteredItems.clear();
        filteredItems.addAll(items);
        displayItems(filteredItems);
        progressBar.setVisibility(View.GONE);
    }

    private void displayItems(List<Item> items) {
        tableLayout.removeAllViews();
        addTableHeader();
        for (Item item : items) {
            TableRow row = new TableRow(this);
            TextView listIdView = new TextView(this);
            TextView nameView = new TextView(this);

            listIdView.setText(String.valueOf(item.getListId()));
            nameView.setText(item.getName());

            listIdView.setBackgroundResource(R.drawable.table_row_border);
            nameView.setBackgroundResource(R.drawable.table_row_border);

            listIdView.setTextSize(18);
            nameView.setTextSize(18);

            listIdView.setGravity(Gravity.CENTER);
            nameView.setGravity(Gravity.CENTER);

            row.addView(listIdView);
            row.addView(nameView);

            tableLayout.addView(row);
        }
    }

    /**
     * @brief The addTableHeader function sets up and adds a header row
     * (TableRow) to a TableLayout in an Android application. addTableHeader is
     * to dynamically create and populate a header row for a table within an
     * Android application's UI. This header row typically provides labels for
     * columns and enhances the visual organization and readability of tabular
     * data presented to the user.
     *
     * @return private
     */
    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        TextView listIdHeader = new TextView(this);
        TextView nameHeader = new TextView(this);

        listIdHeader.setText("List ID");
        nameHeader.setText("Name");

        listIdHeader.setBackgroundResource(R.drawable.table_row_border);
        nameHeader.setBackgroundResource(R.drawable.table_row_border);

        listIdHeader.setTextSize(18);
        nameHeader.setTextSize(18);

        listIdHeader.setGravity(Gravity.CENTER);
        nameHeader.setGravity(Gravity.CENTER);

        headerRow.addView(listIdHeader);
        headerRow.addView(nameHeader);

        tableLayout.addView(headerRow);
    }

    /**
     * @brief applyFilter is to dynamically filtering and displaying Item
     * objects in the UI based on user input. It provides visual feedback to the
     * user (progressBar) during the filtering process and updates the UI
     * (tableLayout) with the filtered results once the filtering operation is
     * complete.
     *
     * @return private
     */
    private void applyFilter() {
        progressBar.setVisibility(View.VISIBLE);
        tableLayout.removeAllViews();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String filterText = filterEditText.getText().toString().trim().toLowerCase();
                filteredItems = allItems.stream()
                        .filter(item -> item.getName().toLowerCase().trim().contains(filterText))
                        .collect(Collectors.toList());
                displayItems(filteredItems);

                // Hide progress bar after filtering is done
                progressBar.setVisibility(View.GONE);
            }
        }, 500); // Simulated filtering delay of 2 seconds
    }
}