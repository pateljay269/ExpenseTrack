package patel.jay.expensetrack.RechargeManager.Customer;

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
import android.widget.TextView;

import java.util.Arrays;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;

/**
 * Created by Jay on 09-08-2017.
 */

public class SetCustLayout {

    private EditText etName, etMobile, etDsc;
    private TextView tvDate;
    private String sRegDate;
    private Activity context;
//    private SqlCustomer sqlCustomer;

    public SetCustLayout(Activity activity) {
        this.context = activity;
    }

    private LinearLayout setLinearLayout(long timeInmiles) {
        //Category etName,etMobile,etDsc;
        LinearLayout layoutLinear = new LinearLayout(context);
        layoutLinear.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(100, 20, 100, 20);
        param.gravity = Gravity.CENTER;

        etName = new EditText(context);
        etName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etName.setHint("Enter Name");
        etName.setLayoutParams(param);
        etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        etMobile = new EditText(context);
        etMobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        etMobile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        etMobile.setHint("Enter Mobile");
        etMobile.setLayoutParams(param);

        etDsc = new EditText(context);
        etDsc.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etDsc.setHint("Description (Optional)");
        etDsc.setLayoutParams(param);

        tvDate = new TextView(context);
        tvDate.setLayoutParams(param);
        tvDate.setTextSize(17);
        sRegDate = timeInmiles + "";
        TimeConvert tc = TimeConvert.timeMiliesConvert(timeInmiles);
        String time = "Register On: " + tc.getDateTime();
        tvDate.setText(time);
        tvDate.setTextColor(context.getResources().getColor(R.color.black));

        layoutLinear.addView(tvDate);
        layoutLinear.addView(etName);
        layoutLinear.addView(etMobile);
        layoutLinear.addView(etDsc);

        return layoutLinear;
    }

    //region Blank Customer Dialog

    public void alertAddCat(String name, String mobileNo) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(setLinearLayout(System.currentTimeMillis()));
        alert.setTitle("Add Customer");
        alert.setIcon(R.drawable.allorder);
        etName.setText(name);
        etMobile.setText(mobileNo);
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
                            String sName = etName.getText().toString();
                            String sMobile = etMobile.getText().toString();

                            if (sName.isEmpty() || sMobile.isEmpty()) {
                                if (TextUtils.isEmpty(etName.getText().toString())) {
                                    etName.setError("Required");
                                }
                                if (TextUtils.isEmpty(etMobile.getText().toString())) {
                                    etMobile.setError("Required");
                                }
                            } else {
                                for (String aSPECIAL_CHAR : MyConstants.SPECIAL_CHAR) {
                                    if (sName.contains(aSPECIAL_CHAR)) {
                                        etName.setError("Not Allow " + Arrays.toString(MyConstants.SPECIAL_CHAR));
                                        return;
                                    }
                                }
                                saveCustomer(sName, sMobile, sRegDate, etDsc.getText().toString());
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

    private void saveCustomer(String name, String mobile, String time, String dsc) {
        try {
            SqlCustomer sqlCustomer = new SqlCustomer(name, mobile, time, dsc);
            long val = sqlCustomer.insert(context);
            toastMSG(val);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //endregion

    //region Filled Dialog Category

    public void alertEditCat(final SqlCustomer sqlCustomer) {
//        this.sqlCustomer = sqlCustomer;
        final String cName = sqlCustomer.getCustName();
        final String cMobile = sqlCustomer.getCustMobile();
        String cDsc = sqlCustomer.getCustDsc();
        final String cDate = sqlCustomer.getCustRegDate();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(setLinearLayout(Long.parseLong(cDate)));
        builder.setIcon(R.drawable.allorder);
        builder.setTitle("Edit Customer Details");
        builder.setCancelable(true);
        etName.setText(cName);
        etMobile.setText(cMobile);
        etDsc.setText(cDsc);

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                int coutCid = SqlBalAccount.countCid(context, cMobile);
                if (coutCid == 0) {
                    long val = sqlCustomer.delete(context);
                    toastMSG(val);
                } else {
                    MyConstants.toast(context, "This Customer is having some data ");//+toast+" Table");
                }
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Update", null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button update = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                //region Update Click
                if (update != null) {
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String sName = etName.getText().toString();
                            for (String aSPECIAL_CHAR : MyConstants.SPECIAL_CHAR) {
                                if (sName.contains(aSPECIAL_CHAR)) {
                                    etName.setError("Not Allow " + Arrays.toString(MyConstants.SPECIAL_CHAR));
                                    return;
                                }
                            }
                            SqlCustomer sqlCustomer1 = new SqlCustomer(sName, etMobile.getText().toString(),
                                    cDate, etDsc.getText().toString());
                            long val = sqlCustomer1.update(context, sqlCustomer.getCustMobile());
                            toastMSG(val);
                            dialog.cancel();
                        }
                    });
                }
                //endregion
            }
        });

        dialog.show();
    }

    //endregion

    private void toastMSG(long val) {
        if (val > 0) {
            CustomersActivity.setRv(context);
//            MyConstants.toast(context, "Success");
        } else {
            MyConstants.toast(context, "Failed");
        }
    }
}