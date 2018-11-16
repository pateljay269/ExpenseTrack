package patel.jay.exmanager.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.ViewHolder.CardVH;
import patel.jay.exmanager.Expense.Transaction.TransDialog;
import patel.jay.exmanager.SQL.Account;
import patel.jay.exmanager.SQL.Category;
import patel.jay.exmanager.SQL.Trans;
import patel.jay.exmanager.Utility.TimeConvert;

import static android.view.View.GONE;
import static java.lang.Long.parseLong;
import static patel.jay.exmanager.R.drawable.border;
import static patel.jay.exmanager.R.layout.row_cardview;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;


/**
 * Created by Jay on 27-07-2017.
 */

public class TransAdapter extends RecyclerView.Adapter<CardVH> {

    private ArrayList<Trans> transList;
    private Activity activity;
    private boolean dscDisplay;

    public TransAdapter(ArrayList<Trans> transList, Activity activity) {
        this.transList = transList;
        this.activity = activity;
        this.dscDisplay = false;
    }

    public TransAdapter(ArrayList<Trans> transList, Activity activity, boolean dscDisplay) {
        this(transList, activity);
        this.dscDisplay = dscDisplay;
    }

    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(row_cardview, parent, false);
        return new CardVH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        final Trans trans = transList.get(position);

        //region Fill Details

        if ((trans.getEx_rs() + "").length() > 6) {
            holder.tvRs.setTextSize(18);
        }
        holder.tvRs.setText(trans.getEx_rs() + "/-");

        TimeConvert tc = timeMilies(parseLong(trans.getTime()));
        holder.tvDateDD.setText(new TimeConvert().numberOf(tc.getDd()));
        holder.tvDateMY.setText(tc.getMMM_YY());
        holder.tvTime.setText(tc.getHH_Min_AP());
        holder.tvDay.setText(tc.getWeekDay());

        holder.tvDsc.setText(new Account().accString(activity, trans.getAcid()));
        holder.tvCat.setText(new Category().catStr(activity, trans.getCid()));

        if (dscDisplay) {
            holder.tvExtra.setText(trans.getDescription());
            holder.linearMain.setBackground(activity.getResources().getDrawable(border));
        }

        if (!dscDisplay || trans.getDescription().isEmpty()) {
            holder.tvExtra.setVisibility(GONE);
        }
        //endregion

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransDialog transDialog = new TransDialog(activity);
                transDialog.alertEditView(trans);
            }
        };

        holder.cardLayout.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return transList.size();
    }

    public ArrayList<Trans> getTransList() {
        return transList;
    }
}