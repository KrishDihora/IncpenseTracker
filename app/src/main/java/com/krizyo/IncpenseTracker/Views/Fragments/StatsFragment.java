package com.krizyo.IncpenseTracker.Views.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.material.tabs.TabLayout;
import com.krizyo.IncpenseTracker.Models.TranscationModel;
import com.krizyo.IncpenseTracker.R;
import com.krizyo.IncpenseTracker.Utils.Constant;
import com.krizyo.IncpenseTracker.Utils.Helper;
import com.krizyo.IncpenseTracker.ViewModels.MainViewModel;
import com.krizyo.IncpenseTracker.databinding.FragmentStatsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.realm.RealmResults;

public class StatsFragment extends Fragment {

    public StatsFragment() {
        // Required empty public constructor
    }

    FragmentStatsBinding binding;
    static Calendar calendar;

    /*
    0 - Daily
    1 - Monthly
    2 - Calender
    3 - Summary
    4 - Notes
    */

    public static MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater);

        viewModel = TranscationFragment.viewModel;

        calendar = Calendar.getInstance();
        updateDate();

        binding.previousDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.SELECTED_TAB_STATS == Constant.DAILY){
                    calendar.add(Calendar.DATE,-1);
                } else if(Constant.SELECTED_TAB_STATS == Constant.MONTHLY) {
                    calendar.add(Calendar.MONTH,-1);
                }
                updateDate();
            }
        });

        binding.nextDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.SELECTED_TAB_STATS == Constant.DAILY){
                    calendar.add(Calendar.DATE,1);
                } else if(Constant.SELECTED_TAB_STATS == Constant.MONTHLY) {
                    calendar.add(Calendar.MONTH,1);
                }
                updateDate();
            }
        });

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("Daily")){
                    Constant.SELECTED_TAB_STATS = Constant.DAILY;
                    updateDate();
                } else if (tab.getText().toString().equals("Monthly")) {
                    Constant.SELECTED_TAB_STATS = Constant.MONTHLY;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.black));

                Constant.SELECTED_STATS_TYPE = Constant.INCOME;
            }
        });

        binding.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.black));

                Constant.SELECTED_STATS_TYPE = Constant.EXPENSE;
            }
        });

        //chart
        Pie pie = AnyChart.pie();

        viewModel.categoryTranscations.observe(getViewLifecycleOwner(), new Observer<RealmResults<TranscationModel>>() {
            @Override
            public void onChanged(RealmResults<TranscationModel> transcationModels) {

                if (transcationModels.size()>0){

                    binding.emptyStateIcon.setVisibility(View.GONE);
                    binding.anyChart.setVisibility(View.VISIBLE);

                    ArrayList<DataEntry> data = new ArrayList<>();
                    Map<String,Double> categoryMap = new HashMap<>();

                    for (TranscationModel transcationModel:transcationModels) {
                        String category = transcationModel.getCategory();
                        double amount = transcationModel.getAmount();

                        if (categoryMap.containsKey(category)){
                            double currentTotal = categoryMap.get(category).doubleValue();
                            currentTotal += amount;
                            categoryMap.put(category,Math.abs(currentTotal));
                        }else {
                            categoryMap.put(category,Math.abs(amount));
                        }

                    }

                    for (Map.Entry<String,Double> entry:categoryMap.entrySet()) {
                        data.add(new ValueDataEntry(entry.getKey(),entry.getValue()));
                    }

                    pie.data(data);

                }else {

                    binding.emptyStateIcon.setVisibility(View.VISIBLE);
                    binding.anyChart.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getTranscations(calendar,Constant.SELECTED_STATS_TYPE);

        binding.anyChart.setChart(pie);

        return binding.getRoot();
    }

    void updateDate(){
        if (Constant.SELECTED_TAB_STATS == Constant.DAILY){
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
            viewModel.getTranscations(calendar,Constant.SELECTED_STATS_TYPE);
        } else if(Constant.SELECTED_TAB_STATS == Constant.MONTHLY) {
            binding.currentDate.setText(Helper.formatDateBymonth(calendar.getTime()));
            viewModel.getTranscations(calendar,Constant.SELECTED_STATS_TYPE);
        }
    }

}