package patel.jay.expensetrack.ExpenseManager.Adapters;

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
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.ExpenseManager.AllTransaction.SetExTransLayout;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;


/**
 * Created by Jay on 27-07-2017.
 */

public class ExTransAdapter extends RecyclerView.Adapter<ExTransAdapter.AccountViewHolder> {

    private ArrayList<SqlExTrans> accountArrayList;
    private Activity activity;
    private boolean dscDisplay;

    public ExTransAdapter(ArrayList<SqlExTrans> accountArrayList, Activity activity) {
        this.accountArrayList = accountArrayList;
        this.activity = activity;
        this.dscDisplay = false;
    }

    public ExTransAdapter(ArrayList<SqlExTrans> accountArrayList, Activity activity, boolean dscDisplay) {
        this(accountArrayList, activity);
        this.dscDisplay = dscDisplay;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview, parent, false);
        return new AccountViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        final SqlExTrans sqlExTrans = accountArrayList.get(position);

        //region Fill Details

        if (sqlExTrans.getEx_rs().length() > 6 || sqlExTrans.getIn_rs().length() > 6) {
            holder.tvRs.setTextSize(18);
        }
        if (sqlExTrans.getCaType().equals(SQLiteHelper.CAT_EX)) {
            holder.tvRs.setText(sqlExTrans.getEx_rs() + "/-");
        } else {
            holder.tvRs.setText(sqlExTrans.getIn_rs() + "/-");
        }

        TimeConvert tc = TimeConvert.timeMiliesConvert(Long.parseLong(sqlExTrans.getTime()));
        holder.tvDateDD.setText(MyConstants.numberOf(tc.getDd()));
        holder.tvDateMY.setText(tc.getMMM_YY());
        holder.tvTime.setText(tc.getHH_Min_AP());
        holder.tvDay.setText(tc.getWeekDay());
        holder.tvDsc.setText(SqlExAccount.accString(activity, sqlExTrans.getAcid()));
        holder.tvCat.setText(SqlExCategory.categoryString(activity, sqlExTrans.getCid()));
        if (dscDisplay) {
            holder.tvExtra.setText(sqlExTrans.getDescription());
            holder.linearMain.setBackground(activity.getResources().getDrawable(R.drawable.border));
        } else {
            holder.tvExtra.setVisibility(View.GONE);
        }
        //endregion

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetExTransLayout setExTransLayout = new SetExTransLayout(activity);
                setExTransLayout.alertEditView(sqlExTrans);
            }
        };

        holder.cardLayout.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        TextView tvRs, tvDateDD, tvDateMY, tvDay, tvDsc, tvTime, tvCat;
        TextView tvExtra;
        LinearLayout cardLayout, linearMain;

        AccountViewHolder(View itemView) {
            super(itemView);
            cardLayout = (LinearLayout) itemView.findViewById(R.id.cardLayout);
            linearMain = (LinearLayout) itemView.findViewById(R.id.linearMain);
            tvRs = (TextView) itemView.findViewById(R.id.tvRs);
            tvDateDD = (TextView) itemView.findViewById(R.id.tvDateDD);
            tvDateMY = (TextView) itemView.findViewById(R.id.tvDateMY);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvCat = (TextView) itemView.findViewById(R.id.tvCat);
            tvDsc = (TextView) itemView.findViewById(R.id.tvDsc);
            tvExtra = (TextView) itemView.findViewById(R.id.tvExtra);
        }
    }
}