package patel.jay.expensetrack.ExpenseManager.AllTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import patel.jay.expensetrack.ExpenseManager.Adapters.ExTransAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExInFragment extends Fragment {

    static RecyclerView rvCat;
    static ExTransAdapter adapter;
    static ArrayList<SqlExTrans> accountArrayList;
    View view;
    EditText etFind;

    public ExInFragment() {
        // Required empty public constructor
    }

    public static void setArrayList(Activity context, int year, String month, boolean order) {
        try {
            String caType = SQLiteHelper.CAT_IN;

            if (!month.equals("") && year != 0) {
                accountArrayList = SqlExTrans.allDetails(context, caType, year, month + " " + year, order, false);
            } else if (month.equals("") && year != 0) {
                accountArrayList = SqlExTrans.allDetails(context, caType, year, order, false);
            } else {
                accountArrayList = SqlExTrans.allDetails(context, caType, order, false);
            }
            setRv(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRv(Activity context) {
        try {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rvCat.setLayoutManager(layoutManager);
            adapter = new ExTransAdapter(accountArrayList, context);
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
        try {
            etFind = (EditText) view.findViewById(R.id.etFind);
            etFind.setVisibility(View.VISIBLE);
            rvCat = (RecyclerView) view.findViewById(R.id.recyclerView);
//            rvCat.setLayoutManager(new LinearLayoutManager(getActivity()));

            setArrayList(getActivity(), 0, "", false);

            //region TextWatcher
            etFind.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //region TextChangedArrayList
                    try {
                        String cName = etFind.getText().toString();
                        rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        adapter = new ExTransAdapter(SetRvCalender.addTextWatcherExTrans(cName, accountArrayList), getActivity());
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}

