package com.loz.iyaf.feed;

import java.io.Serializable;

public class TraderData implements Serializable {
    private long id;
    private String name;
    private String about;
    private String coverImg;
    private int coverOffsetX;
    private int coverOffsetY;
    private String link;
    private String profileImg;
    private String website;
    private String phone;
    private boolean kingstonPound;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getCoverOffsetX() {
        return coverOffsetX;
    }

    public void setCoverOffsetX(int coverOffsetX) {
        this.coverOffsetX = coverOffsetX;
    }

    public int getCoverOffsetY() {
        return coverOffsetY;
    }

    public void setCoverOffsetY(int coverOffsetY) {
        this.coverOffsetY = coverOffsetY;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isKingstonPound() {
        return kingstonPound;
    }

    public void setKingstonPound(boolean kingstonPound) {
        this.kingstonPound = kingstonPound;
    }
}
