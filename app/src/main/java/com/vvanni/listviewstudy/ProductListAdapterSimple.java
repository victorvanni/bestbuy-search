package com.vvanni.listviewstudy;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Renata on 14/02/2016.
 */
public class ProductListAdapterSimple extends ArrayAdapter<Product> {

    ArrayList<Product> mylist;
    private Context context;

    public ProductListAdapterSimple(Context _context, ArrayList<Product> _mylist) {
        super(_context, R.layout.list_item, _mylist);

        this.context = _context;
        this.mylist = _mylist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
        convertView = vi.inflate(R.layout.list_item, parent, false);

        // Product object
        Product product = getItem(position);

        // TextView Product name
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(product.name);

        // TextView Product Price
        TextView txtPrice = (TextView) convertView.findViewById(R.id.salePrice);
        txtPrice.setText(" $");
        txtPrice.append(String.valueOf(product.price));
        txtPrice.append(" ");

        // Image
        ImageView img = (ImageView) convertView.findViewById(R.id.image);

        // Download Image via Picasso
        Picasso.with(this.context)
                .load(product.img_url_small)
                .into(img);
        //ImageDownloader imageDownloader = new ImageDownloader();
        //imageDownloader.download(product.img_url_small, img);

        return convertView;

    }
}
