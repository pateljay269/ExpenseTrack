package patel.jay.expensetrack.ExpenseManager.Find;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ExpenseManager.Adapters.ExTransAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Adapters.BalRechargeAdapter;
import patel.jay.expensetrack.RechargeManager.Adapters.BalTransAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

/**
 * Created by Jay on 06-Dec-17.
 */

public class SetCalanderData {

    public static void alertExView(Activity activity, String data, String flag) {
        try {
            final Dialog dialog = new Dialog(activity, R.style.AppTheme);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setContentView(R.layout.floating_rv_layout);
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.trans)));

            dialog.show();

            RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

            FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fab);
            fab.setImageResource(R.drawable.logout);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            switch (MyConstants.DASH_CLICK) {
                case MyConstants.BAL:
                    ArrayList<SqlBalAccount> balAccounts = SqlBalAccount.calenderData(activity, data, flag);
                    BalTransAdapter balAdapter = new BalTransAdapter(balAccounts, activity);
                    recyclerView.setAdapter(balAdapter);
                    break;

                case MyConstants.RECHARGE:
                    ArrayList<SqlRecharge> recharges = SqlRecharge.calenderData(activity, data, flag);
                    BalRechargeAdapter rechargeAdapter = new BalRechargeAdapter(recharges, activity);
                    recyclerView.setAdapter(rechargeAdapter);
                    break;

                case MyConstants.EXPENSE:
                    ArrayList<SqlExTrans> sqlExTrans = SqlExTrans.calenderData(activity, data, flag);
                    ExTransAdapter transAdapter = new ExTransAdapter(sqlExTrans, activity);
                    recyclerView.setAdapter(transAdapter);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(activity, e.getMessage());
        }
    }
}