package com.loz.iyaf.info;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loz.iyaf.feed.InfoData;
import com.loz.iyaf.imagehelpers.Utils;
import com.loz.R;

import java.util.ArrayList;

public class InfoListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<InfoData> data;
    private static LayoutInflater inflater=null;

    public InfoListAdapter (Activity a, ArrayList<InfoData> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row_trader, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        InfoData trader = data.get(position);

        // Setting all values in listview
        title.setText(trader.getTitle());
        Utils.loadImage(trader.getThumb(), thumb_image, null, 0);
        return vi;
    }
}
