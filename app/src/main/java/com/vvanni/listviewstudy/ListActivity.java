package com.vvanni.listviewstudy;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ListActivity extends android.app.ListActivity {

    //List of array strings which will serve as list items
    ArrayList<String> listItems = new ArrayList<String>();

    //Defining a string adapter which will handle the data of the ListView
    ArrayAdapter<String> adapter;

    //Recording how many times the button has been clicked
    int clickCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if (adapter == null) {
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            setListAdapter(adapter);
        }


    }

    //Method which will handle dynamic insertion
    public void addItems(View v)
    {
        listItems.add("Clicked : " + clickCounter++);
        adapter.notifyDataSetChanged();
    }
}
