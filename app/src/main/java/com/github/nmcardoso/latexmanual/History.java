package com.github.nmcardoso.latexmanual;

public class History {
    private Documentation documentation;
    private int id;
    private String createdAt;

    public History() {
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
}
