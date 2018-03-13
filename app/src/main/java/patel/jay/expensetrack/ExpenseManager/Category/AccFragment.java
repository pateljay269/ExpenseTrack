package patel.jay.expensetrack.ExpenseManager.Category;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import patel.jay.expensetrack.ExpenseManager.Adapters.ExAccountAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccFragment extends Fragment {

    static RecyclerView rvCat;
    static ExAccountAdapter adapter;
    static ArrayList<SqlExAccount> accountArrayList;
    View view;
    MaterialEditText etFind;

    public AccFragment() {
        // Required empty public constructor
    }

    public static void setRv(Activity context) {
        accountArrayList = SqlExAccount.allAcc(context);
        adapter = new ExAccountAdapter(accountArrayList, context);
        rvCat.setAdapter(adapter);
        rvCat.scrollToPosition(accountArrayList.size() - 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);
        try {
            rvCat = (RecyclerView) view.findViewById(R.id.recyclerView);
//            rvCat.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            etFind = (MaterialEditText) view.findViewById(R.id.etFind);
            etFind.setVisibility(View.VISIBLE);
            etFind.setHint("Account Name");
            etFind.setFloatingLabelText("Account Name");

            setRv(getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
