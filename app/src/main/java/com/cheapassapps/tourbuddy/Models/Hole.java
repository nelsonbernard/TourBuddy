package com.cheapassapps.tourbuddy.Models;

public class Hole {

    private int courseId;
    private int hole_number;
    private int par;
    private int hdcpMen;
    private int hdcpWomen;
    private int yards;
    private double frontLat = 0;
    private double frontLong = 0;
    private double middleLat = 0;
    private double middleLong = 0;
    private double backLat = 0;
    private double backLong = 0;

    public Hole(){

    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public int getYards() { return yards; }

    public void setYards(int yards) { this.yards = yards;}

    public int getHdcpMen() {
        return hdcpMen;
    }

    public void setHdcpMen(int hdcpMen) {
        this.hdcpMen = hdcpMen;
    }

    public int getHdcpWomen() {
        return hdcpWomen;
    }

    public void setHdcpWomen(int hdcpWomen) {
        this.hdcpWomen = hdcpWomen;
    }

    public double getFrontLat() {
        return frontLat;
    }

    public void setFrontLat(double frontLat) {
        this.frontLat = frontLat;
    }

    public double getFrontLong() {
        return frontLong;
    }

    public void setFrontLong(double frontLong) {
        this.frontLong = frontLong;
    }

    public double getMiddleLat() {
        return middleLat;
    }

    public void setMiddleLat(double middleLat) {
        this.middleLat = middleLat;
    }

    public double getMiddleLong() {
        return middleLong;
    }

    public void setMiddleLong(double middleLong) {
        this.middleLong = middleLong;
    }

    public double getBackLat() {
        return backLat;
    }

    public void setBackLat(double backLat) {
        this.backLat = backLat;
    }

    public double getBackLong() {
        return backLong;
    }

    public void setBackLong(double backLong) {
        this.backLong = backLong;
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
}
