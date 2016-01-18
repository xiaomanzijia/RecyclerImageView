package com.licheng.github.recyclerview.model;

import android.net.Uri;

/**
 * Created by licheng on 16/1/16.
 */
public class FrescoRecycler {
    private String text;
    private Uri uri;

    public FrescoRecycler() {
    }

    public FrescoRecycler(String text, Uri uri) {
        this.text = text;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
