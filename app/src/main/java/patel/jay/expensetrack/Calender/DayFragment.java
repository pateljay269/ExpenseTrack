package patel.jay.expensetrack.Calender;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SetRvCalender;
import patel.jay.expensetrack.ExpenseManager.Adapters.CalenderExAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCalender;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.RechargeManager.Adapters.CalenderBalAdapter;
import patel.jay.expensetrack.RechargeManager.Adapters.CalenderRecAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCalenderBal;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCalenderRec;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment {

    View view;

    public DayFragment() {
        // Required empty public constructor
    }

    public static void setRv(Activity context, RecyclerView rvCat) {
        if (MyConstants.DASH_CLICK.equalsIgnoreCase(MyConstants.BAL)) {
            ArrayList<SqlBalAccount> accountArrayList = SqlCalenderBal.dayWise(context);
            CalenderBalAdapter balAdapter = new CalenderBalAdapter(accountArrayList, context, MyConstants.day);
            rvCat.setAdapter(balAdapter);
            rvCat.scrollToPosition(accountArrayList.size() - 1);
        } else if (MyConstants.DASH_CLICK.equalsIgnoreCase(MyConstants.RECHARGE)) {
            ArrayList<SqlRecharge> rechargeArrayList = SqlCalenderRec.dayWise(context);
            CalenderRecAdapter recAdapter = new CalenderRecAdapter(rechargeArrayList, context, MyConstants.day);
            rvCat.setAdapter(recAdapter);
            rvCat.scrollToPosition(rechargeArrayList.size() - 1);
        } else if (MyConstants.DASH_CLICK.equalsIgnoreCase(MyConstants.EXPENSE)) {
            ArrayList<SqlExTrans> accountArrayList = SqlExCalender.dayWise(context, MyConstants.accName);
            CalenderExAdapter adapter = new CalenderExAdapter(accountArrayList, context, MyConstants.day);
            rvCat.setAdapter(adapter);
            rvCat.scrollToPosition(accountArrayList.size() - 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = SetRvCalender.setExView(getActivity(), inflater, container, MyConstants.day);
        return view;
    }
}
