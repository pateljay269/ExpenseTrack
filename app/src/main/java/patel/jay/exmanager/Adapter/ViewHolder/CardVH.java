package patel.jay.exmanager.Adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import patel.jay.exmanager.R;

public class CardVH extends RecyclerView.ViewHolder {

    public TextView tvRs, tvDateDD, tvDateMY, tvDay, tvDsc, tvTime, tvCat, tvExtra;
    public LinearLayout cardLayout, linearMain, cardDateLayput;

    public CardVH(View itemView) {
        super(itemView);
        cardLayout = itemView.findViewById(R.id.cardLayout);
        cardDateLayput = itemView.findViewById(R.id.cardDateLayput);
        linearMain = itemView.findViewById(R.id.linearMain);
        tvRs = itemView.findViewById(R.id.tvRs);
        tvDateDD = itemView.findViewById(R.id.tvDateDD);
        tvDateMY = itemView.findViewById(R.id.tvDateMY);
        tvDay = itemView.findViewById(R.id.tvDay);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvCat = itemView.findViewById(R.id.tvCat);
        tvDsc = itemView.findViewById(R.id.tvDsc);
        tvExtra = itemView.findViewById(R.id.tvExtra);
    }
}
