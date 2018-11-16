package patel.jay.exmanager.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import patel.jay.exmanager.R;

public class TransVH extends RecyclerView.ViewHolder {

    public TextView tvEx, tvIn, tvTotal, tvDateDD, tvDateMY, tvDay, tvTime;
    public LinearLayout cardLayout, cardDateLayout;

    public TransVH(View itemView) {
        super(itemView);
        cardLayout = itemView.findViewById(R.id.cardLayout);
        cardDateLayout = itemView.findViewById(R.id.cardDateLayput);
        tvIn = itemView.findViewById(R.id.tvInBlue);
        tvEx = itemView.findViewById(R.id.tvExRed);
        tvTotal = itemView.findViewById(R.id.tvTotalBlack);
        tvDateDD = itemView.findViewById(R.id.tvDD);
        tvDateMY = itemView.findViewById(R.id.tvMMMYY);
        tvDay = itemView.findViewById(R.id.tvWeekDay);
        tvTime = itemView.findViewById(R.id.tvHHMMAM);
    }
}
