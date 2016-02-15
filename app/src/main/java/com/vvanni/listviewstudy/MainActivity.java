package com.vvanni.listviewstudy;

import android.app.ListActivity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private BestBuySearch bbSearch;

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
        products = new ArrayList<>(); //equivalent to Datasource.getInstance();
        //datasource = Datasource.getInstance();

        setListAdapter(new ProductListAdapter(this, new ArrayList<Product>()));

        (new LoadNextPage()).execute();
    /*{
        final Context mContext = this;
        lvProducts = (ListView) findViewById(R.id.list_product);

        //I should change the txtHeader further appearance
        //final TextView txtHeader = (TextView) findViewById(R.id.header);

        RequestQueue queue = Volley.newRequestQueue(this);

        // populate data
        bbSearch = new BestBuySearch();
        String url = bbSearch.getSearchURL();

        Log.d("STATE", "url == " + url);

        Log.d("STATE", "starting to populate data");

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {//return response
                        Log.d("Response", response.toString());
                        try {
                            JSONArray jArr;

                            jArr = response.getJSONArray("products");
                            products = productsFromJson(jArr);
//                            txtHeader.append("\nDisplaying "
//                                    + response.getInt("from") + " - "
//                                    + response.getInt("to") + " of "
//                                    + response.getInt("total"));
                            lvProducts.setAdapter(new ProductListAdapterSimple(mContext, products));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("Error response", error.toString());
                    }
                }
        );
        Log.d("STATE", "ending of populating data");
        queue.add(getRequest);*/
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

            try{
                jObj = bbSearch.getApiSearch(search, offset, PAGESIZE);
                jArr = jObj.getJSONArray("products");
                products = productsFromJson(jArr);
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            }

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

        protected void updateDisplayingTextView()
        {
            String text = getString(R.string.displayshort);
            text = String.format(text, Math.min(products.size(), offset + 1),
                    Math.min(offset + PAGESIZE, products.size()), products.size());
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
            return (int) (Math.ceil((float) products.size() / PAGESIZE));
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
                        jObj.getString("name"),
                        jObj.getDouble("salePrice"),
                        jObj.getString("image"),
                        jObj.getString("largeImage")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//(String _name, double _price, String _img_url_small, String _img_url_big)

        return products;
    }
}
