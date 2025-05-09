package com.krizyo.IncpenseTracker.Adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.krizyo.IncpenseTracker.Models.CategoryModel;
import com.krizyo.IncpenseTracker.Models.TranscationModel;
import com.krizyo.IncpenseTracker.R;
import com.krizyo.IncpenseTracker.Utils.Constant;
import com.krizyo.IncpenseTracker.Utils.Helper;
import com.krizyo.IncpenseTracker.Views.Activitys.MainActivity;
import com.krizyo.IncpenseTracker.Views.Fragments.TranscationFragment;
import com.krizyo.IncpenseTracker.databinding.LayoutTranscationRowBinding;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TranscationAdapter extends RecyclerView.Adapter<TranscationAdapter.TranscationViewHolder> {

    Context context;
    RealmResults<TranscationModel> transcations;

    public TranscationAdapter(Context context, RealmResults<TranscationModel> transcations) {

        this.context = context;
        this.transcations = transcations;
    }

    @NonNull
    @Override
    public TranscationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TranscationViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_transcation_row,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull TranscationViewHolder holder, int position) {
        CategoryModel transcationCategory = Constant.getCategoryDetails(transcations.get(position).getCategory());
        holder.binding.categoryIcon.setImageResource(transcationCategory.getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transcationCategory.getCategoryColor()));

        holder.binding.category.setText(transcations.get(position).getCategory());
        holder.binding.accountType.setText(transcations.get(position).getAccountType());
        holder.binding.accountType.setBackgroundTintList(context.getColorStateList(Constant.getAccountTypeColor(transcations.get(position).getAccountType())));
        holder.binding.date.setText(Helper.formatDate(transcations.get(position).getDate()));

        holder.binding.amount.setText(String.valueOf(transcations.get(position).getAmount()));
        if (transcations.get(position).getType().equals(Constant.INCOME)){
            holder.binding.amount.setTextColor(context.getColor(R.color.green));
        }else {
            holder.binding.amount.setTextColor(context.getColor(R.color.red));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog deleteDialog = new AlertDialog.Builder(context).create();
                deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context,R.color.lightBlack)));
                deleteDialog.setTitle("Delete Transcation");
                deleteDialog.setMessage("Are you sure to delete this transcation?");

                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TranscationFragment.viewModel.deleteTranscation(transcations.get(position));
                    }
                });


                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDialog.dismiss();
                    }
                });

                deleteDialog.show();

                deleteDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.white));
                deleteDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context,R.color.white));



                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transcations.size();
    }

    public class TranscationViewHolder extends RecyclerView.ViewHolder{
        LayoutTranscationRowBinding binding;
        public TranscationViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = LayoutTranscationRowBinding.bind(itemView);
        }
    }
}
