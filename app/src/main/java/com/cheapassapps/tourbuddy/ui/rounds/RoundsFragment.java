package com.cheapassapps.tourbuddy.ui.rounds;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheapassapps.tourbuddy.R;

public class RoundsFragment extends Fragment {

    private RoundsViewModel mViewModel;

    public static RoundsFragment newInstance() {
        return new RoundsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.rounds_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RoundsViewModel.class);
        // TODO: Use the ViewModel
    }

}
