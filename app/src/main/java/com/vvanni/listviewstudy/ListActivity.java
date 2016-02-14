package com.vvanni.listviewstudy;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {

    BestBuySearch bbSearch;

    ArrayList<Product> products;
    ListView lvProducts;

    ////List of array strings which will serve as list items
    //ArrayList<String> listItems = new ArrayList<String>();

    ////Defining a string adapter which will handle the data of the ListView
    //ArrayAdapter<String> adapter;

    ////Recording how many times the button has been clicked
    //int clickCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RequestQueue queue = Volley.newRequestQueue(this);

        // populate data
        products = new ArrayList<>();
        products.add(new Product(
                "Apple - iPhone 6 Plus 128GB - Space Gray (AT&T)",
                899.98,
                "http://images.bestbuy.com/BestBuy_US/images/products/7640/7640104_sa.jpg",
                "http://images.bestbuy.com/BestBuy_US/images/products/7640/7640104_sb.jpg"));
        products.add(new Product(
                "Apple - iPhone 6 Plus 64GB - Gold (AT&T)",
                799.98,
                "http://images.bestbuy.com/BestBuy_US/images/products/7640/7640168_sa.jpg",
                "http://images.bestbuy.com/BestBuy_US/images/products/7640/7640168_sb.jpg"));
        products.add(new Product(
                "Apple - iPhone 6 Plus 64GB - Silver (AT&T)",
                799.98,
                "http://images.bestbuy.com/BestBuy_US/images/products/7640/7640159_sa.jpg",
                "http://images.bestbuy.com/BestBuy_US/images/products/7640/7640159_sb.jpg"));
        products.add(new Product(
                "Nest - Protect 2nd Generation (Battery) Smart Smoke/Carbon Monoxide Alarm - White",
                99.99,
                "http://img.bbystatic.com/BestBuy_US/images/products/8077/8077101_rc.jpg",
                "http://img.bbystatic.com/BestBuy_US/images/products/8077/8077101_rb.jpg"));

        lvProducts = (ListView) findViewById(R.id.lists_product);
        lvProducts.setAdapter(new ProductListAdapterSimple(this, products));
    }
}
