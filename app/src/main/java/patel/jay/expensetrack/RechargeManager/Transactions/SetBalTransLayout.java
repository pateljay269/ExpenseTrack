package patel.jay.expensetrack.RechargeManager.Transactions;

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
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.DashBoard.AddFromHomeActivity;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.FindTotal.BalCustFindActivity;
import patel.jay.expensetrack.RechargeManager.FindTotal.BalDateFindActivity;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;

/**
 * Created by Jay on 08-08-2017.
 */

public class SetBalTransLayout implements View.OnFocusChangeListener {

    //region Global Declaration
    private String sDsc, sDate, sMonth, sYear, sYYMMDD;
    private int fDD, fMM, fYY, fHH, fMin;
    private long timeInMilies = 0, val = 0;

    private SwitchButton sbtnCaType;
    private Spinner spnCustomer;
    private TextView tvDate, tvTime;
    private MaterialEditText etDesc, etAmount;

    private ArrayList<String> customerList;
    private ArrayAdapter<String> arrayAdapter;

    private SqlBalAccount sqlBalAccount;

    private Activity activity;
    private String customerName = "", sCatType;
    //endregion

    //region Constructors
    public SetBalTransLayout(Activity activity) {
        this.activity = activity;
    }

    public SetBalTransLayout(Activity activity, String customerName) {
        this.activity = activity;
        this.customerName = customerName;
    }
    //endregion

