package com.vvanni.bestbuysearch;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends ListActivity implements AdapterView.OnItemClickListener {


    private BestBuySearch bbSearch;             //Variable to perform the API search
    private int nOfResults;                     //Number of results found
    private int offset = 0;                     //Position that is showing
    protected static final int PAGESIZE = 10;   //To easily change how many results per page
    protected TextView textViewHeader;          //TextView that holds the buttons and display msg
    protected boolean loading = false;          //to set the loading icon
    private String search;                      //What is searching for
    private ProgressBar progressBar;            //The loading icon
    private Button first;                       //Button to go to the first page
    private Button last;                        //Button to go to the last page
    private Button prev;                        //Button to go to the previous page
    private Button next;                        //Button to go to the next page
    ArrayList<Product> products;                //list of products to show

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search); //that would be the buttons.xml

        //instanciate every view
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewHeader = (TextView) findViewById(R.id.header);
        first = (Button) findViewById(R.id.buttonfirst);
        prev = (Button) findViewById(R.id.buttonprev);
        next = (Button) findViewById(R.id.buttonnext);
        last = (Button) findViewById(R.id.buttonlast);

        //instanciate other vars
        products = new ArrayList<>();
        bbSearch = new BestBuySearch();

        //intent to get what is searching for passed by the MainActivity
        Intent intent = getIntent();
        search = intent.getStringExtra("SEARCH_MESSAGE");

        //Setting up the adapter and the action to click on an Item
        setListAdapter(new ProductListAdapter(this, new ArrayList<Product>()));
        getListView().setOnItemClickListener(this);

        //async function to load the page
        (new LoadNextPage()).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        String item = adapter.getItemAtPosition(position).toString();
        //id is the row that I'm working on, also, the position of product in products.get(id)
        Intent intent = new Intent(this, FullItemActivity.class);

        //populate the intent with a lot of extras to show the specific product
        intent.putExtra("product_sku", products.get((int)id).sku);
        intent.putExtra("product_name", products.get((int)id).name);
        intent.putExtra("product_img", products.get((int) id).img_url_small);
        intent.putExtra("product_price", products.get((int) id).price);
        //In case there is no description, it will show "No description."
        if(products.get((int)id).descript.equals("null"))
        {
            intent.putExtra("product_descript", "No description.");
        } else if (products.get((int)id).descript.equals(null)) {
            intent.putExtra("product_descript", "no description.");
        } else {
            intent.putExtra("product_descript", products.get((int)id).descript);
        }

        //Starts the popup activity
        startActivity(intent);

    }

    private class LoadNextPage extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {//before loading the products it set to visible the loading icon
            progressBar.setVisibility(View.VISIBLE);
            loading = true;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0)
        {//here the urlconnection happens
            JSONObject jObj = new JSONObject();
            JSONArray jArr = new JSONArray();

            //set up the search
            bbSearch.setSearch(search);
            bbSearch.setPage(getCurrentPage());
            bbSearch.setPageSize(PAGESIZE);

            //try to get the results
            try{
                jObj = bbSearch.getApiSearch();
                nOfResults = jObj.getInt("total");
                jArr = jObj.getJSONArray("products");
                products = productsFromJson(jArr);
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {//after getting the search result the adapter is used to feed the ListView
            ProductListAdapter productListAdapter = ((ProductListAdapter) getListAdapter());
            productListAdapter.clear();

            for (Product product : products)
            {
                productListAdapter.add(product);
            }

            productListAdapter.notifyDataSetChanged();

            //updates the header with results info and the buttons
            updateDisplayingTextView();

            loading = false;
        }
    }

    protected void updateDisplayingTextView()
    {
        String text = getString(R.string.displayshort);
        text = String.format(text, Math.min(nOfResults, offset + 1),
                Math.min(offset + PAGESIZE, nOfResults), nOfResults);
        if(nOfResults == 0)
        {
            textViewHeader.setText("No results...");
        } else {
            textViewHeader.setText(text);
        }
        updateButtons();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void updateButtons()
    {//set the status of the buttons by the current page, first page and last page
        if (getCurrentPage() > 1) {
            first.setEnabled(true);
            prev.setEnabled(true);
        } else {
            first.setEnabled(false);
            prev.setEnabled(false);
        }
        if (getCurrentPage() < getLastPage()) {
            next.setEnabled(true);
            last.setEnabled(true);
        } else {
            next.setEnabled(false);
            last.setEnabled(false);
        }

    }

    //Returns the number of the last page
    private int getLastPage()
    {
        return (int) (Math.ceil((float) nOfResults / PAGESIZE));
    }

    //Returns the number of the current page
    private int getCurrentPage()
    {
        return (int) (Math.ceil((float) (offset + 1) / PAGESIZE));
    }


    // Buttons functions
    public void first(View v)
    {
        if (!loading)
        {
            offset = 0;
            (new LoadNextPage()).execute();
        }
    }

    public void next(View v)
    {
        if (!loading)
        {
            offset = getCurrentPage() * PAGESIZE;
            (new LoadNextPage()).execute();
        }
    }

    public void previous(View v)
    {
        if (!loading)
        {
            offset = (getCurrentPage() - 2) * PAGESIZE;
            (new LoadNextPage()).execute();
        }
    }

    public void last(View v)
    {
        if (!loading)
        {
            offset = (getLastPage() - 1) * PAGESIZE;
            (new LoadNextPage()).execute();
        }
    }

    //Function to transform a JSONArray into an ArrayList of Products
    public ArrayList<Product> productsFromJson(JSONArray jArr)
    {//feeds a product list by an JSON Array
        ArrayList<Product> products = new ArrayList<>();
        JSONObject jObj = new JSONObject();

        int mSku = 0;
        String mName = new String();
        Double mSalePrice = 0.0;
        String mImage = new String();
        String mLargeImage = new String();
        String mDescript = new String();

        for (int i = 0; i < jArr.length(); i++)
        {
            try {//here we try to get every value that we can
                jObj = jArr.getJSONObject(i);
                mSku = jObj.getInt("sku");
                mName = jObj.getString("name");
                mSalePrice = jObj.getDouble("salePrice");
                mImage = jObj.getString("image");
                mLargeImage = jObj.getString("largeImage");
                mDescript = jObj.getString("longDescription");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //then we add the product here
            //This is done this way in case that we have product without some attribute
            products.add(new Product(mSku, mName, mSalePrice, mImage, mLargeImage, mDescript));
        }

        return products;
    }
}
