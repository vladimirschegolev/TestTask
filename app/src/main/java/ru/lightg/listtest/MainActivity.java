package ru.lightg.listtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Line[] lines;
    private String[] names = {"id", "first name", "last name", "data size"};
    private Info info;

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return lines;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                info.setText(String.format("ID: %d%nFirst name: %s%nLast name: %s%nData size: %d", lines[i].id, lines[i].firstName, lines[i].lastName, lines[i].data.length));
                info.show(getFragmentManager(),"info");
            }
        });

        Spinner spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
        spinnerSort.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names));
        spinnerSort.setSelection(0);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortBy(position);
                listView.invalidateViews();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        info = new Info();

        if (savedInstanceState == null) {
            new ParseTask().execute();
        } else {
            lines = (Line[]) getLastCustomNonConfigurationInstance();
            listView.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.list_item, lines));
        }

    }

    private void sortBy(final int position) {
        if (lines != null) {
            Arrays.sort(lines, new Comparator<Line>() {
                @Override
                public int compare(Line i1, Line i2) {
                    switch (position) {
                        case 0:
                            return i1.id - i2.id;
                        case 1:
                            return i1.firstName.compareTo(i2.firstName);
                        case 2:
                            return i1.lastName.compareTo(i2.lastName);
                        case 3:
                            return i1.data.length - i2.data.length;
                    }
                    return 0;
                }
            });
        }
    }


    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://www.mocky.io/v2/583e07b51200003d14c045a5");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String input) {
            try {
                JSONParser parser = new JSONParser();
                JSONArray array = (JSONArray) parser.parse(input);
                lines = new Line[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    lines[i] = new Line((JSONObject)array.get(i));
                }
                listView.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.list_item, lines));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }
}
