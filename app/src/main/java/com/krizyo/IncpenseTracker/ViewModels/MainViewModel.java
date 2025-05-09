package com.krizyo.IncpenseTracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.krizyo.IncpenseTracker.Models.TranscationModel;
import com.krizyo.IncpenseTracker.Utils.Constant;
import com.krizyo.IncpenseTracker.Views.Activitys.MainActivity;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setUpDatabase();
    }

    Realm realm;
    Calendar calendar;
    public MutableLiveData<RealmResults<TranscationModel>> transcations = new MutableLiveData<>();
    public MutableLiveData<Double> totalIncome = new MutableLiveData<>();
    public MutableLiveData<Double> totalExpense = new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount = new MutableLiveData<>();

    void setUpDatabase(){
        realm = Realm.getDefaultInstance();
    }

    public void addTranscations(TranscationModel transcationModel){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transcationModel);
        realm.commitTransaction();
    }
    public void getTranscations(Calendar calendar){

        this.calendar=calendar;

        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        RealmResults<TranscationModel> transcationModels = null;
        double income = 0;
        double expense = 0;
        double amount = 0;

        if(Constant.SELECTED_TAB == Constant.DAILY){

            transcationModels = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() + (24*60*60*1000)))
                    .findAll();


            income = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() + (24*60*60*1000)))
                    .equalTo("type",Constant.INCOME)
                    .sum("amount").doubleValue();


            expense = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() + (24*60*60*1000)))
                    .equalTo("type",Constant.EXPENSE)
                    .sum("amount").doubleValue();


            amount = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() + (24*60*60*1000)))
                    .sum("amount").doubleValue();


        } else if (Constant.SELECTED_TAB == Constant.MONTHLY) {

            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime = calendar.getTime();
            calendar.add(Calendar.MONTH,1);
            Date endTime = calendar.getTime();

            transcationModels = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .findAll();

            income = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .equalTo("type",Constant.INCOME)
                    .sum("amount").doubleValue();


            expense = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .equalTo("type",Constant.EXPENSE)
                    .sum("amount").doubleValue();


            amount = realm.where(TranscationModel.class)
                    .greaterThanOrEqualTo("date",startTime)
                    .lessThan("date",endTime)
                    .sum("amount").doubleValue();

        }

        transcations.setValue(transcationModels);
        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(amount);


    }

    public void deleteTranscation(TranscationModel transcationModel){
        realm.beginTransaction();
        transcationModel.deleteFromRealm();
        realm.commitTransaction();
        getTranscations(calendar);
    }

}
