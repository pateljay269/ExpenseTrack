package patel.jay.exmanager.Expense.Find;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.TransAdapter;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Trans;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static patel.jay.exmanager.R.style.AppTheme;
import static patel.jay.exmanager.Utility.MyConst.error;

/**
 * Created by Jay on 06-Dec-17.
 */

public class CalDialog {

    public void alertExView(Activity activity, String data[], String flag) {
        try {
            final Dialog dialog = new Dialog(activity, AppTheme);
            dialog.getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
            dialog.setContentView(R.layout.floating_rv_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.trans)));

            dialog.show();

            RecyclerView rv = dialog.findViewById(R.id.recyclerView);
            rv.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));

            FloatingActionButton fab = dialog.findViewById(R.id.fab);
            fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            RecyclerView.Adapter adapter = null;

            ArrayList list = null;

            list = new Trans(activity).calenderData(data, flag);
            adapter = new TransAdapter(list, activity);

            rv.setAdapter(adapter);

        } catch (Exception e) {
            error(activity, e);
        }
    }

    public void alertExView(Activity activity, String data, String flag) {
        try {
            final Dialog dialog = new Dialog(activity, AppTheme);
            dialog.getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
            dialog.setContentView(R.layout.floating_rv_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.trans)));

            dialog.show();

            RecyclerView rv = dialog.findViewById(R.id.recyclerView);
            rv.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));

            FloatingActionButton fab = dialog.findViewById(R.id.fab);
            fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            RecyclerView.Adapter adapter = null;

            ArrayList list = null;

            list = new Trans(activity).calenderRepo(data, flag);
            adapter = new TransAdapter(list, activity);

            rv.setAdapter(adapter);

        } catch (Exception e) {
            error(activity, e);
        }
    }
}