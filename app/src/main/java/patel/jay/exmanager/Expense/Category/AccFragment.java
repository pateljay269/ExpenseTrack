package patel.jay.exmanager.Expense.Category;


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

import patel.jay.exmanager.Adapter.AccAdapter;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Account;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccFragment extends Fragment {

    static RecyclerView rvCat;
    static ArrayList<Account> accList, tempList;

    View view;

    public static void setRv(Activity context) {
        accList = new Account().allAcc(context);
        tempList = accList;
        AccAdapter adapter = new AccAdapter(accList, context);
        rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        rvCat.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);

        rvCat = view.findViewById(R.id.recyclerView);

        MaterialEditText etFind = view.findViewById(R.id.etFind);
        etFind.setVisibility(View.VISIBLE);
        etFind.setHint("Account Name");
        etFind.setFloatingLabelText("Account Name");

        setRv(getActivity());

        etFind.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cName = s + "";
                if (cName.isEmpty()) {
                    accList = tempList;
                } else {
                    accList = new ArrayList<>();
                    for (Account sqlExCategory : tempList) {
                        if (sqlExCategory.getAccName().contains(cName)) {
                            accList.add(sqlExCategory);
                        }
                    }
                }

                AccAdapter adapter = new AccAdapter(accList, getActivity());
                rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
                rvCat.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

        return view;
    }
}
