package patel.jay.expensetrack.RechargeManager.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;
import patel.jay.expensetrack.RechargeManager.Transactions.SetBalTransLayout;

/**
 * Created by Jay on 09-08-2017.
 */

public class BalTransAdapter extends RecyclerView.Adapter<BalTransAdapter.AccountViewHolder> {

    private ArrayList<SqlBalAccount> accountArrayList;
    private Activity activity;

    public BalTransAdapter(ArrayList<SqlBalAccount> accountArrayList, Activity activity) {
        this.accountArrayList = accountArrayList;
        this.activity = activity;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trans, parent, false);
        return new AccountViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        final SqlBalAccount sqlBalAccount = accountArrayList.get(position);

        try {
            //region Fill Details
            TimeConvert tc = TimeConvert.timeMiliesConvert(Long.parseLong(sqlBalAccount.getTime()));

            if (sqlBalAccount.getExpense().equals("0")) {
                holder.tvEx.setVisibility(View.GONE);
            } else if (sqlBalAccount.getIncome().equals("0")) {
                holder.tvIn.setVisibility(View.GONE);
            }

            holder.tvEx.setText(sqlBalAccount.getExpense() + "/-");
            holder.tvIn.setText(sqlBalAccount.getIncome() + "/-");
            holder.tvTime.setText(tc.getHH_Min_AP());
            String custName = SqlCustomer.mobileToName(activity, sqlBalAccount.getCustName());
            holder.tvCust.setText(custName);
            if (custName.length() > 6)
                holder.tvCust.setTextSize(14);

            holder.tvDD.setText(MyConstants.numberOf(tc.getDd()));
            holder.tvDateMY.setText(tc.getMMM_YY());
            holder.tvDay.setText(tc.getWeekDay());

            holder.cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetBalTransLayout setBalTransLayout = new SetBalTransLayout(activity);
                    setBalTransLayout.alertEditView(sqlBalAccount);
                }
            });
            //endregion
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        TextView tvEx, tvIn, tvDD, tvDateMY, tvDay, tvTime, tvCust;
        LinearLayout cardLayout;

        AccountViewHolder(View itemView) {
            super(itemView);
            cardLayout = (LinearLayout) itemView.findViewById(R.id.cardLayout);
            tvEx = (TextView) itemView.findViewById(R.id.tvExRed);
            tvIn = (TextView) itemView.findViewById(R.id.tvInBlue);
            tvCust = (TextView) itemView.findViewById(R.id.tvTotalBlack);
            tvTime = (TextView) itemView.findViewById(R.id.tvHHMMAM);
            tvDD = (TextView) itemView.findViewById(R.id.tvDD);
            tvDateMY = (TextView) itemView.findViewById(R.id.tvMMMYY);
            tvDay = (TextView) itemView.findViewById(R.id.tvWeekDay);
        }
    }
}