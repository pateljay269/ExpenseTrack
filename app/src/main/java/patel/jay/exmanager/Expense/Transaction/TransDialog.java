package patel.jay.exmanager.Expense.Transaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import patel.jay.exmanager.Calender.CalenderActivity;
import patel.jay.exmanager.Expense.Find.CatFindActivity;
import patel.jay.exmanager.Expense.Find.DateFindActivity;
import patel.jay.exmanager.Expense.Find.MonFindActivity;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Account;
import patel.jay.exmanager.SQL.Category;
import patel.jay.exmanager.SQL.Trans;
import patel.jay.exmanager.Utility.TimeConvert;

import static android.R.string.cancel;
import static android.R.string.ok;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.TOP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;
import static patel.jay.exmanager.Calender.CalenderActivity.setViewPager;
import static patel.jay.exmanager.Expense.Find.DateFindActivity.setRv;
import static patel.jay.exmanager.Expense.Transaction.TransActivity.setExList;
import static patel.jay.exmanager.Utility.MyConst.error;
import static patel.jay.exmanager.Utility.MyConst.toast;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

/**
 * Created by Jay on 06-08-2017.
 */

public class TransDialog {

    //region Global Declaration
    private int fDD, fMM, fYY, fHH, fMin, DD, MM, YY, WW;
    private long timeInMilies = 0;

    private Spinner spnCategory, spnAccount;
    private TextView tvDate, tvTime;
    private MaterialEditText etAmount;
    private AutoCompleteTextView etDesc;
    private Activity activity;
    //endregion

    public TransDialog(Activity activity) {
        this.activity = activity;
    }

    public TransDialog(Activity activity, long timeInMilies) {
        this(activity);
        this.timeInMilies = timeInMilies;
    }

    private void setCurrentTime(long currentTimeMillis) {
        timeInMilies = currentTimeMillis;
        TimeConvert tc = timeMilies(timeInMilies);
        fYY = tc.getYy();
        fMM = tc.getMm();
        fDD = tc.getDd();
        fHH = tc.getHh24();
        fMin = tc.getMin();

        YY = tc.getYy();
        MM = tc.getMm();
        DD = tc.getDd();
        WW = tc.getWeek();

        tvDate.setText(tc.getDD_MM_YY());
        tvTime.setText(tc.getHH_Min_AP());
    }

    private void setTime() {
        String date = fDD + "-" + (fMM + 1) + "-" + fYY, time = fHH + ":" + fMin;
        TimeConvert tc = timeMilies(date, time);

        timeInMilies = tc.getMyMillies();
        fYY = tc.getYy();
        fMM = tc.getMm();
        fDD = tc.getDd();
        fHH = tc.getHh24();
        fMin = tc.getMin();

        YY = tc.getYy();
        MM = tc.getMm();
        DD = tc.getDd();
        WW = tc.getWeek();

        tvDate.setText(tc.getDD_MM_YY());
        tvTime.setText(tc.getHH_Min_AP());
    }

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

                        String date = fDD + "-" + (fMM + 1) + "-" + fYY,
                                time = fHH + ":" + fMin;

