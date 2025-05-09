package com.krizyo.IncpenseTracker.Views.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krizyo.IncpenseTracker.R;
import com.krizyo.IncpenseTracker.databinding.FragmentStatsBinding;

public class StatsFragment extends Fragment {

    public StatsFragment() {
        // Required empty public constructor
    }

    FragmentStatsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater);


        return binding.getRoot();
    }
}