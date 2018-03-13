package patel.jay.expensetrack.RechargeManager.Transactions;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.SetRvCalender;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Adapters.BalTransAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalExFragment extends Fragment {

    static RecyclerView rvCat;
    static EditText etFind;
    static BalTransAdapter adapter;
    static ArrayList<SqlBalAccount> accountArrayList;
    View view;

    public BalExFragment() {
        // Required empty public constructor
    }

    public static void setRv(Activity context) {
        try {
            rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            accountArrayList = SqlBalAccount.allDetails(context, SQLiteHelper.COL_INCOME);
            adapter = new BalTransAdapter(accountArrayList, context);
            rvCat.setAdapter(adapter);
            rvCat.scrollToPosition(accountArrayList.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);
        rvCat = (RecyclerView) view.findViewById(R.id.recyclerView);
        rvCat.setLayoutManager(new LinearLayoutManager(getActivity()));
        etFind = (EditText) view.findViewById(R.id.etFind);
        etFind.setVisibility(View.VISIBLE);

        //region TextWatcher
        etFind.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //region TextChangedArrayList
                try {
                    String cName = etFind.getText().toString();
                    rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    adapter = new BalTransAdapter(SetRvCalender.addTextWatcherBal(cName, accountArrayList), getActivity());
                    rvCat.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //endregion
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
        });
        //endregion

        setRv(getActivity());
        return view;
    }
}
