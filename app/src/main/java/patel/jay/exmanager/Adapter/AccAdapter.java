package patel.jay.exmanager.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.ViewHolder.CatVH;
import patel.jay.exmanager.Expense.Category.CatDialog;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Account;

/**
 * Created by Jay on 8/27/2017.
 */

public class AccAdapter extends RecyclerView.Adapter<CatVH> {

    private ArrayList<Account> accountArrayList;
    private Activity activity;

    public AccAdapter(ArrayList<Account> accountArrayList, Activity activity) {
        this.accountArrayList = accountArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new CatVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatVH holder, int position) {
        final Account account = accountArrayList.get(position);
        holder.tvCat.setText(account.getAccName());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatDialog catDialog = new CatDialog(activity);
                catDialog.alertEditAcc(account);
            }
        };

        holder.tvCat.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

}
