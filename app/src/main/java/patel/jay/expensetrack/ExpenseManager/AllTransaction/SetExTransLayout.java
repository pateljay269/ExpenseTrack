package patel.jay.expensetrack.ExpenseManager.AllTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kyleduo.switchbutton.SwitchButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.DashBoard.AddFromHomeActivity;
import patel.jay.expensetrack.ExpenseManager.Find.ExCatFindActivity;
import patel.jay.expensetrack.ExpenseManager.Find.ExDateFindActivity;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

/**
 * Created by Jay on 06-08-2017.
 */

public class SetExTransLayout implements View.OnFocusChangeListener {

    //region Global Declaration
    private String sCat, sAcc, sDsc, sDate, sMonth, sYear, sWeek, now_date, now_time, sYYMMDD;
    private int fDD, fMM, fYY, fHH, fMin;
    private long timeInMilies = 0, val = 0;

    private Spinner spnCategory, spnAccount;
    private TextView tvDate, tvTime;
    private MaterialEditText etDesc, etAmount;
    private SwitchButton sbtnCaType;
    private String sCatType;

    private ArrayList<String> categoryString, accString;

    private SqlExTrans sqlExTrans;
    private Activity activity;
    //endregion

    public SetExTransLayout(Activity activity) {
        this.activity = activity;
    }

    private void setCurrentTime(boolean isFilled) {
        if (!isFilled) {
            Calendar calendar = Calendar.getInstance();
            fYY = calendar.get(Calendar.YEAR);
            fMM = calendar.get(Calendar.MONTH);
            fDD = calendar.get(Calendar.DAY_OF_MONTH);
            fHH = calendar.get(Calendar.HOUR_OF_DAY);
            fMin = calendar.get(Calendar.MINUTE);
//            fillTimeInMilies();
        }
    }