                        timeInMilies = timeMilies(date, time).getMyMillies();
                        setCurrentTime(timeInMilies);
                    }
                }, hour, minute, true).show();
    }
    //endregion

    private View setCustomView(long currentTimeMillis) {
        View view = null;

        try {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.dialog_trans, null);

            tvDate = view.findViewById(R.id.tvDate);
            tvTime = view.findViewById(R.id.tvTime);
            spnAccount = view.findViewById(R.id.spnAcc);
            spnCategory = view.findViewById(R.id.spnCat);
            etDesc = view.findViewById(R.id.etDesc);
            etAmount = view.findViewById(R.id.etAmount);

            if (timeInMilies > 0) {
                currentTimeMillis = timeInMilies;
            }
            setCurrentTime(currentTimeMillis);

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

            ArrayList<String> arrayList = new Account().allAccString(activity);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, arrayList);
            spnAccount.setAdapter(adapter);

            arrayList = new Category().allCatStr(activity);
            adapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, arrayList);
            spnCategory.setAdapter(adapter);

            spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    int acid = new Account().accInt(activity, spnAccount.getSelectedItem().toString());
                    String cName = new Trans(activity).findMaxCid(acid);
                    if (!cName.equals("")) {
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnCategory.getAdapter();
                        spnCategory.setSelection(adapter.getPosition(cName));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            view.findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etDesc.setText("");
                }
            });

            view.findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dsc = etDesc.getText().toString();
                    showDscList(dsc);
                }
            });

            view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fDD <= 31) {
                        fDD++;
                        setTime();
                    }
                }
            });

            view.findViewById(R.id.btnMin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fDD > 0) {
                        fDD--;
                        setTime();
                    }
                }
            });

            Trans e = new Trans(activity);

            etDesc.setThreshold(1);
            etDesc.setTextColor(Color.BLACK);
            etDesc.setVisibility(View.VISIBLE);

            arrayList = e.getDsc();

            adapter = new ArrayAdapter<>(activity, R.layout.spn_row, R.id.tvRow, arrayList);
            etDesc.setAdapter(adapter);

            etDesc.setInputType(etDesc.getInputType() & (~EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void showDscList(String dsc) {
        try {
            ArrayList<String> dscs = new Trans(activity).getDsc();
            final ArrayList<String> arrayList = new ArrayList<>();

            dsc = dsc.toLowerCase();
            for (String str : dscs) {
                if (str.toLowerCase().contains(dsc)) {
                    arrayList.add(str);
                }
            }

            if (arrayList.size() == 0) {
                toast(activity, "No Dsc Found");
                return;
            } else if (arrayList.size() == 1) {
                toast(activity, "No Other Dsc Found");
                return;
            }

            GridLayout layoutSpn = new GridLayout(activity);
            GridView spnView = new GridView(activity);

            AbsListView.LayoutParams spnParam = new AbsListView.LayoutParams(MATCH_PARENT, MATCH_PARENT);

            spnView.setLayoutParams(spnParam);
            spnView.setNumColumns(2);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, arrayList);

            spnView.setAdapter(arrayAdapter);
            layoutSpn.addView(spnView);

            AlertDialog.Builder gridDialog = new AlertDialog.Builder(activity)
                    .setView(layoutSpn)
                    .setCancelable(true)
                    .setTitle("Select Any")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            final AlertDialog dialog = gridDialog.create();
            dialog.show();

            spnView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String str = arrayList.get(position);
                    etDesc.setText(str);
                    dialog.cancel();
                }
            });
        } catch (Exception e) {
            error(activity, e);
        }
    }

    public void alertAddCat() {
        try {
            //region Blank Dialog

            Builder alert = new Builder(activity);
            alert.setCancelable(true);

            alert.setView(setCustomView(currentTimeMillis()));

            alert.setPositiveButton(ok, null)
                    .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
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
                                        int etAmt = parseInt(etAmount.getText().toString());
                                        if (etAmt < 1) {
                                            etAmount.setError("Not Valid 0");
                                        } else {
                                            String dsc = etDesc.getText().toString();
                                            String sCat = spnCategory.getSelectedItem().toString();
                                            String sAcc = spnAccount.getSelectedItem().toString();

                                            int cid = new Category().catId(activity, sCat);
                                            int acid = new Account().accInt(activity, sAcc);
                                            Trans trans = new Trans(etAmt, timeInMilies + "",
                                                    DD, MM, YY, WW, dsc, acid, cid);

                                            long val = trans.insert(activity);
                                            toastMSG(val);

                                            dialog.cancel();
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

            wmlp.gravity = TOP | CENTER;
            wmlp.y = 0;

            //endregion

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void alertEditView(final Trans sqlTrans) {
        try {
            timeInMilies = parseLong(sqlTrans.getTime());

            Builder builder = new Builder(activity);
            builder.setView(setCustomView(timeInMilies));
            builder.setCancelable(true);

            //region Fill Details In Builder
            etAmount.setText(sqlTrans.getEx_rs() + "");

            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnAccount.getAdapter();
            String id = new Account().accString(activity, sqlTrans.getAcid());
            spnAccount.setSelection(adapter.getPosition(id));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnCategory.getAdapter();
                    String id = new Category().catStr(activity, sqlTrans.getCid());
                    spnCategory.setSelection(adapter.getPosition(id));
                }
            }, 500);

            etDesc.setText(sqlTrans.getDescription());
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
                    Button update = alert.getButton(BUTTON_POSITIVE);
                    //region Update Click

                    if (update != null) {
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //region Update
                                int idc = spnCategory.getSelectedItemPosition();
                                int ida = spnAccount.getSelectedItemPosition();
                                if (idc == 0 || ida == 0 || etAmount.getText().toString().isEmpty()) {
                                    if (etAmount.getText().toString().isEmpty()) {
                                        etAmount.setError("Required");
                                    }
                                    if (ida == 0) {
                                        ((TextView) spnAccount.getChildAt(0).findViewById(R.id.tvRow)).setError("Required");
                                    }
                                    if (idc == 0) {
                                        ((TextView) spnCategory.getChildAt(0).findViewById(R.id.tvRow)).setError("Required");
                                    }
                                } else {
                                    String sDsc = etDesc.getText().toString();

                                    int sCat = new Category().catId(activity, spnCategory.getSelectedItem().toString());
                                    int sAcc = new Account().accInt(activity, spnAccount.getSelectedItem().toString());

                                    int etAmt = parseInt(etAmount.getText().toString());
                                    if (etAmt < 1) {
                                        etAmount.setError("Not Valid 0");
                                    } else {
                                        Trans trans1 = new Trans(sqlTrans.getAid(), etAmt, timeInMilies + "",
                                                DD, MM, YY, WW, sDsc, sAcc, sCat);

                                        long val = trans1.update(activity);
                                        alert.cancel();
                                        toastMSG(val);
                                    }
                                }
                                //endregion
                            }
                        });
                    }

                    //endregion
                }
            });

            WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();

            wmlp.gravity = TOP | CENTER;
            wmlp.y = 0;

            //endregion

            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toastMSG(long val) {
        if (val > 0) {
            if (activity instanceof DateFindActivity) {
                setRv(activity);
            } else if (activity instanceof CatFindActivity) {
                CatFindActivity.setRv(activity);
            } else if (activity instanceof MonFindActivity) {
                MonFindActivity.setRv(activity);
            } else if (activity instanceof CalenderActivity) {
                setViewPager((AppCompatActivity) activity);
            } else if (activity instanceof TransActivity) {
                setExList(activity);
            }
        } else {
            toast(activity, "Failed");
        }
    }

}
