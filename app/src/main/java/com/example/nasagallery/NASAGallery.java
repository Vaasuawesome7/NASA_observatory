package com.example.nasagallery;

@SuppressWarnings("all")
public class NASAGallery {
    private final String date;

    private final String explanation;

    private final String hdurl;

    private final String mediaType;

    private final String serviceVersion;

    private final String title;

    private final String url;

    public NASAGallery(String date, String explanation, String hdurl, String mediaType,
                       String serviceVersion, String title, String url) {
        this.date = date;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.mediaType = mediaType;
        this.serviceVersion = serviceVersion;
        this.title = title;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
