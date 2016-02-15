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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by Renata on 14/02/2016.
 */
public class BestBuySearch {

    public ArrayList<Product> products;

    //private static Activity mActivity;
    //private static Context mCtx;
    private RequestQueue mQueue;
    private String rawJsonURL;
    private String search;
    private String apiKey;
    private String show;
    private String sort;
    private String descript;
    private int pageSize;
    private int page;
    private JSONObject mResponse;

    public BestBuySearch()
    {
        rawJsonURL = "https://api.bestbuy.com/v1/products(customerReviewCount>100"
                + "&search=%search)?format=json&show=%show"
                + "&sort=%sort&pageSize=%pageSize&page=%pageNum&apiKey=%apiKey";
        search = "smartphone"; //just a test
        apiKey = "vhhepc7hsp89rbb9rbubxs6h";
        show = "sku,name,salePrice,image,largeImage,description";
        sort = "customerReviewAverage.dsc";
        pageSize = 10;
        page = 1;
    }

    public void setSearch(String _search){search = _search;}

    public String getSearch(){return search;}

    public void setDescript(String _descript){search = _descript;}

    public String getDescript(){return descript;}
    
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
                .replace("%pageNum", String.valueOf(getPage()))
                .replace("%apiKey", getApiKey());
    }

    public JSONObject getApiSearch()
    {
        String link = getSearchURL();
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject response = new JSONObject();

        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", responseString);
                response = new JSONObject(responseString);
            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        return response;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}

