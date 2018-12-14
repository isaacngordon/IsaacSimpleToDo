package com.example.isaacgordon.isaacsimpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //items = new ArrayList<>();
        readItems();;
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //mock data
        //items.add("ites 1");
        //items.add("item 2");

        setupListViewListener();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Items added to the list", Toast.LENGTH_SHORT).show();

    }

    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up listener");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from ist at " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                Toast.makeText(getApplicationContext(), "Items removed to the list", Toast.LENGTH_SHORT).show();
               return true;
            }
        });
    }
    private File getdataFile(){
        return new File(getFilesDir(), "todo.txt");

    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getdataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "IOException reading file.");
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getdataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "IOException writing file.");
        }
    }
}
