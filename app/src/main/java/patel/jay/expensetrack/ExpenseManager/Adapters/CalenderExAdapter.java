package patel.jay.expensetrack.ExpenseManager.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.ExpenseManager.Find.SetCalanderData;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

/**
 * Created by Jay on 31-07-2017.
 */

public class CalenderExAdapter extends RecyclerView.Adapter<CalenderExAdapter.AccountViewHolder> {

    private final TimeConvert nowTc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
    private String current;

    private ArrayList<SqlExTrans> accountArrayList;
    private Activity activity;
    private String type;

    public CalenderExAdapter(ArrayList<SqlExTrans> accountArrayList, Activity activity, String type) {
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
        final SqlExTrans sqlAccount = accountArrayList.get(position);
        try {

            //region Fill Details
            TimeConvert tc = TimeConvert.timeMiliesConvert(Long.parseLong(sqlAccount.getTime()));

            holder.tvDateDD.setText("");
            holder.tvEx.setTextColor(Color.RED);
            holder.tvIn.setTextColor(Color.BLUE);
            holder.tvTotal.setTextColor(Color.BLACK);

            holder.tvDateMY.setText(tc.getMMM_YY());
            holder.tvDay.setText(tc.getWeekDay());
            holder.cardDateLayout.setVisibility(View.GONE);

            int ex = 0, in = 0;
            ex = Integer.parseInt(sqlAccount.getEx_rs());
            in = Integer.parseInt(sqlAccount.getIn_rs());

            holder.tvEx.setText(ex + "/-");
            holder.tvIn.setText(in + "/-");

            holder.tvTotal.setText((in - ex) + "/-");
            //endregion

            String temp = "", str = "", flag = "";
            switch (type) {
                //region Day,Month,Week,Year
                case MyConstants.day:
                    current = nowTc.getDD_MM_YY();
                    temp = sqlAccount.getDate();

                    holder.cardDateLayout.setVisibility(View.VISIBLE);
                    holder.tvDateDD.setText(MyConstants.numberOf(tc.getDd()));

                    str = sqlAccount.getDate();
                    flag = MyConstants.day;
                    break;

                case MyConstants.week:
                    temp = "(" + tc.getWeek() + ") " + tc.getMMM_YY();
                    current = "(" + nowTc.getWeek() + ") " + nowTc.getMMM_YY();
                    holder.tvDateDD.setTextSize(20);
                    holder.tvDateDD.setText(Html.fromHtml("(" + tc.getWeek() + "<sup><small>"
                            + MyConstants.weekSuper(tc.getWeek()) + "</small></sup>)")
                            + "\n" + tc.getMMM() + "\n" + tc.getYy());

                    str = sqlAccount.getWeekNumber();
                    flag = MyConstants.week;
                    break;

                case MyConstants.month:
                    temp = tc.getMMM() + "\n" + tc.getYy();
                    current = nowTc.getMMM() + "\n" + nowTc.getYy();

                    holder.tvDateDD.setText(temp);
                    holder.tvDateDD.setTextSize(20);

                    flag = MyConstants.month;
                    str = sqlAccount.getMonth();
                    break;

                case MyConstants.year:
                    temp = tc.getYy() + "";
                    current = nowTc.getYy() + "";
                    holder.tvDateDD.setText(temp);

                    str = sqlAccount.getYear();
                    flag = MyConstants.year;
                    break;
                //endregion
            }

            if (temp.equalsIgnoreCase(current)) {
                holder.tvDateDD.setTextColor(activity.getResources().getColor(R.color.current));
            }

            final String finalStr = str;

            final String finalFlag = flag;
            holder.cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetCalanderData.alertExView(activity, finalStr, finalFlag);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        TextView tvEx, tvIn, tvTotal, tvDateDD, tvDateMY, tvDay;
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