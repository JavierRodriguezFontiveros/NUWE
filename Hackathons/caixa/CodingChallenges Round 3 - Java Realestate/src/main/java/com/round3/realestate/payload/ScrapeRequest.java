package com.round3.realestate.payload;

public class ScrapeRequest {

    private String url;
    private boolean store;

    // Getters y setters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }
}
