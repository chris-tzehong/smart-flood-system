package com.example.smartfloodsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Threads implements Serializable {

    private String mThreadTitle;
    private String mThreadContent;
    private Date mThreadDate;
    private String mThreadImageUri;
    private ArrayList<HashMap<String, String>> mThreadComment;
    private String mPostUserName;
    private String mThreadLocation;
    private String mThreadId;

    public Threads() {

    }


    public Threads(String mThreadTitle, String mThreadContent, Date mThreadDate, String mThreadImageUri, ArrayList<HashMap<String, String>> mThreadComment, String mPostUserName, String mThreadLocation, String mThreadId) {
        this.mThreadTitle = mThreadTitle;
        this.mThreadContent = mThreadContent;
        this.mThreadDate = mThreadDate;
        this.mThreadImageUri = mThreadImageUri;
        this.mThreadComment = mThreadComment;
        this.mPostUserName = mPostUserName;
        this.mThreadLocation = mThreadLocation;
        this.mThreadId = mThreadId;
    }

    public String getmThreadTitle() {
        return mThreadTitle;
    }

    public void setmThreadTitle(String mThreadTitle) {
        this.mThreadTitle = mThreadTitle;
    }

    public String getmThreadContent() {
        return mThreadContent;
    }

    public void setmThreadContent(String mThreadContent) {
        this.mThreadContent = mThreadContent;
    }

    public Date getmThreadDate() {
        return mThreadDate;
    }

    public void setmThreadDate(Date mThreadDate) {
        this.mThreadDate = mThreadDate;
    }

    public String getmThreadImageUri() {
        return mThreadImageUri;
    }

    public void setmThreadImageUri(String mThreadImageUri) {
        this.mThreadImageUri = mThreadImageUri;
    }

    public ArrayList<HashMap<String, String>> getmThreadComment() {
        return mThreadComment;
    }

    public String getmPostUserName() {
        return mPostUserName;
    }

    public void setmPostUserName(String mPostUserName) {
        this.mPostUserName = mPostUserName;
    }

    public void setmThreadComment(ArrayList<HashMap<String, String>> mThreadComment) {
        this.mThreadComment = mThreadComment;
    }

    public String getmThreadLocation() {
        return mThreadLocation;
    }

    public void setmThreadLocation(String mThreadLocation) {
        this.mThreadLocation = mThreadLocation;
    }

    public String getmThreadId() {
        return mThreadId;
    }

    public void setmThreadId(String mThreadId) {
        this.mThreadId = mThreadId;
    }
}
