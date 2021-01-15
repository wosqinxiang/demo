package com.ahdms.ctidservice.db.model;

public class CtidPic {
    private String id;

    private String ctidHash;

    private String picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCtidHash() {
        return ctidHash;
    }

    public void setCtidHash(String ctidHash) {
        this.ctidHash = ctidHash == null ? null : ctidHash.trim();
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture == null ? null : picture.trim();
    }
}