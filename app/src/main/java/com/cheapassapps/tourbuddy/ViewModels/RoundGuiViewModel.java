package com.cheapassapps.tourbuddy.ViewModels;

import com.cheapassapps.tourbuddy.Models.Round;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoundGuiViewModel extends ViewModel {
    private MutableLiveData<Round> currentRound;

    public RoundGuiViewModel() {

    }

    public LiveData<Round> getRound(){
        return currentRound;
    }
}
