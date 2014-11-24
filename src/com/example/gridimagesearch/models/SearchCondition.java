package com.example.gridimagesearch.models;

public class SearchCondition {
    private String imageColor;
    private String imageSize;
    private String imageType;
    private String query;
    private String site;
    private int size = 8;
    
    public String getImageColor() {
        return imageColor;
    }

    public void setImageColor(String imageColor) {
        this.imageColor = imageColor;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
    
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
