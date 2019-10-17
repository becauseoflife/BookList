package com.casper.testdrivendevelopment;

/**
 * Created by jszx on 2019/9/24.
 */

public class Book {
    private String title;
    private int CoverResourceId;

    public Book(String title, int coverResourceId) {
        this.title = title;
        CoverResourceId = coverResourceId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCoverResourceId() {
        return CoverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        CoverResourceId = coverResourceId;
    }


}