    //region Methods
    private View setCustomView(final boolean isFilled) {
        View view = null;

        try {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.dialog_bal_trans, null);

            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            spnCustomer = (Spinner) view.findViewById(R.id.spnCustomer);
            etDesc = (MaterialEditText) view.findViewById(R.id.etDesc);
            etAmount = (MaterialEditText) view.findViewById(R.id.etAmount);
            sbtnCaType = (SwitchButton) view.findViewById(R.id.sbtnCaType);
            setCurrentTime(isFilled);

            sCatType = SQLiteHelper.COL_EXPENSE;
            sbtnCaType.setText(SQLiteHelper.COL_INCOME, SQLiteHelper.COL_EXPENSE);
            sbtnCaType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    sCatType = isChecked ? SQLiteHelper.COL_INCOME : SQLiteHelper.COL_EXPENSE;
                }
            });

            etAmount.setOnFocusChangeListener(this);
            etDesc.setOnFocusChangeListener(this);

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

            spnCustomer.setBackgroundResource(R.drawable.border);
            customerList = SqlCustomer.allCustomerString(activity);
            arrayAdapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, customerList);
            spnCustomer.setAdapter(arrayAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
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

    private boolean emptyValidation(int idd, String amount) {

        if (idd == 0 || amount.isEmpty()) {
            if (idd == 0) {
                ((TextView) spnCustomer.getChildAt(0).findViewById(R.id.tvRow)).setError("Required");
            }
            if (TextUtils.isEmpty(etAmount.getText().toString())) {
                etAmount.setError("Required");
            }
            return false;
        } else {
            int etAmt = Integer.parseInt(amount);
            if (etAmt < 1) {
                etAmount.setError("Not Valid 0");
                return false;
            } else {
                return true;
            }
        }
    }

    private void toastMSG(long val) {
        try {
            if (val > 0) {
                if (activity instanceof BalDateFindActivity) {
                    BalDateFindActivity.setRvUpdate(activity);
                } else if (activity instanceof BalCustFindActivity) {
                    BalCustFindActivity.setRv(activity, sqlBalAccount.getCustName());
                } else {
                    BalExFragment.setRv(activity);
                    BalInFragment.setRv(activity);
                }
            } else {
                MyConstants.toast(activity, "Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            MyConstants.hideSoftKeyboard(activity);
        }
    }

    //endregion

    //region Transaction Blank Dialog

    public void alertAddCat(String cType) {
        try {
            //region Blank Dialog
            TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setView(setCustomView(false))
//                    .setTitle("Add " + cType).setIcon(R.drawable.allorder)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            //region CustomerFindActivity FloatingButton Add Click
            if (!customerName.equals("")) {
                arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, customerList);
                spnCustomer.setSelection(customerList.indexOf(customerName));
            }
            //endregion

            if (cType.equals(SQLiteHelper.COL_EXPENSE)) {
                sbtnCaType.setChecked(false);
            } else {
                sbtnCaType.setChecked(true);
            }

            tvDate.setText(tc.getDD_MM_YY());
            tvTime.setText(tc.getHH_Min_AP());

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
                                    int idd = spnCustomer.getSelectedItemPosition();
                                    String amount = etAmount.getText().toString().trim();

                                    if (emptyValidation(idd, amount)) {

                                        sDsc = etDesc.getText().toString();
                                        String sCustomer = spnCustomer.getSelectedItem().toString();
                                        String sCustMobile = SqlCustomer.nameToMobile(activity, sCustomer);
                                        saveCustDetail(sCustMobile, amount, timeInMilies, sDate, sMonth, sYear, sYYMMDD, sDsc);
                                        dialog.cancel();

                                        AddFromHomeActivity.checkExit(activity, MyConstants.BAL);
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

            dialog.show();
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCustDetail(String custName, String amount, long timeInMili, String date,
                                String month, String year, String yymmdd, String description) {
        try {
            //region TimeConvert

            if (timeInMili == 0) {
                timeInMili = System.currentTimeMillis();
                TimeConvert tc = TimeConvert.timeMiliesConvert(timeInMili);

                date = tc.getDD_MM_YY();
                month = tc.getMMM_YY();
                year = tc.getYy() + "";
                yymmdd = tc.getYYYY_MM_DD();
            }
            String datetime = timeInMili + "";
            //endregion

//            if (caType.equals(SQLiteHelper.COL_EXPENSE)) {
            if (sCatType.equals(SQLiteHelper.COL_EXPENSE)) {
                sqlBalAccount = new SqlBalAccount(custName, description, datetime, date, month, year, yymmdd, amount, "0");
//            } else if (caType.equals(SQLiteHelper.COL_INCOME)) {
            } else if (sCatType.equals(SQLiteHelper.COL_INCOME)) {
                sqlBalAccount = new SqlBalAccount(custName, description, datetime, date, month, year, yymmdd, "0", amount);
            }

            val = sqlBalAccount.insert(activity);
            toastMSG(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //endregion

    //region Fill Dialog

    public void alertEditView(final SqlBalAccount account) {
        try {
            //region Retrive From Sqlite
            timeInMilies = Long.parseLong(account.getTime());
            TimeConvert tc = TimeConvert.timeMiliesConvert(timeInMilies);

            fDD = tc.getDd();
            fMM = tc.getMm();
            fYY = tc.getYy();
            fHH = tc.getHh();
            fMin = tc.getMin();

            sDate = account.getDate();
            sMonth = account.getMonth();
            sYear = account.getYear();
            sYYMMDD = account.getYymmdd();

            //endregion

            //region Dialog Builder

            //region Fill Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(setCustomView(true))
//            .setIcon(R.drawable.allorder).setTitle("Edit Details")
                    .setCancelable(true);

            if (account.getIncome().equals("0")) {
                etAmount.setText(account.getExpense());
                sbtnCaType.setChecked(false);
            } else if (account.getExpense().equals("0")) {
                etAmount.setText(account.getIncome());
                sbtnCaType.setChecked(true);
            }

            etDesc.setText(account.getDsc());
            tvDate.setText(tc.getDD_MM_YY());
            tvTime.setText(tc.getHH_Min_AP());
            String custName = SqlCustomer.mobileToName(activity, account.getCustName());

            customerList = SqlCustomer.allCustomerString(activity);
            arrayAdapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, customerList);
            spnCustomer.setAdapter(arrayAdapter);
            spnCustomer.setSelection(customerList.indexOf(custName));

            builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialoginterface, int i) {
                    long val = account.delete(activity);
                    toastMSG(val);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            builder.setPositiveButton("Update", null);
            //endregion

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
                                try {
                                    //region Update
                                    int idd = spnCustomer.getSelectedItemPosition();
                                    String amount = etAmount.getText().toString().trim();

                                    if (emptyValidation(idd, amount)) {

                                        /*if (timeInMilies == 0) {
                                            timeInMilies = System.currentTimeMillis();
                                        }*/

                                        String sCustName = spnCustomer.getSelectedItem().toString();
                                        String mobile = SqlCustomer.nameToMobile(activity, sCustName);
                                        String sDsc = etDesc.getText().toString();
                                        String datetime = String.valueOf(timeInMilies);

                                        SqlBalAccount account1 = null;
                                        if (account.getIncome().equals("0")) {
                                            account1 = new SqlBalAccount(account.getTid(), mobile,
                                                    sDsc, datetime, sDate, sMonth, sYear, sYYMMDD, amount, "0");
                                        } else if (account.getExpense().equals("0")) {
                                            account1 = new SqlBalAccount(account.getTid(), mobile,
                                                    sDsc, datetime, sDate, sMonth, sYear, sYYMMDD, "0", amount);
                                        }

                                        assert account1 != null;
                                        val = account1.update(activity);
                                        sqlBalAccount = account;
//                                        changeType(val);
                                        alert.cancel();
                                        toastMSG(val);
                                    }
                                    //endregion
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    //endregion
                }
            });

            WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();

            wmlp.gravity = Gravity.TOP | Gravity.CENTER;
//            wmlp.x = 100;   //x position
            wmlp.y = 0;   //y position

            //endregion

            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
//            MyConstants.toast(activity, e.getMessage());
        }
    }

    private void changeType(long val) {
        if (val > 0) {
            long vall = sqlBalAccount.updateTypeChange(activity);
            if (vall > 0) {
                MyConstants.toast(activity, "Category Type Changed");
            }
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

                        try {
                            fillTimeInMilies();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                        }

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
//        sWeek = tc.getWeek_MMM_YY();    //calendar.get(Calendar.WEEK_OF_MONTH) + " " + sMonth;

        tvDate.setText(sDate);
        tvTime.setText(tc.getHH_Min_AP());
    }

    //endregion

}