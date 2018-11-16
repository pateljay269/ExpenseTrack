package patel.jay.exmanager.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import patel.jay.exmanager.R;

public class CatVH extends RecyclerView.ViewHolder {

    public TextView tvCat;

    public CatVH(View itemView) {
        super(itemView);
        tvCat = itemView.findViewById(R.id.tvCat);
    }
}
