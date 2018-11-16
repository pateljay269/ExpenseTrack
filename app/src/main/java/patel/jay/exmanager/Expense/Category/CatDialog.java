package patel.jay.exmanager.Expense.Category;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import patel.jay.exmanager.SQL.Account;
import patel.jay.exmanager.SQL.Category;
import patel.jay.exmanager.SQL.Trans;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;
import static patel.jay.exmanager.SQL.SQL.CAT_AC;
import static patel.jay.exmanager.Utility.MyConst.toast;

/**
 * Created by Jay on 06-08-2017.
 */

public class CatDialog {

    private EditText etCat;

    private Activity context;

    public CatDialog(Activity context) {
        this.context = context;
    }

    //region Blank Category Dalog
    private LinearLayout setLinearLayout() {
        //Category EditText
        LinearLayout layoutLinear = new LinearLayout(context);
        layoutLinear.setOrientation(VERTICAL);

        LayoutParams param = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        param.setMargins(100, 20, 100, 20);
        param.gravity = Gravity.CENTER;

        etCat = new EditText(context);
        etCat.setInputType(TYPE_TEXT_FLAG_CAP_WORDS);
        etCat.setHint("Enter Name");
        etCat.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        etCat.setLayoutParams(param);

        layoutLinear.addView(etCat);

        return layoutLinear;
    }

    public void alertAddCat(final String cType) {

        Builder alert = new Builder(context);
        alert.setView(setLinearLayout());
        alert.setTitle(cType);
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
                Button ok = dialog.getButton(BUTTON_POSITIVE);

                //region Ok Click
                if (ok != null) {
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (TextUtils.isEmpty(etCat.getText().toString())) {
                                etCat.setError("Required");
                            } else {
                                if (cType.equalsIgnoreCase(CAT_AC)) {
                                    saveAccount(etCat.getText().toString());
                                } else {
                                    saveCategory(etCat.getText().toString());
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
        Account account = new Account(accName);
        long val = account.insert(context);
        toastMSG(val);
    }

    private void saveCategory(String sCatName) {
        try {
            Category category = new Category(sCatName);
            long val = category.insert(context);
            toastMSG(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Filled Dialog Category

    public void alertEditCat(final Category category) {

        Builder builder = new Builder(context);
        builder.setView(setLinearLayout());
        builder.setCancelable(true);
        etCat.setText(category.getCatName());

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                int coutCid = new Trans(context).countCid(category.getCid());

                if (coutCid == 0) {
                    long val = category.delete(context);
                    toastMSG(val);
                } else {
                    toast(context, "This Category is having some data.");
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
                    Category category1 = new Category(category.getCid(), etCat.getText().toString());
                    long val = category1.update(context);
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
    public void alertEditAcc(final Account account) {

        Builder builder = new Builder(context);
        builder.setView(setLinearLayout());
        builder.setTitle("Edit Account");
        builder.setCancelable(true);
        etCat.setText(account.getAccName());

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                int coutCid = new Trans(context).countAcId(account.getAcid());
                if (coutCid == 0) {
                    long val = account.delete(context);
                    toastMSG(val);
                } else {
                    toast(context, "This Account is having some data.");
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
                Account account1 = new Account(account.getAcid(), etCat.getText().toString());
                long val = account1.update(context);
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
                AccFragment.setRv(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            toast(context, "Failed");
        }
    }

}
