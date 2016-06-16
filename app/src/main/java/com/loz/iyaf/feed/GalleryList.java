package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class GalleryList implements Serializable {
    private Date date;
    private Iterable<GalleryData> data;

    public GalleryList() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<GalleryData> getData() {
        return data;
    }

    public void setData(Iterable<GalleryData> data) {
        this.data = data;
    }
}
