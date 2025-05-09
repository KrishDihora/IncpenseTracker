package com.krizyo.IncpenseTracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krizyo.IncpenseTracker.Models.AccountModel;
import com.krizyo.IncpenseTracker.R;
import com.krizyo.IncpenseTracker.databinding.LayoutAccountRowBinding;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    Context context;
    ArrayList<AccountModel> accounts;
    AccountClickListener accountClickListener;

    public interface AccountClickListener{
        void onAccountSelected(AccountModel accountModel);
    }

    public AccountAdapter(Context context, ArrayList<AccountModel> accounts,AccountClickListener accountClickListener) {
        this.context = context;
        this.accounts = accounts;
        this.accountClickListener=accountClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_account_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.binding.accountName.setText(accounts.get(position).getAccountName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountClickListener.onAccountSelected( accounts.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{
        LayoutAccountRowBinding binding;
        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = LayoutAccountRowBinding.bind(itemView);
        }
    }
}


