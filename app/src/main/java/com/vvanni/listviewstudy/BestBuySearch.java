package com.vvanni.listviewstudy;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by Renata on 14/02/2016.
 */
public class BestBuySearch {

    public ArrayList<Product> products;
    public ListView lvProducts;

    private static Activity mActivity;
    private static Context mCtx;
    private RequestQueue mQueue;
    private String rawJsonURL;
    private String search;
    private String apiKey;
    private String show;
    private String sort;
    private int pageSize;
    private int page;
    private JSONObject mResponse;

    public BestBuySearch()
    {
        rawJsonURL = "https://api.bestbuy.com/v1/products(customerReviewCount>100"
                + "&search=%search)?format=json&show=%show"
                + "&sort=%sort&pageSize=%pageSize&page=1&apiKey=%apiKey";
        search = "smartphone"; //just a test
        apiKey = "vhhepc7hsp89rbb9rbubxs6h";
        show = "sku,name,salePrice,image,largeImage";
        sort = "customerReviewAverage.dsc";
        pageSize = 10;
        page = 1;
    }

    public void setSearch(String _search){search = _search;}

    public String getSearch(){return search;}

    public void setShow(String _show){show = _show;}

    public String getShow(){return show;}

    public void setSort(String _sort){sort = _sort;}

    public String getSort(){return sort;}

    public void setPageSize(int _pageSize){pageSize = _pageSize;}

    public int getPageSize(){return pageSize;}

    public void setPage(int _page){page = _page;}

    public int getPage(){return page;}

    public void setApiKey(String _apiKey){apiKey = _apiKey;}

    public String getApiKey(){return apiKey;}

    public String getSearchURL()
    {//returns the JSON string from the URL requested
        return rawJsonURL.replace("%search", getSearch())
                .replace("%show", getShow())
                .replace("%sort", getSort())
                .replace("%pageSize", String.valueOf(getPageSize()))
                .replace("%apiKey", getApiKey());
    }

    public RequestQueue getRequestQueue()
    {
        if (mQueue == null) {
            // if the queue isn't setted, it gets from the app context
            mQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mQueue;
    }
}

