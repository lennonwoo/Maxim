package com.lennonwoo.maxim.model;

public class Maxim {
    private String maxim;
    private int id;

    public Maxim(String maxim, int id) {
        this.maxim = maxim;
        this.id = id;
    }

    public String getMaxim() {
        return maxim;
    }

    public void setMaxim(String maxim) {
        this.maxim = maxim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
