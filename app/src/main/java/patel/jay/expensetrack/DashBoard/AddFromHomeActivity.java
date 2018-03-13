package patel.jay.expensetrack.DashBoard;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ExpenseManager.AllTransaction.SetExTransLayout;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Recharge.SetRecLayout;
import patel.jay.expensetrack.RechargeManager.Transactions.SetBalTransLayout;

public class AddFromHomeActivity extends AppCompatActivity {

    public static boolean check;
    GridView spnView;
    GridLayout layoutSpn;
    GridView.LayoutParams spnParam;
    AlertDialog gridDialog;
    ArrayList<String> arrayList;
    Activity activity = AddFromHomeActivity.this;

    public static void checkExit(Activity activity, String type) {
        if (check) {
            check = false;
//            switch (type) {
//                case MyConstants.BAL:
//                    activity.startActivity(new Intent(activity, BalTransactionActivity.class));
//                    break;
//
//                case MyConstants.RECHARGE:
//                    activity.startActivity(new Intent(activity, RechargeActivity.class));
//                    break;
//
//                case MyConstants.EXPENSE:
//                    activity.startActivity(new Intent(activity, ExTransactionActivity.class));
//                    break;
//            }
            MyConstants.backClick(activity);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_home);

        try {
            alertSpnCat();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GridLayout categoryDialog() {
        try {
            //region Category
            spnView = new GridView(this);
            layoutSpn = new GridLayout(this);
            spnParam = new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT);

            spnView.setLayoutParams(spnParam);
            spnView.setNumColumns(2);

            arrayList = new ArrayList<>();
            arrayList.add(MyConstants.EXPENSE);
            arrayList.add(MyConstants.RECHARGE);
            arrayList.add(MyConstants.BAL);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

            spnView.setAdapter(arrayAdapter);
            layoutSpn.addView(spnView);

            //endregion
        } catch (Exception e) {
            e.printStackTrace();
        }

        return layoutSpn;
    }

    private void alertSpnCat() {
        gridDialog = new AlertDialog.Builder(this)
                .setView(categoryDialog())
                .setTitle("Select Any")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        gridDialog.show();

        spnView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String cType = SQLiteHelper.COL_EXPENSE;
                check = true;
                switch (arrayList.get(position)) {
                    case MyConstants.BAL:
                        SetBalTransLayout layoutBal = new SetBalTransLayout(activity);
                        layoutBal.alertAddCat(cType);
                        break;

                    case MyConstants.RECHARGE:
                        cType = SQLiteHelper.COL_RECHARGE;
                        SetRecLayout layoutRec = new SetRecLayout(activity);
                        layoutRec.alertAddCat(cType);
                        break;

                    case MyConstants.EXPENSE:
                        SetExTransLayout layoutEx = new SetExTransLayout(activity);
                        layoutEx.alertAddCat(cType);
                        break;
                }
                gridDialog.dismiss();
            }
        });
    }
}
