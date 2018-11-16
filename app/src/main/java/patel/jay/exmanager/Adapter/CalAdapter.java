package patel.jay.exmanager.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.ViewHolder.TransVH;
import patel.jay.exmanager.Expense.Find.CalDialog;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Trans;
import patel.jay.exmanager.Utility.TimeConvert;

import static android.graphics.Color.BLACK;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;
import static patel.jay.exmanager.Calender.CalenderActivity.isRepo;
import static patel.jay.exmanager.SQL.SQL.COL_DATE;
import static patel.jay.exmanager.SQL.SQL.COL_MONTH;
import static patel.jay.exmanager.Utility.MyConst.day;
import static patel.jay.exmanager.Utility.MyConst.month;
import static patel.jay.exmanager.Utility.MyConst.week;
import static patel.jay.exmanager.Utility.MyConst.year;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

/**
 * Created by Jay on 31-07-2017.
 */

public class CalAdapter extends RecyclerView.Adapter<TransVH> {

    private final TimeConvert nowTc = timeMilies(currentTimeMillis());

    private ArrayList<Trans> accountArrayList;
    private Activity activity;
    private String type;

    public CalAdapter(ArrayList<Trans> accountArrayList, Activity activity, String type) {
        this.accountArrayList = accountArrayList;
        this.activity = activity;
        this.type = type;
    }

    @NonNull
    @Override
    public TransVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trans, parent, false);
        return new TransVH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final TransVH holder, int position) {
        final Trans sqlAccount = accountArrayList.get(position);
        try {

            //region Fill Details
            TimeConvert tc = timeMilies(parseLong(sqlAccount.getTime()));

            holder.tvDateDD.setText("");
            holder.tvEx.setTextColor(Color.RED);
            holder.tvIn.setTextColor(Color.BLUE);
            holder.tvTotal.setTextColor(BLACK);

            holder.tvDateMY.setText(tc.getMMM_YY());
            holder.tvDay.setText(tc.getWeekDay());

            holder.cardDateLayout.setVisibility(View.GONE);
            holder.tvDay.setVisibility(View.INVISIBLE);
            holder.tvTime.setVisibility(View.INVISIBLE);
            holder.tvDateMY.setVisibility(View.INVISIBLE);
            holder.tvIn.setVisibility(View.GONE);
            holder.tvEx.setVisibility(View.GONE);

            holder.tvTotal.setText(sqlAccount.getEx_rs() + "/-");

            //endregion

            String temp = "", current = "";
            switch (type) {
                case day:
                    current = nowTc.getDD_MM_YY();
                    temp = tc.getDD_MM_YY();

                    if (isRepo) {
                        temp = tc.getDd() + "";
                        current = nowTc.getDd() + "";

                    } else {
                        holder.tvDay.setVisibility(View.VISIBLE);
                        holder.tvTime.setVisibility(View.VISIBLE);
                        holder.tvDateMY.setVisibility(View.VISIBLE);
                        holder.cardDateLayout.setVisibility(View.VISIBLE);
                    }
                    holder.tvDateDD.setText(tc.numberOf(tc.getDd()));

                    break;

                case week:
                    temp = "(" + tc.getWeek() + ") " + tc.getMMM_YY();
                    current = "(" + nowTc.getWeek() + ") " + nowTc.getMMM_YY();

                    holder.tvDateDD.setTextSize(20);
                    holder.tvDateDD.setText("(" + tc.weekSuper(tc.getWeek()) + ")"
                            + "\n" + tc.getMMM());

                    holder.cardDateLayout.setVisibility(View.VISIBLE);
                    holder.tvDay.setBackgroundResource(0);
                    holder.tvDay.setTextColor(BLACK);
                    holder.tvDay.setVisibility(View.VISIBLE);
                    holder.tvDay.setText("\t" + tc.getYy());
                    holder.tvDay.setTextSize(20);
                    break;

                case month:
                    temp = tc.getMMM() + " " + tc.getYy();
                    current = nowTc.getMMM() + " " + nowTc.getYy();

                    if (isRepo) {
                        temp = tc.getMMM();
                        current = nowTc.getMMM();

                        holder.cardDateLayout.setVisibility(View.INVISIBLE);
                        holder.tvDay.setVisibility(View.INVISIBLE);
                    } else {
                        holder.cardDateLayout.setVisibility(View.VISIBLE);
                        holder.tvDay.setBackgroundResource(0);
                        holder.tvDay.setTextColor(BLACK);
                        holder.tvDay.setVisibility(View.VISIBLE);
                        holder.tvDay.setText("\t" + tc.getYy());
                        holder.tvDay.setTextSize(20);

                    }

                    String mon = tc.monthName(tc.getMm());
                    holder.tvDateDD.setText(mon);
                    holder.tvDateDD.setTextSize(20);

                    break;

                case year:
                    temp = tc.getYy() + "";
                    current = nowTc.getYy() + "";

                    holder.tvDateDD.setText(temp);
                    break;
                //endregion
            }

            if (!isRepo && temp.equalsIgnoreCase(current)) {
                holder.tvDateDD.setTextColor(activity.getResources().getColor(R.color.current));
            }

            String dates[] = new String[4];
            dates[0] = sqlAccount.getDate() + "";
            dates[1] = sqlAccount.getMonth() + "";
            dates[2] = sqlAccount.getYear() + "";
            dates[3] = sqlAccount.getWeekNumber() + "";

            String flag = type;
            if (isRepo) {
                switch (type) {
                    case day:
                        flag = COL_DATE;
                        dates[0] = sqlAccount.getDate() + "";
                        break;

                    case month:
                        flag = COL_MONTH;
                        dates[0] = sqlAccount.getMonth() + "";
                        break;
                }
            }

            final String finalDt[] = dates;
            final String finalFlag = flag;

            holder.cardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isRepo) {
                        new CalDialog().alertExView(activity, finalDt[0], finalFlag);
                    } else {
                        new CalDialog().alertExView(activity, finalDt, finalFlag);
                    }
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

}