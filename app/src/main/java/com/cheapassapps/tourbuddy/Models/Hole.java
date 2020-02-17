package com.cheapassapps.tourbuddy.Models;

public class Hole {

    private int courseId;
    private int hole_number;
    private int yardage;
    private int handicap;
    private double front;
    private double middle;
    private double back;

    public Hole(){

    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getHole_number() {
        return hole_number;
    }

    public void setHole_number(int hole_number) {
        this.hole_number = hole_number;
    }

    public int getYardage() {
        return yardage;
    }

    public void setYardage(int yardage) {
        this.yardage = yardage;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }

    public double getFront() {
        return front;
    }

    public void setFront(double front) {
        this.front = front;
    }

    public double getMiddle() {
        return middle;
    }

    public void setMiddle(double middle) {
        this.middle = middle;
    }

    public double getBack() {
        return back;
    }

    public void setBack(double back) {
        this.back = back;
    }
}
