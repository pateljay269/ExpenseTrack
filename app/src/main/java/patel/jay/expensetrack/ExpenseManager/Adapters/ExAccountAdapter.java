package patel.jay.expensetrack.ExpenseManager.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ExpenseManager.Category.SetExCatLayout;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.R;

/**
 * Created by Jay on 8/27/2017.
 */

public class ExAccountAdapter extends RecyclerView.Adapter<ExAccountAdapter.AccViewHolder> {

    private ArrayList<SqlExAccount> accountArrayList;
    private Activity activity;

    public ExAccountAdapter(ArrayList<SqlExAccount> accountArrayList, Activity activity) {
        this.accountArrayList = accountArrayList;
        this.activity = activity;
    }

    @Override
    public AccViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new AccViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccViewHolder holder, int position) {
        final SqlExAccount sqlExAccount = accountArrayList.get(position);
        holder.tvCat.setText(sqlExAccount.getAccName());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetExCatLayout setExCatLayout = new SetExCatLayout(activity);
                setExCatLayout.alertEditAcc(sqlExAccount);
//                MyConstants.toast(context,sqlCategory.getCaType());
            }
        };

        holder.tvCat.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    class AccViewHolder extends RecyclerView.ViewHolder {

        TextView tvCat;

        AccViewHolder(View itemView) {
            super(itemView);
            tvCat = (TextView) itemView.findViewById(R.id.tvCat);
        }
    }
}
