package patel.jay.expensetrack.ExpenseManager.Category;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

/**
 * Created by Jay on 06-08-2017.
 */

public class SetExCatLayout {

    EditText etCat;

    Activity context;

    public SetExCatLayout(Activity context) {
        this.context = context;
    }

    //region Blank Category Dalog
    public LinearLayout setLinearLayout() {
        //Category EditText
        LinearLayout layoutLinear = new LinearLayout(context);
        layoutLinear.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(100, 20, 100, 20);
        param.gravity = Gravity.CENTER;

        etCat = new EditText(context);
        etCat.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etCat.setHint("Enter Name");
        etCat.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        etCat.setLayoutParams(param);
        layoutLinear.addView(etCat);

        return layoutLinear;
    }

    public void alertAddCat(final String cType) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(setLinearLayout());
        alert.setTitle(cType);
        alert.setIcon(R.drawable.allorder);
        alert.setPositiveButton("Ok", null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = alert.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                //region Ok Click
                if (ok != null) {
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (TextUtils.isEmpty(etCat.getText().toString())) {
                                etCat.setError("Required");
                                return;
                            } else {
                                if (cType.equalsIgnoreCase(SQLiteHelper.CAT_AC)) {
                                    saveAccount(etCat.getText().toString());
                                } else {
                                    saveCategory(etCat.getText().toString(), cType);
                                }
                                dialog.cancel();
                            }

                        }
                    });
                }
                //endregion
            }
        });

        dialog.show();
    }

    private void saveAccount(String accName) {
        SqlExAccount sqlExAccount = new SqlExAccount(accName);
        long val = sqlExAccount.insert(context);
        toastMSG(val);
    }

    private void saveCategory(String sCatName, String sCaType) {
        try {
            SqlExCategory sqlExCategory = new SqlExCategory(sCatName, sCaType);
            long val = sqlExCategory.insert(context);
            toastMSG(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Filled Dialog Category

    public void alertEditCat(final SqlExCategory sqlExCategory) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(setLinearLayout());
        builder.setIcon(R.drawable.allorder);
        builder.setTitle(sqlExCategory.getCaType() + " Edit Category");
        builder.setCancelable(true);
        etCat.setText(sqlExCategory.getCatName());

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                int coutCid = SqlExTrans.countCid(context, sqlExCategory.getCid());

                if (coutCid == 0) {
                    long val = sqlExCategory.delete(context);
                    toastMSG(val);
                } else {
                    MyConstants.toast(context, "This Category is having some data ");//+toast+" Table");
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    SqlExCategory sqlExCategory1 = new SqlExCategory(sqlExCategory.getCid(), etCat.getText().toString(), sqlExCategory.getCaType());
                    long val = sqlExCategory1.update(context);
                    toastMSG(val);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //endregion

    //region Filled Account Details
    public void alertEditAcc(final SqlExAccount sqlExAccount) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(setLinearLayout());
        builder.setIcon(R.drawable.allorder);
        builder.setTitle("Edit Account");
        builder.setCancelable(true);
        etCat.setText(sqlExAccount.getAccName());

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                int coutCid = SqlExTrans.countAcId(context, sqlExAccount.getAcid());
                if (coutCid == 0) {
                    long val = sqlExAccount.delete(context);
                    toastMSG(val);
                } else {
                    MyConstants.toast(context, "This Account is having some data ");//+toast+" Table");
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SqlExAccount sqlExAccount1 = new SqlExAccount(sqlExAccount.getAcid(), etCat.getText().toString());
                long val = sqlExAccount1.update(context);
                toastMSG(val);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    //endregion

    private void toastMSG(long val) {
        if (val > 0) {
            try {
                ECatFragment.setRv(context);
                ICatFragment.setRv(context);
                AccFragment.setRv(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyConstants.toast(context, "Failed");
        }
    }

}
