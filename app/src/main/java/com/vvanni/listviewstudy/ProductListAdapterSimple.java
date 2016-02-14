package com.vvanni.listviewstudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
import java.util.List;

/**
 * Created by Renata on 14/02/2016.
 */
public class ProductListAdapterSimple extends ArrayAdapter<Product> {

    List<Product> mylist;

    public ProductListAdapterSimple(Context _context, List<Product> _mylist) {
        super(_context, R.layout.list_item, _mylist);

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

        // TextView
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setText(product.name);

        // Image
        ImageView img = (ImageView) convertView.findViewById(R.id.image);

        // Download Image (with cache)
        //ImageDownloader imageDownloader = new ImageDownloader();
        //imageDownloader.download(product.img_url_small, img);

        return convertView;

    }
}
