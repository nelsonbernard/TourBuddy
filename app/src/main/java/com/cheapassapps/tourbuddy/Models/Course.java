package com.cheapassapps.tourbuddy.Models;

import java.util.List;

public class Course {

    private int courseId;
    private String name;
    private String subcourse;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private List<Tee> tees;

    public Course(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Tee> getTees() {
        return tees;
    }

    public void setTees(List<Tee> tees) {
        this.tees = tees;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getSubcourse() {
        return subcourse;
    }

    public void setSubcourse(String subcourse) {
        this.subcourse = subcourse;
    }
}
