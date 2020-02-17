package com.cheapassapps.tourbuddy.ui.rounds;

import com.cheapassapps.tourbuddy.Models.Round;
import java.util.List;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoundsViewModel extends ViewModel {
    private MutableLiveData<List<Round>> rounds = new MutableLiveData<>();

    public MutableLiveData<List<Round>> getCourses(){

        return rounds;
    }
}
