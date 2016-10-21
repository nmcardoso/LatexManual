package com.github.nmcardoso.latexmanual;

import android.view.View;

import java.util.List;

public class Card {
    private final String title;
    private final List<?> contentList;
    private final CharSequence contentText;
    private final int contentType;
    private final int listItemType;
    private final int headerBackground;
    private final int headerIcon;
    private final boolean viewMore;
    private final View.OnClickListener clickListener;

    public static final int TEXT = 0;
    public static final int LIST = 1;

    public Card(CardBuilder builder) {
        this.title = builder.title;
        this.contentList = builder.contentList;
        this.contentText = builder.contentText;
        this.contentType = builder.contentType;
        this.listItemType = builder.listItemType;
        this.headerBackground = builder.headerBackground;
        this.headerIcon = builder.headerIcon;
        this.viewMore = builder.viewMore;
        this.clickListener = builder.clickListener;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public String getTitle() {
        return title;
    }

    public List<?> getContentList() {
        return contentList;
    }

    public CharSequence getContentText() {
        return contentText;
    }

    public int getContentType() {
        return contentType;
    }

    public int getListItemType() {
        return listItemType;
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
        private CharSequence contentText = null;
        private int contentType;
        private int listItemType;
        private int headerBackground = 0;
        private int headerIcon = 0;
        private boolean viewMore = false;
        private View.OnClickListener clickListener = null;

        public CardBuilder(String title, CharSequence contentText) {
            this.title = title;
            this.contentText = contentText;
            this.contentType = Card.TEXT;
        }

        public CardBuilder(String title, List<?> contentList) {
            this.title = title;
            this.contentList = contentList;
            this.contentType = Card.LIST;
        }

        public CardBuilder listItemType(int listItemType) {
            this.listItemType = listItemType;
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

        public CardBuilder clickListener(View.OnClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}
