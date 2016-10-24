package com.expocontacts.expomobile.model;

public class EventFavourite {

    private Integer mEventId;
    private Integer mUserFairId;
    private Integer mRequiresSync;

    public EventFavourite() {

    }

    public Integer getEventId() {
        return mEventId;
    }

    public void setEventId(Integer exhibitorFairId) {
        mEventId = exhibitorFairId;
    }

    public Integer getUserFairId() {
        return mUserFairId;
    }

    public void setUserFairId(Integer userFairId) {
        mUserFairId = userFairId;
    }

}
