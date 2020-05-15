package com.example.mahe.pilldispenser.ClassFiles;

public class Tray {


    int morn_bef_food;
    int morn_aft_food;
    int aftnoon_bef_food;
    int aftnoon_aft_food;
    int night_bef_food;
    int night_aft_food;


    String tab_name;

    public String getTab_name() {
        return tab_name;
    }

    public Tray() {
    }

    public int getMorn_bef_food() {
        return morn_bef_food;
    }

    public void setMorn_bef_food(int morn_bef_food) {
        this.morn_bef_food = morn_bef_food;
    }

    public int getMorn_aft_food() {
        return morn_aft_food;
    }

    public void setMorn_aft_food(int morn_aft_food) {
        this.morn_aft_food = morn_aft_food;
    }

    public int getAftnoon_bef_food() {
        return aftnoon_bef_food;
    }

    public void setAftnoon_bef_food(int aftnoon_bef_food) {
        this.aftnoon_bef_food = aftnoon_bef_food;
    }

    public int getAftnoon_aft_food() {
        return aftnoon_aft_food;
    }

    public void setAftnoon_aft_food(int aftnoon_aft_food) {
        this.aftnoon_aft_food = aftnoon_aft_food;
    }

    public int getNight_bef_food() {
        return night_bef_food;
    }

    public void setNight_bef_food(int night_bef_food) {
        this.night_bef_food = night_bef_food;
    }

    public int getNight_aft_food() {
        return night_aft_food;
    }

    public void setNight_aft_food(int night_aft_food) {
        this.night_aft_food = night_aft_food;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }

    public Tray(int morn_bef_food, int morn_aft_food, int aftnoon_bef_food, int aftnoon_aft_food, int night_bef_food, int night_aft_food, String tab_name) {
        this.morn_bef_food = morn_bef_food;
        this.morn_aft_food = morn_aft_food;
        this.aftnoon_bef_food = aftnoon_bef_food;
        this.aftnoon_aft_food = aftnoon_aft_food;
        this.night_bef_food = night_bef_food;
        this.night_aft_food = night_aft_food;
        this.tab_name = tab_name;
    }
}
