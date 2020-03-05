package com.cheapassapps.tourbuddy.Models;

public class Round {
    private int roundId;
    private Course course;
    private Tee tee;
    private Hole[] holes = new Hole[18];
    private int[] score = new int[18];

    public Round(){
        for (int i = 0; i < 18; i++){
            holes[i] = new Hole();
            score[i] = 0;
        }
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Tee getTee() {
        return tee;
    }

    public void setTee(Tee tee) {
        this.tee = tee;
    }

    public int getScore(int hole){ return this.score[hole]; }

    public void setScore(int hole, int score){
        this.score[hole] = score;
    }

    public Hole getHole(int hole){
        return this.holes[hole];
    }

    public int getHoleCount() { return this.holes.length; }

    public void setHole(int index, Hole hole){
        this.holes[index] = hole;
    }

}
