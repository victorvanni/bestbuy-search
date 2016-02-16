package com.vvanni.listviewstudy;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//Adapter with ViewHolder pattern
public class ProductListAdapter extends ArrayAdapter<Product> {

    ArrayList<Product> mylist;
    Context mContext;

    public ProductListAdapter(Context _context, ArrayList<Product> _mylist) {
        super(_context, R.layout.list_item, _mylist);

        this.mContext = _context;
        this.mylist = _mylist;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Product row = getItem(position);

        ProductViewHolder holder;

        if (convertView == null)
        { //In case the View is null, we have to declare it for the first time
          //saving the values into the holder
            convertView = new RelativeLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.list_item, parent, false);

            holder = new ProductViewHolder();
            holder.img  = (ImageView)convertView.findViewById(R.id.image);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.price = (TextView)convertView.findViewById(R.id.salePrice);

            convertView.setTag(holder);
        } else {
            //If the view is not null, ie it was already created
            //it doesn't need to instanciate everything again
            holder = (ProductViewHolder) convertView.getTag();
        }

        //(re)populate holder
        holder.name.setText(row.name);
        holder.price.setText("$");
        holder.price.append(String.valueOf(row.price));
        // Download Image via Picasso
        Picasso.with(mContext)
                .load(row.img_url_small)
                .into(holder.img);

        //
        return convertView;
    }

    static class ProductViewHolder {
        public ImageView img;
        public TextView name;
        public TextView price;
        public Context mContext;
    }
}
