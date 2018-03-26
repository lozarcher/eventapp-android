package com.loz.iyaf.news;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loz.iyaf.feed.NewsData;
import com.loz.iyaf.imagehelpers.Utils;
import com.loz.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewsListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<NewsData> data;
    private static LayoutInflater inflater=null;

    public NewsListAdapter(Activity a, ArrayList<NewsData> d) {
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
            vi = inflater.inflate(R.layout.list_row_news, null);

        TextView newsText = (TextView)vi.findViewById(R.id.newsText); // title
        ImageView newsImage=(ImageView)vi.findViewById(R.id.newsImage); // thumb image
        TextView dateText = (TextView)vi.findViewById(R.id.dateText);
        TextView newsLink = (TextView)vi.findViewById(R.id.linkText);

        NewsData newsItem = data.get(position);

        // Setting all values in listview
        String message = newsItem.getMessage();
        if (message != null) {
            newsText.setVisibility(View.VISIBLE);
            newsText.setText(newsItem.getMessage().toString());
        } else {
            newsText.setVisibility(View.GONE);
        }

        dateText.setText(dateDiff(newsItem.getCreatedDate()));

        String imageUrl = newsItem.getPictureUrl();
        if (imageUrl != null) {
            Utils.loadImage(newsItem.getPictureUrl(), newsImage, null);
            newsImage.setVisibility(View.VISIBLE);
        } else {
            newsImage.setVisibility(View.GONE);
        }

        String link = newsItem.getName();
        final String linkUrl = newsItem.getLink();

        if (linkUrl != null) {
            newsLink.setVisibility(View.VISIBLE);
            if (link == null) {
                link = linkUrl;
            }

            newsLink.setText(
                    Html.fromHtml(
                            "<a href=\"" + linkUrl + "\">" + link + "</a> "));
            newsLink.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            newsLink.setVisibility(View.GONE);
        }

        return vi;
    }

    private String dateDiff (Date date) {
        Date todayDate = Calendar.getInstance().getTime();
        long ti = (todayDate.getTime()-date.getTime())/1000;
        if(ti < 1) {
            return "Just now";
        } else 	if (ti < 60) {
            return "Just now";
        } else if (ti < 3600) {
            int diff = Math.round(ti / 60);
            if (diff == 1) {
                return String.format("%d minute ago", diff);
            } else {
                return String.format("%d minutes ago", diff);
            }
        } else if (ti < 86400) {
            int diff = Math.round(ti / 60 / 60);
            if (diff == 1) {
                return String.format("%d hour ago", diff);
            } else {
                return String.format("%d hours ago", diff);
            }
        } else if (ti < 2629743) {
            int diff = Math.round(ti / 60 / 60 / 24);
            if (diff == 1) {
                return String.format("%d day ago", diff);
            } else {
                return String.format("%d days ago", diff);
            }
        } else {
            int diff = Math.round(ti / 60 / 60 / 24 / 30);
            if (diff == 1) {
                return String.format("%d month ago", diff);
            } else {
                return String.format("%d months ago", diff);
            }
        }
    }
}
