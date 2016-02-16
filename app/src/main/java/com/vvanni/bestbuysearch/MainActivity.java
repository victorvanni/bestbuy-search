package com.vvanni.bestbuysearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

//The MainActivity just wait for the Search Button to be pressed
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //that would be the buttons.xml
    }

    public void search(View view)
    {//id.search_button onClick method
        Intent intent = new Intent(this, SearchActivity.class);
        EditText editText = (EditText) findViewById(R.id.search_field);
        String searchText = editText.getText().toString();
        //the search text that we need to execute on BestBuy API
        intent.putExtra("SEARCH_MESSAGE", searchText);
        startActivity(intent);
    }


}

