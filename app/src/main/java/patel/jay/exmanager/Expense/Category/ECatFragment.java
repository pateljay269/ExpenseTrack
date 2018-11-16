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

import patel.jay.exmanager.Adapter.CatAdapter;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Category;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ECatFragment extends Fragment {

    private static RecyclerView rvCat;
    private static ArrayList<Category> catList;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calender, container, false);

        rvCat = view.findViewById(R.id.recyclerView);

        MaterialEditText etFind = view.findViewById(R.id.etFind);
        etFind.setVisibility(View.VISIBLE);
        etFind.setHint(R.string.cat_lable);
        etFind.setFloatingLabelText(getResources().getString(R.string.cat_lable));

        setRv(getActivity());

        etFind.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
                CatAdapter adapter = new CatAdapter(addTextExCat(s, catList), getActivity());
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

    public static void setRv(Activity context) {
        rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        catList = new Category().allCat(context);
        CatAdapter adapter = new CatAdapter(catList, context);
        rvCat.setAdapter(adapter);
    }

    public ArrayList<Category> addTextExCat(CharSequence s, ArrayList<Category> accList) {

        try {
            ArrayList<Category> tempArrayList = accList;
            String cName = s + "";
            if (cName.isEmpty()) {
                accList = tempArrayList;
            } else {
                accList = new ArrayList<>();
                for (Category category : tempArrayList) {
                    if (category.getCatName().contains(cName)) {
                        accList.add(category);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accList;
    }
}
