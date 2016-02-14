package com.vvanni.listviewstudy;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
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

public class ListActivity extends Activity {

    BestBuySearch bbSearch;

    ArrayList<Product> products;
    ListView lvProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final Context mContext = this;
        lvProducts = (ListView) findViewById(R.id.lists_product);

        final TextView tvSearchTitle = (TextView) findViewById(R.id.search_title);

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
                            tvSearchTitle.append("\n"
                                    + response.getInt("from") + " to "
                                    + response.getInt("to") + "/"
                                    + response.getInt("total"));
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
        queue.add(getRequest);
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
