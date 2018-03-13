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
import patel.jay.expensetrack.ExpenseManager.Find.SetCalanderData;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;

/**
 * Created by Jay on 31-07-2017.
 */

public class CalenderBalAdapter extends RecyclerView.Adapter<CalenderBalAdapter.AccountViewHolder> {

    private final TimeConvert nowTc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
    private String current;

    private ArrayList<SqlBalAccount> accountArrayList;
    private Activity activity;
    private String type;

    public CalenderBalAdapter(ArrayList<SqlBalAccount> accountArrayList, Activity activity, String type) {
        this.accountArrayList = accountArrayList;
        this.activity = activity;
        this.type = type;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trans, parent, false);
        return new AccountViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final AccountViewHolder holder, int position) {
        final SqlBalAccount sqlBalAccount = accountArrayList.get(position);
        try {

            //region Fill Details
            TimeConvert tC = TimeConvert.timeMiliesConvert(Long.parseLong(sqlBalAccount.getTime()));

            holder.tvDateDD.setText("");
            holder.tvDateMY.setText(tC.getMMM_YY());
            holder.tvDay.setText(tC.getWeekDay());
            holder.cardDateLayout.setVisibility(View.GONE);

            int ex = Integer.parseInt(sqlBalAccount.getExpense());
            int in = Integer.parseInt(sqlBalAccount.getIncome());

            holder.tvEx.setText(ex + "/-");
            holder.tvIn.setText(in + "/-");

            holder.tvTotal.setText((ex - in) + "/-");
            //endregion

            String temp = "", str = "", flag = "";
            switch (type) {
                //region Day,Month,Year,Total
                case MyConstants.day:
                    temp = sqlBalAccount.getDate();
                    current = nowTc.getDD_MM_YY();

                    holder.cardDateLayout.setVisibility(View.VISIBLE);
                    holder.tvDateDD.setText(MyConstants.numberOf(tC.getDd()));

                    str = sqlBalAccount.getDate();
                    flag = MyConstants.day;
                    break;

                case MyConstants.month:
                    temp = tC.getMMM() + "\n" + tC.getYy();
                    current = nowTc.getMMM() + "\n" + nowTc.getYy();

                    holder.tvDateDD.setText(temp);
                    holder.tvDateDD.setTextSize(30);

                    str = sqlBalAccount.getMonth();
                    flag = MyConstants.month;
                    break;

                case MyConstants.year:
                    temp = tC.getYy() + "";
                    current = nowTc.getYy() + "";
                    holder.tvDateDD.setText(temp);

                    str = sqlBalAccount.getYear();
                    flag = MyConstants.year;
                    break;

                case MyConstants.Total:
//                    temp = sqlBalAccount.getDate();

                    final String custName = SqlCustomer.mobileToName(activity, sqlBalAccount.getCustName());
                    holder.tvDateDD.setText(custName);
                    if (custName.length() > 6)
                        holder.tvDateDD.setTextSize(20);

                    str = sqlBalAccount.getCustName();
                    flag = MyConstants.Total;

                    break;
                //endregion
            }

            final String finalStr = str;
            final String finalFlag = flag;
            holder.cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetCalanderData.alertExView(activity, finalStr, finalFlag);
                }
            });

            if (temp.equalsIgnoreCase(current)) {
                holder.tvDateDD.setTextColor(activity.getResources().getColor(R.color.current));
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(activity, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        TextView tvIn, tvEx, tvTotal, tvDateDD, tvDateMY, tvDay;
        LinearLayout cardLayout, cardDateLayout;

        AccountViewHolder(View itemView) {
            super(itemView);
            cardLayout = (LinearLayout) itemView.findViewById(R.id.cardLayout);
            cardDateLayout = (LinearLayout) itemView.findViewById(R.id.cardDateLayput);
            tvIn = (TextView) itemView.findViewById(R.id.tvInBlue);
            tvEx = (TextView) itemView.findViewById(R.id.tvExRed);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotalBlack);
            tvDateDD = (TextView) itemView.findViewById(R.id.tvDD);
            tvDateMY = (TextView) itemView.findViewById(R.id.tvMMMYY);
            tvDay = (TextView) itemView.findViewById(R.id.tvWeekDay);
            TextView tvTime = (TextView) itemView.findViewById(R.id.tvHHMMAM);
            tvTime.setVisibility(View.GONE);
        }
    }

}