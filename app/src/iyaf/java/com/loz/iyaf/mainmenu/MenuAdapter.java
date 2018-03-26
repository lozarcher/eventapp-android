package com.loz.iyaf.mainmenu;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.loz.iyaf.imagehelpers.Utils;
import com.loz.R;

public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater=null;

    public MenuAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater)mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        ImageView img;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.menulist, null);

        holder.img = (ImageView) rowView.findViewById(R.id.menuImage);

        holder.img.setPadding(0, 0, 0, 0);
        Utils.loadImage("drawable://" + mThumbIds[position], holder.img, null);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, mContext.getResources().getDisplayMetrics()));

        switch (position) {
            case 0 :
                holder.img.setRotation(10);
                lp.setMargins(0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, mContext.getResources().getDisplayMetrics())), 0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics())));
                break;
            default:
                lp.setMargins(0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, mContext.getResources().getDisplayMetrics())), 0, 0);
                break;
        }

        holder.img.setLayoutParams(lp);

        return rowView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.menu_events, R.drawable.menu_performers,
            R.drawable.menu_news, R.drawable.menu_gallery,
            R.drawable.menu_twitter, R.drawable.menu_info
    };

}
