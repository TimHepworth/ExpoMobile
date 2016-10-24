package com.expocontacts.expomobile.model;

public class NewsArticle {

    private Integer mNewsId;
    private String mHeadline;
    private long mPublicationDate;
    private String mArticleText;
    private String mPictureURL;

    public NewsArticle() {

    }

    public Integer getNewsId() {
        return mNewsId;
    }

    public void setNewsId(Integer newsId) {
        mNewsId = newsId;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String headline) {
        mHeadline = headline;
    }

    public long getPublicationDate() {
        return mPublicationDate;
    }

    public void setPublicationDate(long publicationDate) {
        mPublicationDate = publicationDate;
    }

    public String getArticleText() {
        return mArticleText;
    }

    public void setArticleText(String articleText) {
        mArticleText = articleText;
    }

    public String getPictureURL() {
        return mPictureURL;
    }

    public void setPictureURL(String pictureURL) {
        mPictureURL = pictureURL;
    }
}
