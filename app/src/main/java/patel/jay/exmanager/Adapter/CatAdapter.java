package patel.jay.exmanager.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.ViewHolder.CatVH;
import patel.jay.exmanager.Expense.Category.CatDialog;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Category;


/**
 * Created by Jay on 24-07-2017.
 */

public class CatAdapter extends RecyclerView.Adapter<CatVH> {

    private ArrayList<Category> categoryArrayList;
    private Activity context;

    public CatAdapter(ArrayList<Category> categoryArrayList, Activity context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new CatVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CatVH holder, int position) {
        final Category category = categoryArrayList.get(position);
        holder.tvCat.setText(category.getCatName());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CatDialog catDialog = new CatDialog(context);
                catDialog.alertEditCat(category);
            }
        };

        holder.tvCat.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

}
