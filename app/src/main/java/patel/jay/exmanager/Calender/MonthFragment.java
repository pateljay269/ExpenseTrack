package patel.jay.exmanager.Calender;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.CalAdapter;
import patel.jay.exmanager.SQL.Calender;

import static patel.jay.exmanager.Calender.CalenderActivity.isRepo;
import static patel.jay.exmanager.SQL.SQL.COL_MONTH;
import static patel.jay.exmanager.Utility.MyConst.accName;
import static patel.jay.exmanager.Utility.MyConst.month;
import static patel.jay.exmanager.Utility.MyConst.setExView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment {

    View view;

    public static void setRv(Activity context, RecyclerView rvCat) {
        RecyclerView.Adapter adapter = null;
        ArrayList list = null;
        String type = month;

        Calender calender = new Calender(context);
        if (isRepo) {
            list = calender.totalWise(accName, COL_MONTH);
        } else {
            list = calender.monthWise(accName);
        }
        adapter = new CalAdapter(list, context, type);

        rvCat.scrollToPosition(list.size() - 1);
        rvCat.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = setExView(getActivity(), inflater, container, month);
        return view;
    }
}
