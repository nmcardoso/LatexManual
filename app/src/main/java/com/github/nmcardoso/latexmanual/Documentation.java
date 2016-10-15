package com.github.nmcardoso.latexmanual;

public class Documentation {
    private int id;
    private String title;
    private String fileName;
    private String data;

    public Documentation() {
    }

    public Documentation(int id, String title, String fileName, String data) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
