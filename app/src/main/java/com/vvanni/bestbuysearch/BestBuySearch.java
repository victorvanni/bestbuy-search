package com.vvanni.bestbuysearch;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.*;
import java.util.ArrayList;

public class BestBuySearch {

    //attributes necessary to buld the search url
    public ArrayList<Product> products;
    private String rawJsonURL;
    private String search;
    private String apiKey;
    private String show;
    private String sort;
    private String descript;
    private int pageSize;
    private int page;
    private JSONObject mResponse;

    //basic constructor feeding the variables with basic informations
    public BestBuySearch()
    {
        rawJsonURL = "https://api.bestbuy.com/v1/products(customerReviewCount>100"
                + "&search=%search)?format=json&show=%show"
                + "&sort=%sort&pageSize=%pageSize&page=%pageNum&apiKey=%apiKey";
        search = "smartphone"; //just a test
        apiKey = "vhhepc7hsp89rbb9rbubxs6h";
        show = "sku,name,salePrice,image,largeImage,longDescription,shortDescription";
        sort = "customerReviewAverage.dsc";
        pageSize = 10;
        page = 1;
    }

    //getters and setters
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

    //returns the search URL builded with some string formats to avoid multiple spaces
    public String getSearchURL()
    {//returns the JSON string from the URL requested
        //Some string treatments to allow search with multiple spaces before, after and between
        //with multiple words
        String auxSearch = getSearch();
        auxSearch = auxSearch.trim();
        auxSearch = auxSearch.replaceAll(" ", "&search=");
        auxSearch = auxSearch.replaceAll("\\s+", " ");
        return rawJsonURL.replace("%search", auxSearch)
                .replace("%show", getShow())
                .replace("%sort", getSort())
                .replace("%pageSize", String.valueOf(getPageSize()))
                .replace("%pageNum", String.valueOf(getPage()))
                .replace("%apiKey", getApiKey());
    }

    //HTTP GET by urlConnection
    public JSONObject getApiSearch()
    {
        String link = getSearchURL();
        URL url;
        HttpURLConnection urlConnection = null;
        JSONObject response = new JSONObject();

        try {
            //try to connect
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            int responseCode = urlConnection.getResponseCode();

            //if the connection was succesfull get the whole stream into a response string
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

    //reads the results of HTTP GET
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

