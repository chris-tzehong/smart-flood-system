package com.example.smartfloodsystem;

import java.util.Date;
import java.util.HashMap;

public class Threads {

    private String mThreadTitle;
    private String mThreadContent;
    private Date mThreadDate;
    private String mThreadImageUri;
    private HashMap<String, String> mThreadComment;
    private String mPostUserName;

    public Threads() {

    }


    public Threads(String mThreadTitle, String mThreadContent, Date mThreadDate, String mThreadImageUri, HashMap<String, String> mThreadComment, String mPostUserName) {
        this.mThreadTitle = mThreadTitle;
        this.mThreadContent = mThreadContent;
        this.mThreadDate = mThreadDate;
        this.mThreadImageUri = mThreadImageUri;
        this.mThreadComment = mThreadComment;
        this.mPostUserName = mPostUserName;
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

    public HashMap<String, String> getmThreadComment() {
        return mThreadComment;
    }

    public String getmPostUserName() {
        return mPostUserName;
    }

    public void setmPostUserName(String mPostUserName) {
        this.mPostUserName = mPostUserName;
    }

    public void setmThreadComment(HashMap<String, String> mThreadComment) {
        this.mThreadComment = mThreadComment;
    }
}
