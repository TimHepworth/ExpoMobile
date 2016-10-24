package com.expocontacts.expomobile.model;

public class UserPost {

    private Integer mPostId;
    private String mForename;
    private String mSurname;
    private String mPostText;
    private Integer mPostDate;
    private String mImageURL;

    public Integer getPostId() {
        return mPostId;
    }

    public void setPostId(Integer postId) {
        mPostId = postId;
    }

    public String getForename() {
        return mForename;
    }

    public void setForename(String forename) {
        mForename = forename;
    }

    public String getSurname() {
        return mSurname;
    }

    public void setSurname(String surname) {
        mSurname = surname;
    }

    public String getPostText() {
        return mPostText;
    }

    public void setPostText(String postText) {
        mPostText = postText;
    }

    public Integer getPostDate() {
        return mPostDate;
    }

    public void setPostDate(Integer postDate) {
        mPostDate = postDate;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }
}
