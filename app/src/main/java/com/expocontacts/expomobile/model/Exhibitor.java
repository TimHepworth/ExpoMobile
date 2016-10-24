package com.expocontacts.expomobile.model;

public class Exhibitor {

    private Integer mExhibitorId;
    private Integer mExhibitorFairId;
    private String mExhibitorName;
    private String mExhibitorLogo;
    private String mStand;
    private String mWebsite;
    private String mProfile;
    private boolean mFavourite;

    public Exhibitor() {

    }

    public Integer getExhibitorId() {
        return mExhibitorId;
    }

    public void setExhibitorId(Integer exhibitorId) {
        mExhibitorId = exhibitorId;
    }

    public Integer getExhibitorFairId() {
        return mExhibitorFairId;
    }

    public void setExhibitorFairId(Integer exhibitorFairId) {
        mExhibitorFairId = exhibitorFairId;
    }

    public String getExhibitorName() {
        return mExhibitorName;
    }

    public void setExhibitorName(String exhibitorName) {
        mExhibitorName = exhibitorName;
    }

    public String getExhibitorLogo() {
        return mExhibitorLogo;
    }

    public void setExhibitorLogo(String exhibitorLogo) {
        mExhibitorLogo = exhibitorLogo;
    }

    public String getStand() {
        return mStand;
    }

    public void setStand(String stand) {
        mStand = stand;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public String getProfile() {
        return mProfile;
    }

    public void setProfile(String profile) {
        mProfile = profile;
    }

    public boolean isFavourite() {
        return mFavourite;
    }

    public void setFavourite(boolean favourite) {
        mFavourite = favourite;
    }
}
