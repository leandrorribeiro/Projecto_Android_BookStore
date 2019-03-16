package com.example.projecto_android_leandroribeiro_final.model;

import io.realm.RealmObject;

public class Favorites extends RealmObject {
    private String id;
    private String ivImage;
    private String tvNome;
    private String tvData;
    private String tvSub;

    public Favorites(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIvImage() {
        return ivImage;
    }

    public void setIvImage(String ivImage) {
        this.ivImage = ivImage;
    }

    public String getTvNome() {
        return tvNome;
    }

    public void setTvNome(String tvNome) {
        this.tvNome = tvNome;
    }

    public String getTvData() {
        return tvData;
    }

    public void setTvData(String tvData) {
        this.tvData = tvData;
    }

    public String getTvSub() {
        return tvSub;
    }

    public void setTvSub(String tvSub) {
        this.tvSub = tvSub;
    }
}
