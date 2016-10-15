package com.github.nmcardoso.latexmanual;

import java.util.List;

public class Card {
    private String title;
    private List<Object> contentList = null;
    private String contentString = null;
    private int contentType;

    public static final int STRING = 0;
    public static final int LIST = 1;

    public Card(int contentType) {
        this.contentType = contentType;
    }

    public Card(String title, List<Object> contentList) {
        this.title = title;
        this.contentList = contentList;
        this.contentType = LIST;
    }

    public Card(String title, String contentString) {
        this.title = title;
        this.contentString = contentString;
        this.contentType = STRING;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getContentList() {
        return contentList;
    }

    public void setContentList(List<Object> contentList) {
        this.contentList = contentList;
    }

    public String getContentString() {
        return contentString;
    }

    public void setContentString(String contentString) {
        this.contentString = contentString;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }
}
