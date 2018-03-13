package patel.jay.expensetrack.ExpenseManager.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ExpenseManager.Category.SetExCatLayout;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.R;


/**
 * Created by Jay on 24-07-2017.
 */

public class CategoryExAdapter extends RecyclerView.Adapter<CategoryExAdapter.CategoryViewHolder> {

    private ArrayList<SqlExCategory> categoryArrayList;
    private Activity context;

    public CategoryExAdapter(ArrayList<SqlExCategory> categoryArrayList, Activity context) {
        this.categoryArrayList = categoryArrayList;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        final SqlExCategory sqlExCategory = categoryArrayList.get(position);
        holder.tvCat.setText(sqlExCategory.getCatName());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetExCatLayout setExCatLayout = new SetExCatLayout(context);
                setExCatLayout.alertEditCat(sqlExCategory);
//                MyConstants.toast(context,sqlExCategory.getCaType());
            }
        };

        holder.tvCat.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCat;

        CategoryViewHolder(View itemView) {
            super(itemView);
            tvCat = (TextView) itemView.findViewById(R.id.tvCat);
        }
    }

}
