package patel.jay.expensetrack.ExpenseManager.Category;


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

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.SetRvCalender;
import patel.jay.expensetrack.ExpenseManager.Adapters.CategoryExAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ECatFragment extends Fragment {

    static RecyclerView rvCat;
    static CategoryExAdapter adapter;
    static ArrayList<SqlExCategory> categoryArrayList;
    View view;
    MaterialEditText etFind;

    public ECatFragment() {
        // Required empty public constructor
    }

    public static void setRv(Activity context) {
        categoryArrayList = SqlExCategory.allCatType(context, SQLiteHelper.CAT_EX);
        adapter = new CategoryExAdapter(categoryArrayList, context);
        rvCat.setAdapter(adapter);
        rvCat.scrollToPosition(categoryArrayList.size() - 1);
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
            etFind.setHint(R.string.cat_lable);
            etFind.setFloatingLabelText(getResources().getString(R.string.cat_lable));

            setRv(getActivity());

            //region TextWatcher
            etFind.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //region TextChangedArrayList
                    try {
                        String cName = etFind.getText().toString();

                        adapter = new CategoryExAdapter(SetRvCalender.addTextWatcherExCat(cName, categoryArrayList), getActivity());
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