    private View setCustomView(String cType, final boolean isFilled) {
        View view = null;

        try {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.dialog_ex_trans, null);

            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            spnAccount = (Spinner) view.findViewById(R.id.spnAccount);
            spnCategory = (Spinner) view.findViewById(R.id.spnCategory);
            etDesc = (MaterialEditText) view.findViewById(R.id.etDesc);
            etAmount = (MaterialEditText) view.findViewById(R.id.etAmount);
            sbtnCaType = (SwitchButton) view.findViewById(R.id.sbtnCaType);
            setCurrentTime(isFilled);

            etAmount.setOnFocusChangeListener(this);
            etDesc.setOnFocusChangeListener(this);

            sCatType = SQLiteHelper.COL_EXPENSE;
            sbtnCaType.setText(SQLiteHelper.COL_INCOME, SQLiteHelper.COL_EXPENSE);
            sbtnCaType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    sCatType = isChecked ? SQLiteHelper.COL_INCOME : SQLiteHelper.COL_EXPENSE;
                    setCategory(sCatType);
                }
            });

            tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker(fDD, fMM, fYY, fHH, fMin);
                }
            });

            tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(fHH, fMin);
                }
            });

            setCategory(cType);
            spnAccount.setBackgroundResource(R.drawable.border);
            ArrayList<String> accString = SqlExAccount.allAccString(activity);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, accString);
            spnAccount.setAdapter(spinnerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void setCategory(String cType) {
        spnCategory.setBackgroundResource(R.drawable.border);
        ArrayList<String> categoryString = SqlExCategory.allCategoryString(activity, cType);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, categoryString);
        spnCategory.setAdapter(spinnerArrayAdapter);

    }

    //region Transaction Blank Dialog

    public void alertAddCat(final String cType) {
        try {
            TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

            //region Blank Dialog

            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setCancelable(true);

            alert.setView(setCustomView(cType, false));//alert.setView(setLayoutBlank(cType));
            tvDate.setText(tc.getDD_MM_YY());
            tvTime.setText(tc.getHH_Min_AP());
//            alert.setTitle("Add " + cType);
            alert.setIcon(R.drawable.allorder);
            alert.setPositiveButton(android.R.string.ok, null)
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
                                try {
                                    int idc = spnCategory.getSelectedItemPosition();
                                    int ida = spnAccount.getSelectedItemPosition();

                                    if (idc == 0 || ida == 0 || etAmount.getText().toString().isEmpty()) {
                                        if (TextUtils.isEmpty(etAmount.getText().toString())) {
                                            etAmount.setError("Required");
                                        }
                                        if (ida == 0) {
                                            ((TextView) spnAccount.getChildAt(0).findViewById(R.id.tvRow)).setError("Required");
                                        }
                                        if (idc == 0) {
                                            ((TextView) spnCategory.getChildAt(0).findViewById(R.id.tvRow)).setError("Required");
                                        }
                                    } else {
                                        int etAmt = Integer.parseInt(etAmount.getText().toString());
                                        if (etAmt < 1) {
                                            etAmount.setError("Not Valid 0");
                                        } else {
                                            sDsc = etDesc.getText().toString();
                                            sCat = spnCategory.getSelectedItem().toString();
                                            sAcc = spnAccount.getSelectedItem().toString();
                                            saveCategory(etAmount.getText().toString(), timeInMilies,
                                                    sDate, sMonth, sYear, sYYMMDD, sWeek,
                                                    sDsc, sCat, sAcc, cType);
                                            dialog.cancel();

                                            AddFromHomeActivity.checkExit(activity, MyConstants.EXPENSE);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    //endregion
                }
            });

            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

            wmlp.gravity = Gravity.TOP | Gravity.CENTER;
//            wmlp.x = 100;   //x position
            wmlp.y = 0;   //y position

            //endregion

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCategory(String amount, long timeInMili, String date, String month, String year,
                              String yymmdd, String week, String description, String catName,
                              String accName, String caType) {
        try {
            //region TimeConvert

            if (timeInMili == 0) {
                timeInMili = System.currentTimeMillis();
                TimeConvert tc = TimeConvert.timeMiliesConvert(timeInMili);

                date = tc.getDD_MM_YY();
                month = tc.getMMM_YY();
                year = tc.getYy() + "";
                yymmdd = tc.getYYYY_MM_DD();
                week = tc.getWeek_MMM_YY();
            }

            String datetime = timeInMili + "";
            //endregion

            int cid = SqlExCategory.categoryInt(activity, catName);
            int acid = SqlExAccount.accInt(activity, accName);
            SqlExTrans sqlExTrans = null;
            if (caType.equals(SQLiteHelper.CAT_EX)) {
                sqlExTrans = new SqlExTrans(amount, "0", datetime, date, month, year, yymmdd, week, description, acid, cid, caType);
            } else if (caType.equals(SQLiteHelper.CAT_IN)) {
                sqlExTrans = new SqlExTrans("0", amount, datetime, date, month, year, yymmdd, week, description, acid, cid, caType);
            }

            assert sqlExTrans != null;
            val = sqlExTrans.insert(activity);
            toastMSG(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region Filling Transaction

    public void alertEditView(final SqlExTrans sqlTrans) {
        try {
            //region Retrive From Sqlite
            timeInMilies = Long.parseLong(sqlTrans.getTime());
            TimeConvert tc = TimeConvert.timeMiliesConvert(timeInMilies);

            fDD = tc.getDd();
            fMM = tc.getMm();
            fYY = tc.getYy();
            fHH = tc.getHh();
            fMin = tc.getMin();

            sDate = sqlTrans.getDate();
            sMonth = sqlTrans.getMonth();
            sYear = sqlTrans.getYear();
            sYYMMDD = sqlTrans.getYymmdd();
            sWeek = sqlTrans.getWeekNumber();

            sqlExTrans = sqlTrans;
            //endregion

            //region Dialog Builder

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(setCustomView(sqlTrans.getCaType(), true));//builder.setView(setLayoutFill());
            builder.setIcon(R.drawable.allorder);
//            builder.setTitle("Edit Details");
            builder.setCancelable(true);

            //region Fill Details In Builder
            accString = SqlExAccount.allAccString(activity);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, accString);
            spnAccount.setAdapter(spinnerAdapter);

            categoryString = SqlExCategory.allCategoryString(activity, sqlTrans.getCaType());
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, categoryString);
            spnCategory.setAdapter(spinnerArrayAdapter);

            String sCid = SqlExCategory.categoryString(activity, sqlTrans.getCid());
            String sAcid = SqlExAccount.accString(activity, sqlTrans.getAcid());

            spnAccount.setSelection(accString.indexOf(sAcid));
            spnCategory.setSelection(categoryString.indexOf(sCid));

            etDesc.setText(sqlTrans.getDescription());
            tvDate.setText(tc.getDD_MM_YY());
            tvTime.setText(tc.getHH_Min_AP());

            if (sqlTrans.getCaType().equals(SQLiteHelper.CAT_EX)) {
                etAmount.setText(sqlTrans.getEx_rs());
            } else {
                etAmount.setText(sqlTrans.getIn_rs());
            }
            //endregion

            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    long val = sqlTrans.delete(activity);
                    toastMSG(val);
                }
            });


            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            builder.setPositiveButton("Update", null);

            final AlertDialog alert = builder.create();

            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button update = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                    //region Update Click

                    if (update != null) {
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //region Update
                                int idc = spnCategory.getSelectedItemPosition();
                                int ida = spnAccount.getSelectedItemPosition();
                                if (idc == 0 || ida == 0 || etAmount.getText().toString().isEmpty()) {
                                    if (TextUtils.isEmpty(etAmount.getText().toString())) {
                                        etAmount.setError("Required");
                                    }
                                    if (ida == 0) {
                                        ((TextView) spnAccount.getChildAt(0).findViewById(R.id.tvRow)).setError("Required");
                                    }
                                    if (idc == 0) {
                                        ((TextView) spnCategory.getChildAt(0).findViewById(R.id.tvRow)).setError("Required");
                                    }
                                } else {
                                    String sAmount = etAmount.getText().toString();
                                    String sDsc = etDesc.getText().toString();
                                    if (timeInMilies == 0) {
                                        timeInMilies = System.currentTimeMillis();
                                    }
                                    int sCat = SqlExCategory.categoryInt(activity, spnCategory.getSelectedItem().toString());//spnCategory.getSelectedItemPosition();
                                    int sAcc = SqlExAccount.accInt(activity, spnAccount.getSelectedItem().toString());//spnAccount.getSelectedItemPosition();
                                    String datetime = String.valueOf(timeInMilies);
                                    String caType = sqlTrans.getCaType();

                                    SqlExTrans sqlExTrans1 = null;
                                    if (caType.equals(SQLiteHelper.CAT_EX)) {
                                        sqlExTrans1 = new SqlExTrans(sqlTrans.getAid(), sAmount, "0", datetime,
                                                sDate, sMonth, sYear, sYYMMDD, sWeek, sDsc, sAcc, sCat, caType);
                                    } else if (caType.equals(SQLiteHelper.CAT_IN)) {
                                        sqlExTrans1 = new SqlExTrans(sqlTrans.getAid(), "0", sAmount, datetime,
                                                sDate, sMonth, sYear, sYYMMDD, sWeek, sDsc, sAcc, sCat, caType);
                                    }

                                    assert sqlExTrans1 != null;
                                    val = sqlExTrans1.update(activity);
                                    alert.cancel();
                                    toastMSG(val);
                                }
                                //endregion
                            }
                        });
                    }

                    //endregion
                }
            });

            WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();

            wmlp.gravity = Gravity.TOP | Gravity.CENTER;
