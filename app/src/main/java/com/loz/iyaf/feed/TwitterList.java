package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class TwitterList implements Serializable {
    private Date date;
    private Iterable<TwitterData> data;
    private String next;

    public TwitterList() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<TwitterData> getData() {
        return data;
    }

    public void setData(Iterable<TwitterData> data) {
        this.data = data;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
