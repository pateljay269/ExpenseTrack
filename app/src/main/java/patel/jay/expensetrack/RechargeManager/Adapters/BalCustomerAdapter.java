package patel.jay.expensetrack.RechargeManager.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Customer.SetCustLayout;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;

/**
 * Created by Jay on 09-08-2017.
 */

public class BalCustomerAdapter extends RecyclerView.Adapter<BalCustomerAdapter.CustomerViewHolder> {

    private ArrayList<SqlCustomer> customerArrayList;
    private Activity activity;

    public BalCustomerAdapter(ArrayList<SqlCustomer> customerArrayList, Activity activity) {
        this.customerArrayList = customerArrayList;
        this.activity = activity;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomerViewHolder holder, int position) {
        final SqlCustomer sqlCustomer = customerArrayList.get(position);

        holder.tvMobile.setText(sqlCustomer.getCustMobile());
        holder.tvName.setText(sqlCustomer.getCustName());
        holder.tvDsc.setText(sqlCustomer.getCustDsc());

        TimeConvert tc = TimeConvert.timeMiliesConvert(Long.parseLong(sqlCustomer.getCustRegDate()));
        holder.tvTime.setText(tc.getDateTime());
        holder.tvTime.setVisibility(View.GONE);
        holder.linearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetCustLayout layout = new SetCustLayout(activity);
                layout.alertEditCat(sqlCustomer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerArrayList.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobile, tvTime, tvDsc;
        LinearLayout linearInfo;

        CustomerViewHolder(View itemView) {
            super(itemView);

            linearInfo = (LinearLayout) itemView.findViewById(R.id.cardLayout);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvMobile = (TextView) itemView.findViewById(R.id.tvMobile);
            tvDsc = (TextView) itemView.findViewById(R.id.tvDsc);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
