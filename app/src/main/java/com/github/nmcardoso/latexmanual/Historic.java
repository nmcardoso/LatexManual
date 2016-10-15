package com.github.nmcardoso.latexmanual;

public class Historic {
    private Documentation documentation;
    private int id;
    private String createdAt;
    private int viewCount;

    public Historic() {
    }

    @Override
    public String toString() {
        return getDocumentation().getTitle();
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
