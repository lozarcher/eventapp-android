package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class EventList implements Serializable {
    private Date date;
    private Iterable<EventData> data;

    public EventList() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<EventData> getData() {
        return data;
    }

    public void setData(Iterable<EventData> data) {
        this.data = data;
    }
}
