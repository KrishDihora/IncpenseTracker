package com.krizyo.IncpenseTracker.Utils;

import com.krizyo.IncpenseTracker.Models.CategoryModel;
import com.krizyo.IncpenseTracker.R;

import java.util.ArrayList;

public class Constant {
    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";

    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int CALENDER = 2;
    public static int SUMMARY = 3;
    public static int NOTES = 4;
    public static int SELECTED_TAB = 0;

    public static ArrayList<CategoryModel> categories;

    public static void setCategories(){
        categories = new ArrayList<>();
        categories.add(new CategoryModel("Salary", R.drawable.ic_salary,R.color.category1));
        categories.add(new CategoryModel("Business",R.drawable.ic_business,R.color.category2));
        categories.add(new CategoryModel("Investment",R.drawable.ic_investment,R.color.category3));
        categories.add(new CategoryModel("Loan",R.drawable.ic_loan,R.color.category4));
        categories.add(new CategoryModel("Rent",R.drawable.ic_rent,R.color.category5));
        categories.add(new CategoryModel("Other",R.drawable.ic_other,R.color.category6));
    }

    public static CategoryModel getCategoryDetails(String categoryName){
        for (CategoryModel categoryModel : categories) {
            if (categoryModel.getCategoryName().equals(categoryName)){
                return categoryModel;
            }
        }
        return null;
    }

    public static int getAccountTypeColor(String accountType){
        switch (accountType){
            case "Cash":
                return R.color.cashColor;
            case "Card":
                return R.color.cardColor;
            case "Bank":
                return R.color.bankColor;
            case "UPI":
                return R.color.upiColor;
            default:
                return R.color.defaultColor;
        }
    }
}
