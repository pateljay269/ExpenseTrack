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
import java.util.Calendar;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.ExpenseManager.Find.SetCalanderData;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

/**
 * Created by Jay on 8/20/2017.
 */

public class CalenderRecAdapter extends RecyclerView.Adapter<CalenderRecAdapter.RechargeViewHolder> {

    private static Calendar cal = Calendar.getInstance();
    private final static int MM = cal.get(Calendar.MONTH);
    private final static String currentMM = MyConstants.numberOf(MM + 1);
    private final static int cDay = cal.get(Calendar.DAY_OF_MONTH);
    private final static String currentDD = MyConstants.numberOf(cDay);
    private final static int currentYY = cal.get(Calendar.YEAR);
    private String current;

    private ArrayList<SqlRecharge> rechargeArrayList;
    private Activity activity;
    private String type;

    public CalenderRecAdapter(ArrayList<SqlRecharge> rechargeArrayList, Activity context, String type) {
        this.rechargeArrayList = rechargeArrayList;
        this.activity = context;
        this.type = type;
    }

    @Override
    public RechargeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trans, parent, false);
        return new RechargeViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RechargeViewHolder holder, int position) {
        final SqlRecharge sqlRecharge = rechargeArrayList.get(position);

        try {
            //region Fill Details
            TimeConvert tc = TimeConvert.timeMiliesConvert(Long.parseLong(sqlRecharge.getTime()));

            int rec = Integer.parseInt(sqlRecharge.getRecharge());
            int ince = Integer.parseInt(sqlRecharge.getIncentive());

            holder.tvRecharge.setText(rec + "/-");
            holder.tvIncentive.setText(ince + "/-");
            holder.tvTotal.setText((rec + ince) + "/-");

            holder.cardDateLayout.setVisibility(View.GONE);

            holder.tvDateDD.setText(MyConstants.numberOf(tc.getDd()));
            holder.tvDateMY.setText(tc.getMMM_YY());
            holder.tvDay.setText(tc.getWeekDay());

            String temp = "", str = "", flag = "";
            switch (type) {
                //region Day,Month,Year
                case MyConstants.day:
                    temp = sqlRecharge.getDate();
                    current = currentDD + "-" + currentMM + "-" + currentYY;
                    holder.cardDateLayout.setVisibility(View.VISIBLE);
                    holder.tvDateDD.setText(MyConstants.numberOf(tc.getDd()));
                    if ((rec + "").length() > 6 || (ince + "").length() > 6) {
                        int size = 18;
                        holder.tvRecharge.setTextSize(size);
                        holder.tvIncentive.setTextSize(size);
                        holder.tvTotal.setTextSize(size);
                    }

                    str = sqlRecharge.getDate();
                    flag = MyConstants.day;
                    break;

                case MyConstants.month:
                    temp = tc.getMMM() + "\n" + tc.getYy();
                    current = MyConstants.monthName(MM) + "\n" + currentYY;
                    holder.tvDateDD.setText(temp);
                    holder.tvDateDD.setTextSize(25);

                    str = sqlRecharge.getMonth();
                    flag = MyConstants.month;
                    break;

                case MyConstants.year:
                    temp = tc.getYy() + "";
                    current = currentYY + "";

                    holder.tvDateDD.setText(temp);

                    str = sqlRecharge.getYear();
                    flag = MyConstants.year;
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

            //endregion
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rechargeArrayList.size();
    }

    class RechargeViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecharge, tvIncentive, tvDateDD, tvDateMY, tvDay, tvTotal;
        LinearLayout cardLayout, cardDateLayout;

        RechargeViewHolder(View itemView) {
            super(itemView);
            cardLayout = (LinearLayout) itemView.findViewById(R.id.cardLayout);
            cardDateLayout = (LinearLayout) itemView.findViewById(R.id.cardDateLayput);
            tvRecharge = (TextView) itemView.findViewById(R.id.tvExRed);
            tvIncentive = (TextView) itemView.findViewById(R.id.tvInBlue);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotalBlack);
            tvDateDD = (TextView) itemView.findViewById(R.id.tvDD);
            tvDateMY = (TextView) itemView.findViewById(R.id.tvMMMYY);
            tvDay = (TextView) itemView.findViewById(R.id.tvWeekDay);
            TextView tvTime = (TextView) itemView.findViewById(R.id.tvHHMMAM);
            tvTime.setVisibility(View.GONE);
        }
    }
}
