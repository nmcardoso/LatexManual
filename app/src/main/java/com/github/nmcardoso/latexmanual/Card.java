package com.github.nmcardoso.latexmanual;

import java.util.List;

public class Card {
    private final String title;
    private final List<?> contentList;
    private final String contentText;
    private final int contentType;
    private final int flag;
    private final int headerBackground;
    private final int headerIcon;
    private final boolean viewMore;

    public static final int TEXT = 0;
    public static final int LIST = 1;

    public Card(CardBuilder builder) {
        this.title = builder.title;
        this.contentList = builder.contentList;
        this.contentText = builder.contentText;
        this.contentType = builder.contentType;
        this.flag = builder.flag;
        this.headerBackground = builder.headerBackground;
        this.headerIcon = builder.headerIcon;
        this.viewMore = builder.viewMore;
    }

    public String getTitle() {
        return title;
    }

    public List<?> getContentList() {
        return contentList;
    }

    public String getContentText() {
        return contentText;
    }

    public int getContentType() {
        return contentType;
    }

    public int getFlag() {
        return flag;
    }

    public int getHeaderBackground() {
        return headerBackground;
    }

    public int getHeaderIcon() {
        return headerIcon;
    }

    public boolean hasViewMoreButton() {
        return viewMore;
    }

    public static class CardBuilder {
        private String title;
        private List<?> contentList = null;
        private String contentText = null;
        private int contentType;
        private int flag;
        private int headerBackground = 0;
        private int headerIcon = 0;
        private boolean viewMore = false;

        public CardBuilder(String title, String contentText) {
            this.title = title;
            this.contentText = contentText;
            this.contentType = Card.TEXT;
        }

        public CardBuilder(String title, List<?> contentList) {
            this.title = title;
            this.contentList = contentList;
            this.contentType = Card.LIST;
        }

        public CardBuilder flag(int flag) {
            this.flag = flag;
            return this;
        }

        public CardBuilder headerBackground(int resId) {
            this.headerBackground = resId;
            return this;
        }

        public CardBuilder headerIcon(int resId) {
            this.headerIcon = resId;
            return this;
        }

        public CardBuilder viewMore(boolean viewMore) {
            this.viewMore = viewMore;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
