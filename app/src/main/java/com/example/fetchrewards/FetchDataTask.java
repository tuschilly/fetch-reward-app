package com.example.fetchrewards;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @brief FetchDataTask class is responsible for asynchronously fetching JSON
 * data from a specified URL, parsing the data into Item objects, sorting them
 * based on listId and name, and updating the UI with the sorted list in the
 * MainActivity
 *
 */
public class FetchDataTask extends AsyncTask<Void, Void, List<Item>> {
    // URL of the JSON data source
    private static final String DATA_URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    private MainActivity activity;

    /**
     * Constructor for FetchDataTask.
     *
     * @param activity The MainActivity instance to update UI after fetching data.
     */
    public FetchDataTask(MainActivity activity) {
        this.activity = activity;
    }
    /**
     * Background task to fetch and process JSON data.
     *
     * @param voids No parameters needed for this task.
     * @return List of Item objects parsed and sorted from JSON data.
     */
    @Override
    protected List<Item> doInBackground(Void... voids) {
        List<Item> itemList = new ArrayList<>();
        try {
            URL url = new URL(DATA_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JSONArray jsonArray = new JSONArray(result.toString());
            // Iterate through JSON array and parse each JSON object into Item object
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int listId = jsonObject.getInt("listId");
                String name = jsonObject.optString("name", "").trim();
                int id = jsonObject.getInt("id");

                if (!name.isEmpty()) {
                    itemList.add(new Item(listId, name, id));
                }
            }

            //List sorting by "listID"
            itemList.sort(new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    int listIdCompare = Integer.compare(o1.getListId(), o2.getListId());
                    if (listIdCompare != 0) {
                        return listIdCompare;
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
    /**
     * Method executed on UI thread after background task completes.
     *
     * @param items List of Item objects fetched and processed.
     */
    @Override
    protected void onPostExecute(List<Item> items) {
        activity.updateList(items);
    }
}

