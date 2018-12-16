package com.example.isaacgordon.isaacsimpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    //a numeric code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;
    //keys used for passinf data between activiations
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";


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

        //set up item lsitener for long clicks to remoe items from the list
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

        //set up item click listener for edits
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //create a new activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // pass data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //display the editActivity
                startActivityForResult(i, EDIT_REQUEST_CODE);
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

    //handler for results from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit was completed okay
        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            //extract updated item text from the result intentextras
            String updatedText = data.getExtras().getString(ITEM_TEXT);
            //extract origional position
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model
            items.set(position, updatedText);
            //notify adapater that the model changed
            itemsAdapter.notifyDataSetChanged();
            //persist changed model
            writeItems();
            //notify user
            Toast.makeText(this, "Items updated", Toast.LENGTH_SHORT).show();
        }
    }
}
