package com.vvanni.listviewstudy;

import android.net.ConnectivityManager;

import java.util.List;

/**
 * Created by Renata on 14/02/2016.
 */
public class Product {

    public int sku;
    public String name;
    public double price;
    public String img_url_small;
    public String img_url_big;
    public String descript;

    public Product (int _sku, String _name, double _price, String _img_url_small,
                    String _img_url_big, String _descript)
    {
        this.sku = _sku;
        this.name = _name;
        this.price = _price;
        this.img_url_small = _img_url_small;
        this.img_url_big = _img_url_big;
        this.descript = _descript;
    }

}


