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
import patel.jay.expensetrack.RechargeManager.Recharge.SetRecLayout;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

/**
 * Created by Jay on 8/16/2017.
 */

public class BalRechargeAdapter extends RecyclerView.Adapter<BalRechargeAdapter.RechargeViewHolder> {

    private ArrayList<SqlRecharge> rechargeArrayList;
    private Activity activity;

    public BalRechargeAdapter(ArrayList<SqlRecharge> rechargeArrayList, Activity activity) {
        this.rechargeArrayList = rechargeArrayList;
        this.activity = activity;
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
            holder.tvTotal.setVisibility(View.GONE);

            TimeConvert tC = TimeConvert.timeMiliesConvert(Long.parseLong(sqlRecharge.getTime()));
            int dd = tC.getDd();

            if (sqlRecharge.getRecharge().equals("0")) {
                holder.tvIncentive.setText(sqlRecharge.getIncentive() + "/-");
                holder.tvRecharge.setVisibility(View.GONE);
            } else if (sqlRecharge.getIncentive().equals("0")) {
                holder.tvRecharge.setText(sqlRecharge.getRecharge() + "/-");
                holder.tvIncentive.setVisibility(View.GONE);
            }

            holder.tvTime.setText(tC.getHH_Min_AP());
            holder.tvDD.setText(MyConstants.numberOf(dd));
            holder.tvDateMY.setText(tC.getMMM_YY());
            holder.tvDay.setText(tC.getWeekDay());

            holder.cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetRecLayout layout = new SetRecLayout(activity);
                    layout.alertEditCat(sqlRecharge);
                }
            });
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

        TextView tvRecharge, tvIncentive, tvDD, tvDateMY, tvDay, tvTime, tvTotal;
        LinearLayout cardLayout;

        private RechargeViewHolder(View itemView) {
            super(itemView);

            cardLayout = (LinearLayout) itemView.findViewById(R.id.cardLayout);
            tvRecharge = (TextView) itemView.findViewById(R.id.tvExRed);
            tvIncentive = (TextView) itemView.findViewById(R.id.tvInBlue);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotalBlack);
            tvTime = (TextView) itemView.findViewById(R.id.tvHHMMAM);
            tvDD = (TextView) itemView.findViewById(R.id.tvDD);
            tvDateMY = (TextView) itemView.findViewById(R.id.tvMMMYY);
            tvDay = (TextView) itemView.findViewById(R.id.tvWeekDay);
        }
    }
}
