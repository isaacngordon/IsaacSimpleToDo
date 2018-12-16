package com.example.isaacgordon.isaacsimpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import static com.example.isaacgordon.isaacsimpletodo.MainActivity.ITEM_POSITION;
import static com.example.isaacgordon.isaacsimpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {


    //track edit text
    EditText etItemText;
    //position of the text
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //resolve editText
        etItemText = (EditText) findViewById(R.id.etItemText);
        //set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        //update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION,0);
        //update title bar
        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v){
        //prepare new intent for result
        Intent i = new Intent();
        //pass updated item text as an extra
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());
        //pass the original position
        i.putExtra(ITEM_POSITION, position);
        //set the intent as the result pf the activity
        setResult(RESULT_OK,i);
        //close the activity and return to main
        finish();
    }

}
