package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class TraderList implements Serializable {
    private Date date;
    private Iterable<TraderData> data;

    public TraderList() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<TraderData> getData() {
        return data;
    }

    public void setData(Iterable<TraderData> data) {
        this.data = data;
    }
}
