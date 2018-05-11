package com.loz.iyaf.feed;

import java.io.Serializable;
import java.util.Date;

public class EventData implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String coverUrl;
    private Integer coverOffsetX;
    private Integer coverOffsetY;
    private String profileUrl;
    private Date startTime;
    private Date endTime;
    private String location;
    private VenueData venue;
    private String ticketUrl;
    private boolean isFavourite;
    private Long eventId;
    private Iterable<Long> categories;

    private int notificaitonId;

    public EventData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        if (location == null) {
            return "Kingston";
        } else {
            return location;
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public VenueData getVenue() {
        return venue;
    }

    public void setVenue(VenueData venue) {
        this.venue = venue;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public Integer getCoverOffsetY() {
        return coverOffsetY;
    }

    public void setCoverOffsetY(Integer coverOffsetY) {
        this.coverOffsetY = coverOffsetY;
    }

    public Integer getCoverOffsetX() {
        return coverOffsetX;
    }

    public void setCoverOffsetX(Integer coverOffsetX) {
        this.coverOffsetX = coverOffsetX;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getNotificaitonId() {
        return (int) (this.getStartTime().getTime() / 1000 / 60);
    }

    public void setNotificaitonId(int notificaitonId) {
        this.notificaitonId = notificaitonId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Iterable<Long> getCategories() {
        return categories;
    }

    public void setCategories(Iterable<Long> categories) {
        this.categories = categories;
    }
}
