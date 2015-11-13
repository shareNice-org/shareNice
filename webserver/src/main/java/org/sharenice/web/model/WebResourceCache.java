package org.sharenice.web.model;

/**
 * Created by mischat on 13/11/2015.
 */
public class WebResourceCache {

    private byte[] content;
    private String etag;

    public WebResourceCache(byte[] content, String etag) {
        this.content = content;
        this.etag = etag;
    }

    public byte[] getContent() {
        return content;
    }

    public String getEtag() {
        return etag;
    }
}
