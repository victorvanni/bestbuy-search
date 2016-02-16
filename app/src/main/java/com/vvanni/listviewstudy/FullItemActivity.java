package com.vvanni.listviewstudy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

//PopUp activity to show more details about a single product
public class FullItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_window);

        //Setting up the display size, to look like a popup
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //here we set the popup to be 80% of the parent width and 70% of its height
        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        //Populating the product details
        Intent intent = getIntent();
        String name = intent.getStringExtra("product_name");
        String imgUrl = intent.getStringExtra("product_img");
        Double price = intent.getDoubleExtra("product_price", 0.00);
        String descript = intent.getStringExtra("product_descript");

        //Instanciating the views
        TextView tvName = (TextView) findViewById(R.id.pop_name);
        TextView tvPrice = (TextView) findViewById(R.id.pop_price);
        TextView tvDescript = (TextView) findViewById(R.id.pop_description);
        ImageView ivImg = (ImageView) findViewById(R.id.pop_image);

        //Setting the product details into its views
        tvName.setText(name);
        tvPrice.setText("$");
        tvPrice.append(String.valueOf(price));
        tvDescript.setText(descript);
        tvDescript.setMovementMethod(new ScrollingMovementMethod());

        //downloading the big image
        //For some reason the image is smaller than small image, thats why we use small image here
        Picasso.with(this)
                .load(imgUrl)
                .into(ivImg);
    }
}
