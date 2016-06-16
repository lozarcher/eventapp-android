package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class NewsList implements Serializable {
    private Date date;
    private Iterable<NewsData> data;
    private String next;

    public NewsList() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<NewsData> getData() {
        return data;
    }

    public void setData(Iterable<NewsData> data) {
        this.data = data;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
