package com.loz.iyaf.twitter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loz.iyaf.imagehelpers.Utils;
import com.loz.R;
import com.loz.iyaf.feed.TwitterData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TwitterListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<TwitterData> data;
    private static LayoutInflater inflater=null;

    public TwitterListAdapter(Activity a, ArrayList<TwitterData> d) {
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
            vi = inflater.inflate(R.layout.list_row_tweet, null);

        TextView tweetContent = (TextView)vi.findViewById(R.id.tweetContent); // title
        ImageView tweetAuthorThumbnail=(ImageView)vi.findViewById(R.id.tweetAuthorThumbnail); // thumb image
        TextView tweetAuthor = (TextView)vi.findViewById(R.id.tweetAuthor);
        TextView tweetSince = (TextView)vi.findViewById(R.id.tweetSince);

        TwitterData tweet = data.get(position);

        // Setting all values in listview
        tweetContent.setText(tweet.getText());
        tweetContent.setLinksClickable(true);
        tweetSince.setText(dateDiff(tweet.getCreatedDate()));

        tweetAuthor.setText(tweet.getName());
        Utils.loadImage(tweet.getProfilePic(), tweetAuthorThumbnail, null);


        return vi;
    }

    private String dateDiff (Date date) {
        Date todayDate = Calendar.getInstance().getTime();
        long ti = (todayDate.getTime()-date.getTime())/1000;
        if(ti < 1) {
            return "now";
        } else 	if (ti < 60) {
            return "now";
        } else if (ti < 3600) {
            int diff = Math.round(ti / 60);
            return String.format("%dm", diff);
        } else if (ti < 86400) {
            int diff = Math.round(ti / 60 / 60);
            return String.format("%dh", diff);
        } else if (ti < 2629743) {
            int diff = Math.round(ti / 60 / 60 / 24);
            return String.format("%dd", diff);
        } else {
            int diff = Math.round(ti / 60 / 60 / 24 / 30);
            return String.format("%dm", diff);
        }
    }
}
