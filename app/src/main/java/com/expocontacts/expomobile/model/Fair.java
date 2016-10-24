package com.expocontacts.expomobile.model;

/**
 * Created by Tim on 30/09/2016.
 */

public class Fair {

    private Integer mFairId;
    private String mFairName;
    private String mGeneralInfo;
    private long mUpdateRequired;
    private long mStartDate;
    private long mEndDate;
    private String mFairLogoURL;
    private String mFloorplanURL;
    private Integer mShowGeneralInfo;

    private Integer mShowExhibitorList;
    private Integer mShowSchedule;
    private Integer mShowNews;
    private Integer mShowSpeakers;
    private Integer mShowFloorplan;

    public Integer getFairId() {
        return mFairId;
    }

    public void setFairId(Integer fairId) {
        mFairId = fairId;
    }

    public String getFairName() {
        return mFairName;
    }

    public void setFairName(String fairName) {
        mFairName = fairName;
    }

    public String getGeneralInfo() {
        return mGeneralInfo;
    }

    public void setGeneralInfo(String generalInfo) {
        mGeneralInfo = generalInfo;
    }

    public long getUpdateRequired() {
        return mUpdateRequired;
    }

    public void setUpdateRequired(long updateRequired) {
        mUpdateRequired = updateRequired;
    }

    public long getStartDate() {
        return mStartDate;
    }

    public void setStartDate(long startDate) {
        mStartDate = startDate;
    }

    public long getEndDate() {
        return mEndDate;
    }

    public void setEndDate(long endDate) {
        mEndDate = endDate;
    }

    public String getFairLogoURL() {
        return mFairLogoURL;
    }

    public void setFairLogoURL(String fairLogoURL) {
        mFairLogoURL = fairLogoURL;
    }

    public String getFloorplanURL() {
        return mFloorplanURL;
    }

    public void setFloorplanURL(String floorplanURL) {
        mFloorplanURL = floorplanURL;
    }

    public Integer getShowGeneralInfo() {
        return mShowGeneralInfo;
    }

    public void setShowGeneralInfo(Integer showGeneralInfo) {
        mShowGeneralInfo = showGeneralInfo;
    }

    public Integer getShowExhibitorList() {
        return mShowExhibitorList;
    }

    public void setShowExhibitorList(Integer showExhibitorList) {
        mShowExhibitorList = showExhibitorList;
    }

    public Integer getShowSchedule() {
        return mShowSchedule;
    }

    public void setShowSchedule(Integer showSchedule) {
        mShowSchedule = showSchedule;
    }

    public Integer getShowNews() {
        return mShowNews;
    }

    public void setShowNews(Integer showNews) {
        mShowNews = showNews;
    }

    public Integer getShowSpeakers() {
        return mShowSpeakers;
    }

    public void setShowSpeakers(Integer showSpeakers) {
        mShowSpeakers = showSpeakers;
    }

    public Integer getShowFloorplan() {
        return mShowFloorplan;
    }

    public void setShowFloorplan(Integer showFloorplan) {
        mShowFloorplan = showFloorplan;
    }

}
