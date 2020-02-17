package com.cheapassapps.tourbuddy.ViewModels;

import com.cheapassapps.tourbuddy.Models.Course;
import com.cheapassapps.tourbuddy.Models.Round;
import com.cheapassapps.tourbuddy.Models.Tee;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentRoundViewModel extends ViewModel {
    private MutableLiveData<Round> currentRound;
    private Round round;

    public CurrentRoundViewModel() {
        this.currentRound = new MutableLiveData<>();
        this.round = new Round();

        setCurrentRound();
    }

    public MutableLiveData<Round> getCurrentRound(){
        return currentRound;
    }

    public void setCurrentRound() {
        currentRound.setValue(round);
    }

    public void setCurrentCourse(Course course){
        this.round.setCourse(course);
        setCurrentRound();
    }

    public void setCurrentTee(Tee tee){
        this.round.setTee(tee);
        setCurrentRound();
    }

    public void setHoleScore(int holeNum, int score){
        switch (holeNum){
            case 1:
                this.round.setScore1(score);
                break;
            case 2:
                this.round.setScore2(score);
                break;
            case 3:
                this.round.setScore3(score);
                break;
            case 4:
                this.round.setScore4(score);
                break;
            case 5:
                this.round.setScore5(score);
                break;
            case 6:
                this.round.setScore6(score);
                break;
            case 7:
                this.round.setScore7(score);
                break;
            case 8:
                this.round.setScore8(score);
                break;
            case 9:
                this.round.setScore9(score);
                break;
            case 10:
                this.round.setScore10(score);
                break;
            case 11:
                this.round.setScore11(score);
                break;
            case 12:
                this.round.setScore12(score);
                break;
            case 13:
                this.round.setScore13(score);
                break;
            case 14:
                this.round.setScore14(score);
                break;
            case 15:
                this.round.setScore15(score);
                break;
            case 16:
                this.round.setScore16(score);
                break;
            case 17:
                this.round.setScore17(score);
                break;
            case 18:
                this.round.setScore18(score);
                break;
        }
    }
}
