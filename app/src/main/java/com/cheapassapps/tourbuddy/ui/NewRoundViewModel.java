package com.cheapassapps.tourbuddy.ui;

import com.cheapassapps.tourbuddy.Models.Round;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewRoundViewModel extends ViewModel {
    private MutableLiveData<Round> currentRound;
    private Round round;

    public void NewRoundViewModel(){
        this.currentRound = new MutableLiveData<>();
        this.round = new Round();
    }
}
