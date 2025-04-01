package com.round3.realestate.payload;

public class ScrapeResponse {

    private PropertyData data;
    private boolean saved;

    public ScrapeResponse() {
    }

    public ScrapeResponse(PropertyData data, boolean saved) {
        this.data = data;
        this.saved = saved;
    }

    // Getters y setters
    public PropertyData getData() {
        return data;
    }

    public void setData(PropertyData data) {
        this.data = data;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
