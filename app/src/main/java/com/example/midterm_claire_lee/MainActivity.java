package com.example.midterm_claire_lee;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText inputNumber;
    private ArrayList<String> tableList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputNumber = findViewById(R.id.inputNumber);
        Button btnInsert = findViewById(R.id.btnInsert);
        ListView listView = findViewById(R.id.listView);

        tableList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableList);
        listView.setAdapter(adapter);

        // Restore last session (optional)
        if (!DataStore.getCurrentResults().isEmpty()) {
            tableList.addAll(DataStore.getCurrentResults());
            adapter.notifyDataSetChanged();
            Integer last = DataStore.getLastNumber();
            if (last != null) inputNumber.setText(String.valueOf(last));
        }

        btnInsert.setOnClickListener(v -> {
            String text = inputNumber.getText().toString().trim();
            if (text.isEmpty()) {
                inputNumber.setError("Enter a number");
                return;
            }

            int num;
            try { num = Integer.parseInt(text); }
            catch (NumberFormatException e) {
                inputNumber.setError("Invalid number");
                return;
            }

            tableList.clear();
            for (int i = 1; i <= 10; i++) {
                tableList.add(String.valueOf(num * i));
            }
            adapter.notifyDataSetChanged();

            DataStore.setCurrentTable(num, new ArrayList<>(tableList));
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String item = tableList.get(position);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete this value?")
                    .setMessage(item)
                    .setPositiveButton("Delete", (d, w) -> {
                        tableList.remove(position);
                        adapter.notifyDataSetChanged();
                        DataStore.updateCurrent(new ArrayList<>(tableList));
                        Toast.makeText(MainActivity.this, "Deleted: " + item, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        } else if (id == R.id.action_clear_all) {
            if (tableList.isEmpty()) return true;
            new AlertDialog.Builder(this)
                    .setTitle("Clear all?")
                    .setMessage("Remove all values from the current list.")
                    .setPositiveButton("Clear", (d, w) -> {
                        tableList.clear();
                        adapter.notifyDataSetChanged();
                        DataStore.clearCurrent();
                        Toast.makeText(this, "All rows cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
