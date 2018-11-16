package patel.jay.exmanager.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.ViewHolder.TransVH;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Repo;

import static android.graphics.Color.BLACK;

/**
 * Created by Jay on 31-07-2017.
 */

public class RepoAdapter extends RecyclerView.Adapter<TransVH> {

    private ArrayList<Repo> accountArrayList;

    public RepoAdapter(ArrayList<Repo> accountArrayList) {
        this.accountArrayList = accountArrayList;
    }

    @NonNull
    @Override
    public TransVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trans, parent, false);
        return new TransVH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final TransVH holder, int position) {
        final Repo repo = accountArrayList.get(position);
        try {
            holder.tvDateDD.setVisibility(View.GONE);
            holder.tvTime.setVisibility(View.INVISIBLE);
            holder.tvDateMY.setVisibility(View.INVISIBLE);
            holder.tvIn.setVisibility(View.GONE);
            holder.tvEx.setVisibility(View.GONE);
            holder.tvDay.setBackgroundResource(0);
            holder.tvDay.setTextColor(BLACK);
            holder.tvDay.setTextSize(20);

            int total = repo.getTotal();
            String amount = total > 0 ? total + "/-" : "Data Not Found";

            holder.tvTotal.setText(amount);
            holder.tvDay.setText("\t" + repo.getTitle());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

}