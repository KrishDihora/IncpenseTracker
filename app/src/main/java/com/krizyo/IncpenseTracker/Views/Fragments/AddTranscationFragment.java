package com.krizyo.IncpenseTracker.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.krizyo.IncpenseTracker.Adapters.AccountAdapter;
import com.krizyo.IncpenseTracker.Adapters.CategoryAdapter;
import com.krizyo.IncpenseTracker.Models.AccountModel;
import com.krizyo.IncpenseTracker.Models.CategoryModel;
import com.krizyo.IncpenseTracker.Models.TranscationModel;
import com.krizyo.IncpenseTracker.R;
import com.krizyo.IncpenseTracker.Utils.Constant;
import com.krizyo.IncpenseTracker.Utils.Helper;
import com.krizyo.IncpenseTracker.Views.Activitys.MainActivity;
import com.krizyo.IncpenseTracker.databinding.FragmentAddTranscationBinding;
import com.krizyo.IncpenseTracker.databinding.LayoutListDialogBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTranscationFragment extends BottomSheetDialogFragment {


    FragmentAddTranscationBinding binding;
    TranscationModel transcationModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTranscationBinding.inflate(inflater);

        transcationModel = new TranscationModel();

        binding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.black));

                transcationModel.setType(Constant.INCOME);
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

                transcationModel.setType(Constant.EXPENSE);
            }
        });



        binding.date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.show();

                datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                        calendar.set(Calendar.MONTH,datePicker.getMonth());
                        calendar.set(Calendar.YEAR,datePicker.getYear());

                        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                        String dateToShow = Helper.formatDate(calendar.getTime());

                        binding.date.setText(dateToShow);

                        transcationModel.setDate(calendar.getTime());
                        transcationModel.setId(calendar.getTime().getTime());
                    }
                });
            }
        });

        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutListDialogBinding dialogBinding = LayoutListDialogBinding.inflate(inflater);

                AlertDialog categorydialog = new AlertDialog.Builder(getContext()).create();
                categorydialog.setView(dialogBinding.getRoot());
                categorydialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.white)));


                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constant.categories, new CategoryAdapter.CategoryClickListener() {
                    @Override
                    public void onCategoryClicked(CategoryModel categoryModel) {
                        binding.category.setText(categoryModel.getCategoryName());
                        transcationModel.setCategory(categoryModel.getCategoryName());
                        categorydialog.dismiss();
                    }
                });
                dialogBinding.list.setLayoutManager(new GridLayoutManager(getContext(),3 ));
                dialogBinding.list.setAdapter(categoryAdapter);

                categorydialog.show();
            }
        });

        binding.accountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutListDialogBinding dialogBinding = LayoutListDialogBinding.inflate(inflater);
                AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
                accountDialog.setView(dialogBinding.getRoot());
                accountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.white)));

                ArrayList<AccountModel> accounts = new ArrayList<>();
                accounts.add(new AccountModel(0,"Cash"));
                accounts.add(new AccountModel(0,"UPI"));
                accounts.add(new AccountModel(0,"Card"));
                accounts.add(new AccountModel(0,"Bank"));
                accounts.add(new AccountModel(0,"Other"));

                AccountAdapter accountAdapter = new AccountAdapter(getContext(), accounts, new AccountAdapter.AccountClickListener() {
                    @Override
                    public void onAccountSelected(AccountModel accountModel) {
                        binding.accountType.setText(accountModel.getAccountName());
                        transcationModel.setAccountType(accountModel.getAccountName());
                        accountDialog.dismiss();
                    }
                });
                dialogBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
                dialogBinding.list.setAdapter(accountAdapter);

                accountDialog.show();
            }
        });

        binding.saveTranscationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( (areDrawablesEqual(binding.incomeBtn.getBackground(),ContextCompat.getDrawable(getContext(),R.drawable.default_selector)) &&
                        areDrawablesEqual(binding.expenseBtn.getBackground(),ContextCompat.getDrawable(getContext(),R.drawable.default_selector))) ||
                        binding.date.getText().toString().isEmpty() || binding.amount.getText().toString().isEmpty() ||
                        binding.category.getText().toString().isEmpty() || binding.accountType.getText().toString().isEmpty() ){

                    Toast.makeText(getContext(),"Enter all field !",Toast.LENGTH_SHORT).show();

                }else {
                    double amount = Double.parseDouble(binding.amount.getText().toString());
                    String note = binding.note.getText().toString();

                    if (transcationModel.getType().equals(Constant.EXPENSE)){
                        transcationModel.setAmount(amount*-1);
                    }else {
                        transcationModel.setAmount(amount);
                    }

                    transcationModel.setNote(note);

                    TranscationFragment.viewModel.addTranscations(transcationModel);
                    TranscationFragment.getTranscations();
                    dismiss();
                }

            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    public boolean areDrawablesEqual(Drawable d1, Drawable d2) {
        if (d1 == null || d2 == null) return false;

        // Option 1: Compare by constant state (fast, works for most cases)
        return d1.getConstantState() != null &&
                d1.getConstantState().equals(d2.getConstantState());
    }
}