package patel.jay.expensetrack.RechargeManager.Recharge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.DashBoard.AddFromHomeActivity;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

/**
 * Created by Jay on 8/16/2017.
 */

public class SetRecLayout {

    private String sDate, sMonth, sYear, now_date, now_time, toast = "", caType;
    private int fDD, fMM, fYY, fHH, fMin;
    private long timeInMilies = 0, val = 0;

    private EditText etAmount;
    private TextView tvDate;
    private Activity activity;
    private SqlRecharge sqlRecharge;

    public SetRecLayout(Activity activity) {
        this.activity = activity;
    }

    private LinearLayout setLinearLayout() {
        //Category etAmount
        LinearLayout layoutLinear = new LinearLayout(activity);
        layoutLinear.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        param.setMargins(100, 20, 100, 20);
        param.gravity = Gravity.CENTER;

        etAmount = new EditText(activity);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        etAmount.setHint("Enter " + caType);
        etAmount.setLayoutParams(param);
        etAmount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        tvDate = new TextView(activity);
        tvDate.setLayoutParams(param);
        tvDate.setTextSize(17);
        tvDate.setTextColor(activity.getResources().getColor(R.color.black));
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                showDatePicker(dayOfMonth, month, year, hour, minute);
            }
        });
        layoutLinear.addView(tvDate);
        layoutLinear.addView(etAmount);

        return layoutLinear;
    }

    //region Blank Recharge Dialog

    @SuppressLint("SetTextI18n")
    public void alertAddCat(String cType) {
        caType = cType;
        TimeConvert tC = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setView(setLinearLayout());
        alert.setTitle("Add " + cType);
        alert.setIcon(R.drawable.allorder);
        tvDate.setText(tC.getDD_MMM_YY() + " " + tC.getHH_Min_AP());

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
                            if (etAmount.getText().toString().isEmpty()) {
                                if (TextUtils.isEmpty(etAmount.getText().toString())) {
                                    etAmount.setError("Required");
                                }
                            } else {
                                int etAmt = Integer.parseInt(etAmount.getText().toString());
                                if (etAmt < 1) {
                                    etAmount.setError("Not Valid 0");
                                } else {
                                    saveCategory(etAmount.getText().toString(), timeInMilies, sDate, sMonth, sYear);
                                    dialog.cancel();

                                    AddFromHomeActivity.checkExit(activity, MyConstants.RECHARGE);
                                }
                            }
                        }
                    });
                }
                //endregion
            }
        });
        dialog.show();
    }

    private void saveCategory(String amount, long timeInMilies, String date, String month, String year) {
        try {
            //region TimeConvert
            TimeConvert tC = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

            if (timeInMilies == 0) {
                timeInMilies = System.currentTimeMillis();
                date = tC.getDD_MM_YY();
                month = tC.getMMM_YY();
                year = tC.getYy() + "";
            }
            String datetime = String.valueOf(timeInMilies);
            //endregion

            if (caType.equals(SQLiteHelper.COL_RECHARGE)) {
                sqlRecharge = new SqlRecharge(amount, "0", datetime, date, month, year);
            } else if (caType.equals(SQLiteHelper.COL_INCENTIVE)) {
                sqlRecharge = new SqlRecharge("0", amount, datetime, date, month, year);
            }

            long val = sqlRecharge.insert(activity);
            toastMSG(val);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //endregion

    //region Filled Dialog Recharge

    @SuppressLint("SetTextI18n")
    public void alertEditCat(final SqlRecharge sqlRecharge) {

        //region Retrive From Sqlite
        timeInMilies = Long.parseLong(sqlRecharge.getTime());
        TimeConvert tC = TimeConvert.timeMiliesConvert(timeInMilies);

        fDD = tC.getDd();
        fMM = tC.getMm();
        fYY = tC.getYy();
        fHH = tC.getHh();
        fMin = tC.getMin();

        final String recharge = sqlRecharge.getRecharge();
        final String incentive = sqlRecharge.getIncentive();
        final String time = sqlRecharge.getTime();
        final String date = sqlRecharge.getDate();
        final String month = sqlRecharge.getMonth();
        final String year = sqlRecharge.getYear();

        sDate = date;
        sMonth = month;
        sYear = year;

        //endregion

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(setLinearLayout());
        builder.setIcon(R.drawable.allorder);

        if (sqlRecharge.getRecharge().equals("0")) {
            builder.setTitle("Edit " + SQLiteHelper.COL_INCENTIVE);
            etAmount.setText(incentive);
        } else {
            builder.setTitle("Edit " + SQLiteHelper.COL_RECHARGE);
            etAmount.setText(recharge);
        }
        builder.setCancelable(true);

//        tvDate.setText(MyConstants.numberOf(fDD) + "-" + MyConstants.numberOf(fMM + 1) + "-" + fYY + " " + tC.getTime());
        tvDate.setText(tC.getDateTime());

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                long val = sqlRecharge.delete(activity, sqlRecharge.getRid());
                toastMSG(val);
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

                SqlRecharge sqlRecharge1 = null;
                if (sqlRecharge.getIncentive().equals("0")) {
                    sqlRecharge1 = new SqlRecharge(sqlRecharge.getRid(), etAmount.getText().toString(), "0",
                            String.valueOf(timeInMilies), sDate, sMonth, sYear);
                } else if (sqlRecharge.getRecharge().equals("0")) {
                    sqlRecharge1 = new SqlRecharge(sqlRecharge.getRid(), "0", etAmount.getText().toString(),
                            String.valueOf(timeInMilies), sDate, sMonth, sYear);
                }

                assert sqlRecharge1 != null;
                long val = sqlRecharge1.update(activity);
                toastMSG(val);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(fDD, fMM, fYY, fHH, fMin);
            }
        });

    }

    //endregion

    private void toastMSG(long val) {
        if (val > 0) {
            RechargeActivity.setRv(activity);
        } else {
            MyConstants.toast(activity, "Failed");
        }
    }

    //region Date/Time Dialog

    private void showDatePicker(final int dayOfMonth, final int month, final int year, final int hour, final int minute) {

        new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showTimePicker(dayOfMonth, month, year, hour, minute);
            }
        }, year, month, dayOfMonth).show();
    }

    private void showTimePicker(final int dayOfMonth, final int month, final int year, final int hour, final int minute) {

        new TimePickerDialog(activity,
                new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String tempDate = "", tempMonth = "";

                        tempDate = MyConstants.numberOf(dayOfMonth);
                        tempMonth = MyConstants.numberOf(month + 1);

                        sDate = tempDate + "-" + tempMonth + "-" + year;
                        sMonth = MyConstants.monthName(month) + " " + year;
                        sYear = String.valueOf(year);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0);

                        timeInMilies = calendar.getTimeInMillis();
                        tvDate.setText(tempDate + "-" + tempMonth + "-" + year + " " + hourOfDay + ":" + minute);
                    }
                }, hour, minute, true).show();
    }

    //endregion
}
