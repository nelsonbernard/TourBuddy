package com.cheapassapps.tourbuddy.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cheapassapps.tourbuddy.Models.Round;
import com.cheapassapps.tourbuddy.R;
import com.cheapassapps.tourbuddy.ViewModels.CurrentRoundViewModel;

public class NewRoundFragment extends Fragment {

    private NewRoundViewModel mViewModel;
    private CurrentRoundViewModel mCurrentRoundViewModel;
    private TextView txtName;
    private TextView txtAddress;

    public static NewRoundFragment newInstance() {
        return new NewRoundFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newround, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewRoundViewModel.class);

        mCurrentRoundViewModel = ViewModelProviders.of(this).get(CurrentRoundViewModel.class);
        mCurrentRoundViewModel.getCurrentRound().observe(this, new Observer<Round>(){
            @Override
            public void onChanged(Round round) {
                txtName.setText(round.getCourse().getName());
                txtAddress.setText(round.getCourse().getAddress1());
            }
        });
    }

}
