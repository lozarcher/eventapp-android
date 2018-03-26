package com.loz.iyaf.mainmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loz.iyaf.R;
import com.loz.iyaf.imagehelpers.Utils;

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
        TextView tv;
        ImageView img;
        ImageView spoonImg;
        ImageView teaspoonImg;
        ImageView chopsticksImg;
        ImageView knifeImg;
        ImageView forkImg;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.menulist, null);

        holder.tv = (TextView) rowView.findViewById(R.id.textView);
        holder.img = (ImageView) rowView.findViewById(R.id.menuImage);
        holder.teaspoonImg = (ImageView) rowView.findViewById(R.id.teaspoonImage);
        holder.spoonImg = (ImageView) rowView.findViewById(R.id.spoonImage);
        holder.chopsticksImg = (ImageView) rowView.findViewById(R.id.chopsticksImage);
        holder.knifeImg = (ImageView) rowView.findViewById(R.id.knifeImage);
        holder.forkImg = (ImageView) rowView.findViewById(R.id.forkImage);

        holder.img.setPadding(0,0,0,0);
        Utils.loadImage("drawable://"+mThumbIds[position], holder.img, null);
        holder.tv.setText(labels[position]);

        holder.forkImg.setVisibility(View.INVISIBLE);
        holder.knifeImg.setVisibility(View.INVISIBLE);
        holder.teaspoonImg.setVisibility(View.INVISIBLE);
        holder.spoonImg.setVisibility(View.INVISIBLE);
        holder.chopsticksImg.setVisibility(View.INVISIBLE);

        switch (position) {
            case 1:
                Utils.loadImage("drawable://"+R.id.forkImage, holder.forkImg, null);
                holder.forkImg.setVisibility(View.VISIBLE);
                Utils.loadImage("drawable://" + R.id.knifeImage, holder.knifeImg, null);
                holder.knifeImg.setVisibility(View.VISIBLE);
                break;
            case 2:
                Utils.loadImage("drawable://" + R.id.teaspoonImage, holder.teaspoonImg, null);
                holder.teaspoonImg.setVisibility(View.VISIBLE);
                break;
            case 4:
                Utils.loadImage("drawable://" + R.id.spoonImage, holder.spoonImg, null);
                holder.spoonImg.setVisibility(View.VISIBLE);
                break;
            case 5:
                Utils.loadImage("drawable://" + R.id.chopsticksImage, holder.chopsticksImg, null);
                holder.chopsticksImg.setVisibility(View.VISIBLE);
                break;
        }

        return rowView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.magenta_plate, R.drawable.orange_plate,
            R.drawable.blue_plate, R.drawable.red_plate,
            R.drawable.green_plate, R.drawable.indigo_plate
    };

    // references to our image labels
    private String[] labels = {
            "Events", "Gallery",
            "News", "Traders",
            "Get\nInvolved", "Twitter"
    };

}
