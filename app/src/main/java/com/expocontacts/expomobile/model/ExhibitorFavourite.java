package com.expocontacts.expomobile.model;

public class ExhibitorFavourite {

    private Integer mExhibitorFairId;
    private Integer mUserFairId;
    private Integer mRequiresSync;

    public ExhibitorFavourite() {

    }

    public Integer getExhibitorFairId() {
        return mExhibitorFairId;
    }

    public void setExhibitorFairId(Integer exhibitorFairId) {
        mExhibitorFairId = exhibitorFairId;
    }

    public Integer getUserFairId() {
        return mUserFairId;
    }

    public void setUserFairId(Integer userFairId) {
        mUserFairId = userFairId;
    }

}
