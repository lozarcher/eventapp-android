package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class EventList implements Serializable {
    private Date date;
    private Iterable<EventData> events;
    private Iterable<CategoryData> categories;
    public EventList() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Iterable<EventData> getEvents() {
        return events;
    }

    public void setEvents(Iterable<EventData> events) {
        this.events = events;
    }

    public Iterable<CategoryData> getCategories() {
        return categories;
    }

    public void setCategories(Iterable<CategoryData> categories) {
        this.categories = categories;
    }
}
