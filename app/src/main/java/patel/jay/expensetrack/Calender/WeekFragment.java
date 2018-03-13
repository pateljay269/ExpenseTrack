package patel.jay.expensetrack.Calender;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SetRvCalender;
import patel.jay.expensetrack.ExpenseManager.Adapters.CalenderExAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCalender;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeekFragment extends Fragment {

    View view;

    public WeekFragment() {
        // Required empty public constructor
    }

    public static void setRv(Activity context, RecyclerView rvCat) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvCat.setLayoutManager(layoutManager);
        ArrayList<SqlExTrans> accountArrayList = SqlExCalender.weekWise(context, MyConstants.accName);
        CalenderExAdapter adapter = new CalenderExAdapter(accountArrayList, context, MyConstants.week);
        rvCat.setAdapter(adapter);
        rvCat.scrollToPosition(accountArrayList.size() - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = SetRvCalender.setExView(getActivity(), inflater, container, MyConstants.week);
        return view;
    }
}
