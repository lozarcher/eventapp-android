package com.loz.iyaf.feed;

import java.util.Date;

/**
 * Created by loz on 27/03/16.
 */
public class InfoList {
    private Date date;
    private Iterable<InfoData> data;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<InfoData> getData() {
        return data;
    }

    public void setData(Iterable<InfoData> data) {
        this.data = data;
    }
}
