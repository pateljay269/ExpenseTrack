package patel.jay.exmanager.Calender;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.CalAdapter;
import patel.jay.exmanager.SQL.Calender;
import patel.jay.exmanager.SQL.Trans;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static patel.jay.exmanager.Utility.MyConst.accName;
import static patel.jay.exmanager.Utility.MyConst.setExView;
import static patel.jay.exmanager.Utility.MyConst.week;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeekFragment extends Fragment {

    View view;

    public static void setRv(Activity context, RecyclerView rvCat) {

        rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        ArrayList<Trans> accountArrayList = new Calender(context).weekWise(accName);
        CalAdapter adapter = new CalAdapter(accountArrayList, context, week);
        rvCat.setAdapter(adapter);
        rvCat.scrollToPosition(accountArrayList.size() - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = setExView(getActivity(), inflater, container, week);
        return view;
    }
}
