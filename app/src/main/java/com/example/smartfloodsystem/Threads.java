package com.example.smartfloodsystem;

import java.util.Date;
import java.util.HashMap;

public class Threads {

    private String mThreadTitle;
    private String mThreadContent;
    private Date mThreadDate;
    private HashMap<String, String> mThreadComment;

    public Threads() {

    }

    public Threads(String mThreadTitle, String mThreadContent, Date mThreadDate, HashMap<String, String> mThreadComment) {
        this.mThreadTitle = mThreadTitle;
        this.mThreadContent = mThreadContent;
        this.mThreadDate = mThreadDate;
        this.mThreadComment = mThreadComment;
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

    public HashMap<String, String> getmThreadComment() {
        return mThreadComment;
    }

    public void setmThreadComment(HashMap<String, String> mThreadComment) {
        this.mThreadComment = mThreadComment;
    }
}
