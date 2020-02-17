package com.cheapassapps.tourbuddy.Models;

import java.util.List;

public class Tee {

    private int courseId;
    private int teeId;
    private String color;
    private int rating;
    private float slope;
    private List<Hole> holeList;

    public Tee(){

    }

    public List<Hole> getHoleList() {
        return holeList;
    }

    public void setHoleList(List<Hole> holeList) {
        this.holeList = holeList;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public float getSlope() {
        return slope;
    }

    public void setSlope(float slope) {
        this.slope = slope;
    }

    public int getTeeId() {
        return teeId;
    }

    public void setTeeId(int teeId) {
        this.teeId = teeId;
    }

}
