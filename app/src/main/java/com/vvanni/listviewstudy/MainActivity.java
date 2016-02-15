package com.vvanni.listviewstudy;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private BestBuySearch bbSearch;

    private int nOfResults;
    //------from abstract------
    protected static final int PAGESIZE = 10;

    protected TextView textViewHeader;

    protected boolean loading = false;

    private String search = "smartphone";
    //-------from abstract-------

    private int offset = 0;

    private ProgressBar progressBar;

    private Button first;

    private Button last;

    private Button prev;

    private Button next;

    ArrayList<Product> products;
    ListView lvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list); //that would be the buttons.xml

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewHeader = (TextView) findViewById(R.id.header);
        first = (Button) findViewById(R.id.buttonfirst);
        prev = (Button) findViewById(R.id.buttonprev);
        next = (Button) findViewById(R.id.buttonnext);
        last = (Button) findViewById(R.id.buttonlast);
        products = new ArrayList<>();
        bbSearch = new BestBuySearch();

        setListAdapter(new ProductListAdapter(this, new ArrayList<Product>()));
        getListView().setOnItemClickListener(this);

        (new LoadNextPage()).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        // TODO Auto-generated method stub
        String item = adapter.getItemAtPosition(position).toString();
        //id is the row that I'm working on, also, the position of product in products.get(id)
        //Toast.makeText(MainActivity.this, "CLICK: " + item + " " + id, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, FullItemActivity.class);
        intent.putExtra("product_sku", products.get((int)id).sku);
        intent.putExtra("product_name", products.get((int)id).name);
        intent.putExtra("product_img", products.get((int)id).img_url_big);
        intent.putExtra("product_price", products.get((int)id).price);
        intent.putExtra("product_descript", products.get((int)id).descript);
        startActivity(intent);

    }

    private class LoadNextPage extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            loading = true;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0)
        {
            JSONObject jObj = new JSONObject();
            JSONArray jArr = new JSONArray();
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                Log.e("PagingButtons", e.getMessage());
            }

            bbSearch.setSearch(search);
            bbSearch.setPage(getCurrentPage());
            bbSearch.setPageSize(PAGESIZE);

            try{
                jObj = bbSearch.getApiSearch();
                nOfResults = jObj.getInt("total");
                //Thread.sleep(100);
                jArr = jObj.getJSONArray("products");
                products = productsFromJson(jArr);
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            } //catch (InterruptedException e) {
              //  Log.e("WAIT", e.getMessage());
            //}

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            ProductListAdapter productListAdapter = ((ProductListAdapter) getListAdapter());
            productListAdapter.clear();

            for (Product product : products)
            {
                productListAdapter.add(product);
            }
            productListAdapter.notifyDataSetChanged();

            updateDisplayingTextView();

            loading = false;
        }
    }

    protected void updateDisplayingTextView()
    {
        String text = getString(R.string.displayshort);
        text = String.format(text, Math.min(nOfResults, offset + 1),
                Math.min(offset + PAGESIZE, nOfResults), nOfResults);
        textViewHeader.setText(text);
        updateButtons();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void updateButtons()
    {
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

    private int getLastPage()
    {
        return (int) (Math.ceil((float) nOfResults / PAGESIZE));
    }

    private int getCurrentPage()
    {
        return (int) (Math.ceil((float) (offset + 1) / PAGESIZE));
    }

    /******************** BUTTONS *************************/

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

    public ArrayList<Product> productsFromJson(JSONArray jArr)
    {//feeds a product list by an JSON Array
        ArrayList<Product> products = new ArrayList<>();
        JSONObject jObj = new JSONObject();

        for (int i = 0; i < jArr.length(); i++)
        {
            try {
                jObj = jArr.getJSONObject(i);
                products.add(new Product(
                        jObj.getInt("sku"),
                        jObj.getString("name"),
                        jObj.getDouble("salePrice"),
                        jObj.getString("image"),
                        jObj.getString("largeImage"),
                        jObj.getString("description")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//(String _name, double _price, String _img_url_small, String _img_url_big)

        return products;
    }
}