//            wmlp.x = 100;   //x position
            wmlp.y = 0;

            //endregion

            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toastMSG(long val) {
        if (val > 0) {
            if (activity instanceof ExDateFindActivity) {
                ExDateFindActivity.setRv(activity);
            } else if (activity instanceof ExCatFindActivity) {
                ExCatFindActivity.setRv(activity);
            } else {
                TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
                int mm = tc.getMm();

                ExOutFragment.setArrayList(activity, tc.getYy(), MyConstants.monthName(mm), false);
                ExInFragment.setArrayList(activity, 0, "", false);
            }
        } else {
            MyConstants.toast(activity, "Failed");
        }
    }

    //endregion

    //region Date/Time Dialog

    private void showDatePicker(final int dd, final int mm, final int yy, final int hour, final int minute) {

        new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fDD = dayOfMonth;
                fMM = month;
                fYY = year;
                fHH = hour;
                fMin = minute;

                showTimePicker(hour, minute);
            }
        }, yy, mm, dd).show();
    }

    private void showTimePicker(final int hour, final int minute) {

        new TimePickerDialog(activity,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        fHH = hourOfDay;
                        fMin = minute;

                        fillTimeInMilies();
                    }
                }, hour, minute, true).show();
    }

    private void fillTimeInMilies() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(fYY, fMM, fDD, fHH, fMin, 0);

        timeInMilies = calendar.getTimeInMillis();
        TimeConvert tc = TimeConvert.timeMiliesConvert(timeInMilies);

        sYear = tc.getYy() + "";          //fYY + "";
        sMonth = tc.getMMM_YY();        //MyConstants.monthName(fMM) + " " + fYY;
        sYYMMDD = tc.getYYYY_MM_DD();   //fYY + numberOf(fMM + 1) + numberOf(fDD);
        sDate = tc.getDD_MM_YY();       //numberOf(fDD) + "-" + numberOf(fMM + 1) + "-" + fYY;
        sWeek = tc.getWeek_MMM_YY();    //calendar.get(Calendar.WEEK_OF_MONTH) + " " + sMonth;

        tvDate.setText(sDate);
        tvTime.setText(tc.getHH_Min_AP());
    }
    //endregion

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            MyConstants.hideSoftKeyboard(activity);
        }
    }

}
