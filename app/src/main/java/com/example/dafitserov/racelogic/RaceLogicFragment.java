package com.example.dafitserov.racelogic;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dafitserov.racelogic.databinding.FragmentRaceLogicBinding;

public class RaceLogicFragment extends Fragment {

    FragmentRaceLogicBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_race_logic, container,false);

        view = binding.getRoot();

        return view;
    }
}