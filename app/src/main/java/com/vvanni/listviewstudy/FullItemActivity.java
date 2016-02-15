package com.vvanni.listviewstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Renata on 15/02/2016.
 */
public class FullItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));
/*
        intent.putExtra("product_sku", products.get((int)id).sku);
        intent.putExtra("product_name", products.get((int)id).name);
        intent.putExtra("product_img", products.get((int)id).img_url_big);
        intent.putExtra("product_price", products.get((int)id).price);*/

        Intent intent = getIntent();
        String name = intent.getStringExtra("product_name");
        String imgUrl = intent.getStringExtra("product_img");
        Double price = intent.getDoubleExtra("product_price", 0.00);
        String descript = intent.getStringExtra("product_descript");

        if(descript == "null")
            descript = "No description";

        TextView tvName = (TextView) findViewById(R.id.pop_name);
        TextView tvPrice = (TextView) findViewById(R.id.pop_price);
        TextView tvDescript = (TextView) findViewById(R.id.pop_description);
        ImageView ivImg = (ImageView) findViewById(R.id.pop_image);

        tvName.setText(name);
        tvPrice.setText("$");
        tvPrice.append(String.valueOf(price));
        tvDescript.setText(descript);


        Picasso.with(this)
                .load(imgUrl)
                .into(ivImg);
    }
}
