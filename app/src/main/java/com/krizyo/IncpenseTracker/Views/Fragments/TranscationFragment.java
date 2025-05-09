package com.krizyo.IncpenseTracker.Views.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.krizyo.IncpenseTracker.Adapters.TranscationAdapter;
import com.krizyo.IncpenseTracker.Models.TranscationModel;
import com.krizyo.IncpenseTracker.Utils.Constant;
import com.krizyo.IncpenseTracker.Utils.Helper;
import com.krizyo.IncpenseTracker.ViewModels.MainViewModel;
import com.krizyo.IncpenseTracker.Views.Activitys.MainActivity;
import com.krizyo.IncpenseTracker.databinding.FragmentTranscationBinding;

import java.util.Calendar;

import io.realm.RealmResults;


public class TranscationFragment extends Fragment {

    BottomSheetDialogFragment fragment;
    Context context;

    public TranscationFragment() {
        // Required empty public constructor
    }

    FragmentTranscationBinding binding;
    static Calendar calendar;

    /*
    0 - Daily
    1 - Monthly
    2 - Calender
    3 - Summary
    4 - Notes
    */

    public static MainViewModel viewModel;

    int isFromMonthly = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTranscationBinding.inflate(inflater);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //viewModel = ((MainActivity)getActivity()).viewModel;

        Constant.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.previousDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.SELECTED_TAB == Constant.DAILY){
                    calendar.add(Calendar.DATE,-1);
                } else if(Constant.SELECTED_TAB == Constant.MONTHLY) {
                    calendar.add(Calendar.MONTH,-1);
                }
                updateDate();
            }
        });

        binding.nextDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.SELECTED_TAB == Constant.DAILY){
                    calendar.add(Calendar.DATE,1);
                } else if(Constant.SELECTED_TAB == Constant.MONTHLY) {
                    calendar.add(Calendar.MONTH,1);
                }
                updateDate();
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddTranscationFragment().show(getParentFragmentManager(),null);
            }
        });

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("Daily")){
                    Constant.SELECTED_TAB = Constant.DAILY;
                    updateDate();
                } else if (tab.getText().toString().equals("Monthly")) {
                    Constant.SELECTED_TAB = Constant.MONTHLY;
                    updateDate();
                }/*else if (tab.getText().toString().equals("Calender")) {
                    Constant.SELECTED_TAB = Constant.CALENDER;
                }else if (tab.getText().toString().equals("summary")) {
                    Constant.SELECTED_TAB = Constant.SUMMARY;
                }else {
                    Constant.SELECTED_TAB = Constant.NOTES;
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.transcationList.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.transcations.observe(getViewLifecycleOwner(), new Observer<RealmResults<TranscationModel>>() {
            @Override
            public void onChanged(RealmResults<TranscationModel> transcationModels) {
                TranscationAdapter transcationAdapter = new TranscationAdapter(getContext(), transcationModels);
                binding.transcationList.setAdapter(transcationAdapter);

                if (transcationModels.size()>0){
                    binding.emptyStateIcon.setVisibility(View.GONE);
                }else {
                    binding.emptyStateIcon.setVisibility(View.VISIBLE);
                }

            }
        });

        viewModel.totalIncome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeText.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseText.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalAmount.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalText.setText(String.valueOf(aDouble));
            }
        });

        viewModel.getTranscations(calendar);


        return binding.getRoot();
    }

    void updateDate(){
        if (Constant.SELECTED_TAB == Constant.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
            viewModel.getTranscations(calendar);
        } else if(Constant.SELECTED_TAB == Constant.MONTHLY) {
            binding.currentDate.setText(Helper.formatDateBymonth(calendar.getTime()));
            viewModel.getTranscations(calendar);
        }

    }

    public static void getTranscations(){
        viewModel.getTranscations(calendar);
    }

}