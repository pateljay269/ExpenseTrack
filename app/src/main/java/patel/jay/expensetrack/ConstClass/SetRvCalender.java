package patel.jay.expensetrack.ConstClass;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.expensetrack.Calender.DayFragment;
import patel.jay.expensetrack.Calender.MonthFragment;
import patel.jay.expensetrack.Calender.WeekFragment;
import patel.jay.expensetrack.Calender.YearFragment;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;

/**
 * Created by Jay on 06-08-2017.
 */

public class SetRvCalender {

    public static View setExView(Activity context, LayoutInflater inflater, ViewGroup container, String sCalender) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        try {
            RecyclerView rvCat = (RecyclerView) view.findViewById(R.id.recyclerView);
//            rvCat.setLayoutManager(new LinearLayoutManager(context));
            rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            switch (sCalender) {
                case MyConstants.day:
                    DayFragment.setRv(context, rvCat);
                    break;

                case MyConstants.week:
                    WeekFragment.setRv(context, rvCat);
                    break;

                case MyConstants.month:
                    MonthFragment.setRv(context, rvCat);
                    break;

                case MyConstants.year:
                    YearFragment.setRv(context, rvCat);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public static ArrayList<SqlExTrans> addTextWatcherExTrans(String cName, ArrayList<SqlExTrans> accountArrayList) {

        try {
            ArrayList<SqlExTrans> tempArrayList = accountArrayList;
            if (cName.isEmpty()) {
                accountArrayList = tempArrayList;
            } else {
                //region Search
                accountArrayList = new ArrayList<>();
                for (SqlExTrans sqlAccount : tempArrayList) {
                    if (sqlAccount.getDescription().contains(cName) ||
                            sqlAccount.getEx_rs().contains(cName) ||
                            sqlAccount.getIn_rs().contains(cName)) {
                        accountArrayList.add(sqlAccount);
                    }
                }
                //endregion
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountArrayList;
    }

    public static ArrayList<SqlExCategory> addTextWatcherExCat(String cName, ArrayList<SqlExCategory> accountArrayList) {

        try {
            ArrayList<SqlExCategory> tempArrayList = accountArrayList;
            if (cName.isEmpty()) {
                accountArrayList = tempArrayList;
            } else {
                //region Search
                accountArrayList = new ArrayList<>();
                for (SqlExCategory sqlExCategory : tempArrayList) {
                    if (sqlExCategory.getCatName().contains(cName)) {
                        accountArrayList.add(sqlExCategory);
                    }
                }
                //endregion
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountArrayList;
    }

    public static ArrayList<SqlBalAccount> addTextWatcherBal(String cName, ArrayList<SqlBalAccount> accountArrayList) {
        try {
            ArrayList<SqlBalAccount> tempArrayList = accountArrayList;
            if (cName.isEmpty()) {
                accountArrayList = tempArrayList;
            } else {
                //region Search
                accountArrayList = new ArrayList<>();
                for (SqlBalAccount sqlBalAccount : tempArrayList) {
                    if (sqlBalAccount.getCustName().contains(cName) ||
                            sqlBalAccount.getDsc().contains(cName) ||
                            sqlBalAccount.getIncome().contains(cName) ||
                            sqlBalAccount.getExpense().contains(cName)) {
                        accountArrayList.add(sqlBalAccount);
                    }
                }
                //endregion
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountArrayList;
    }
}