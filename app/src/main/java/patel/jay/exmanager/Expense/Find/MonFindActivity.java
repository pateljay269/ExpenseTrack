package patel.jay.exmanager.Expense.Find;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Collections;

import patel.jay.exmanager.Adapter.TransAdapter;
import patel.jay.exmanager.Expense.Transaction.TransDialog;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.SQL;
import patel.jay.exmanager.SQL.Trans;
import patel.jay.exmanager.Utility.TimeConvert;

import static android.R.layout.simple_list_item_1;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.gc;
import static patel.jay.exmanager.SQL.SQL.COL_AID;
import static patel.jay.exmanager.SQL.SQL.COL_DATE;
import static patel.jay.exmanager.SQL.SQL.COL_MONTH;
import static patel.jay.exmanager.SQL.SQL.COL_YEAR;
import static patel.jay.exmanager.SQL.SQL.TBL_INOUTCOME;
import static patel.jay.exmanager.Utility.MyConst.Total;
import static patel.jay.exmanager.Utility.MyConst.error;
import static patel.jay.exmanager.Utility.MyConst.hideKey;
import static patel.jay.exmanager.Utility.MyConst.toast;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

public class MonFindActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static ArrayList<Trans> accList = new ArrayList<>(), tempList = accList;
    private static String sTypeOrd = "Date", sOrd = "", sYear = "", sMonth = "", sDate = "";
    private static AutoCompleteTextView actFind;
    private static RecyclerView rvDetail;

    Activity activity = MonFindActivity.this;

    LinearLayout linearLayout;
    SwitchButton sbtnTypeOrd, sbtnOrder;
    Spinner spnMonth, spnYear, spnDate;

    Trans et = new Trans(activity);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Object obj : all) {
            obj = null;
        }

        gc();
        all = null;
    }

    private Object[] all = new Object[]{accList, tempList, actFind, rvDetail};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accList = new ArrayList<>();
        tempList = accList;

        setContentView(R.layout.activity_cat_find);
        setTitle("Category Wise Find");

        spnMonth = findViewById(R.id.spnCatFind);
        spnYear = findViewById(R.id.spnAccFind);
        spnDate = findViewById(R.id.spnDate);
        spnDate.setVisibility(VISIBLE);

        sbtnTypeOrd = findViewById(R.id.sbtnType);
        sbtnOrder = findViewById(R.id.sbtnOrder);

        findViewById(R.id.btnFind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRv(activity);
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actFind.setText("");
                hideKey(activity);
            }
        });

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dsc = actFind.getText().toString();
                showDscList(dsc);
            }
        });

        linearLayout = findViewById(R.id.linearFind);
        actFind = findViewById(R.id.actFind);
        actFind.setOnItemClickListener(this);

        rvDetail = findViewById(R.id.recyclerView);

        accList = new ArrayList<>();

        findViewById(R.id.fab).setVisibility(VISIBLE);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region SetDate
                TimeConvert tc = timeMilies(currentTimeMillis());
                String date = "", time = tc.getHh24() + ":" + tc.getMin();

                int yy = tc.getYy(), mm = tc.getMm(), dd = tc.getDd();
                try {
                    if (spnPos(spnYear)) {
                        yy = Integer.parseInt(sYear);
                    }

                    if (spnPos(spnMonth)) {
                        mm = new TimeConvert().monthNum(sMonth);
                    }

                    if (spnPos(spnDate)) {
                        dd = Integer.parseInt(sDate);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    SQL helper = new SQL(activity);
                    SQLiteDatabase db = helper.getReadableDatabase();

                    String selection = "select " + COL_DATE + " from " + TBL_INOUTCOME + " where "
                            + COL_YEAR + "=" + yy + " and "
                            + COL_MONTH + "=" + mm + " "
                            + "order by " + COL_AID + " DESC limit 1";

                    Cursor cursor = db.rawQuery(selection, null);
                    if (cursor.moveToFirst()) {
                        dd = cursor.getInt(cursor.getColumnIndex(COL_DATE));
                    }
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    error(activity, e);
                }

                date = dd + "-" + (mm + 1) + "-" + yy;
                tc = timeMilies(date, time);

                TransDialog transDialog = new TransDialog(activity, tc.getMyMillies());
                transDialog.alertAddCat();
                //endregion
            }
        });

        sTypeOrd = "Date";
        sOrd = "";
        sYear = "";
        sMonth = "";

        sbtnTypeOrd.setText("Amount", "Date");
        sbtnOrder.setText("DESC", "ASC");

        sbtnTypeOrd.setOnCheckedChangeListener(this);
        sbtnOrder.setOnCheckedChangeListener(this);

        ArrayList<String> monList = new ArrayList<>();
        monList.add(COL_MONTH);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, simple_list_item_1, monList);
        spnMonth.setAdapter(adapter);

        spnYear.setOnItemSelectedListener(this);
        spnMonth.setOnItemSelectedListener(this);
        spnDate.setOnItemSelectedListener(this);

        actFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String cName = s + "";
                if (cName.isEmpty()) {
                    accList = tempList;
                } else {
                    accList = new ArrayList<>();
                    for (Trans sqlAccount : tempList) {
                        if (sqlAccount.getDescription().contains(cName)
                                || (sqlAccount.getEx_rs() + "/-").contains(cName)) {
                            accList.add(sqlAccount);
                        }
                    }
                }

                rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
                TransAdapter adapter = new TransAdapter(accList, activity, true);
                rvDetail.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sbtnOrder.setChecked(true);
    }

    private boolean spnPos(Spinner spn) {
        return spn.getSelectedItemPosition() > 0;
    }

    public static void setRv(Activity activity) {
        try {
            Trans e = new Trans(activity);

            String selection = "";
            int mm = new TimeConvert().monthNum(sMonth);

            if (sYear.equals(COL_YEAR) && sMonth.equals(COL_MONTH) && sDate.equals(COL_DATE)) {
                selection = "";

            } else if (sYear.equals(COL_YEAR) && sMonth.equals(COL_MONTH) && !sDate.equals(COL_DATE)) {
                selection = COL_DATE + "='" + sDate + "'";

            } else if (sYear.equals(COL_YEAR) && !sMonth.equals(COL_MONTH) && !sDate.equals(COL_DATE)) {
                selection = COL_DATE + "='" + sDate + "' And " + COL_MONTH + "='" + mm + "'";

            } else if (sYear.equals(COL_YEAR) && !sMonth.equals(COL_MONTH) && sDate.equals(COL_DATE)) {
                selection = COL_MONTH + "='" + mm + "'";

            } else if (!sYear.equals(COL_YEAR) && sMonth.equals(COL_MONTH) && !sDate.equals(COL_DATE)) {
                selection = COL_DATE + "='" + sDate + "' And " + COL_YEAR + "='" + sYear + "'";

            } else if (!sYear.equals(COL_YEAR) && sMonth.equals(COL_MONTH) && sDate.equals(COL_DATE)) {
                selection = COL_YEAR + "='" + sYear + "'";

            } else if (!sYear.equals(COL_YEAR) && !sMonth.equals(COL_MONTH) && sDate.equals(COL_DATE)) {
                selection = COL_YEAR + "='" + sYear + "' And " + COL_MONTH + "='" + mm + "'";

            } else {
                selection = COL_YEAR + "='" + sYear + "' And " + COL_MONTH + "='" + mm + "' And " + COL_DATE + "='" + sDate + "'";

            }

            accList = e.getList(selection, sTypeOrd, sOrd);

            rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
            TransAdapter adapter = new TransAdapter(accList, activity, true);
            rvDetail.setAdapter(adapter);
            tempList = accList;

            actFind = e.getDscAdapter(activity, adapter, actFind);

            int ex = 0;
            String text = "";
            for (Trans trans : accList) {
                ex += trans.getEx_rs();
            }
            text = Total + " : " + ex;
            ((TextView) activity.findViewById(R.id.tvTotal)).setText(text);

            hideKey(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trans_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            finish();
        } else if (id == R.id.action_show) {
            if (linearLayout.getVisibility() == GONE) {
                linearLayout.setVisibility(VISIBLE);
            } else {
                linearLayout.setVisibility(GONE);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<String> yrList = et.yearArray(), tempList = new ArrayList<>();
        yrList.remove(0);
        Collections.reverse(yrList);

        tempList.add(COL_YEAR);
        tempList.addAll(yrList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, simple_list_item_1, tempList);
        spnYear.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        accList.clear();
        TransAdapter adapter = new TransAdapter(accList, activity);
        rvDetail.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        actFind.setText("");
        switch (buttonView.getId()) {
            case R.id.sbtnType:
                if (isChecked) {
                    sTypeOrd = "Amount";
                } else {
                    sTypeOrd = "Date";
                }
                break;

            case R.id.sbtnOrder:
                if (isChecked) {
                    sOrd = "DESC";
                } else {
                    sOrd = "";
                }
                break;
        }
        setRv(activity);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        actFind.setText("");
        try {
            switch (parent.getId()) {
                case R.id.spnCatFind:
                    sMonth = spnMonth.getSelectedItem().toString();
                    fillDate();
                    break;

                case R.id.spnAccFind:
                    sYear = spnYear.getSelectedItem().toString();
                    fillMon();
                    break;

                case R.id.spnDate:
                    sDate = spnDate.getSelectedItem().toString();
                    break;
            }

            hideKey(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillMon() {
        ArrayList<String> monList = et.monthYearArray(sYear),
                tempList = new ArrayList<>();
        monList.remove(0);
        Collections.reverse(monList);

        tempList.add(COL_MONTH);
        tempList.addAll(monList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, simple_list_item_1, tempList);
        spnMonth.setAdapter(adapter);

        fillDate();
    }

    private void fillDate() {
        int mm = new TimeConvert().monthNum(sMonth);

        ArrayList<String> dateList = new ArrayList<>(), temp;

        if (!sYear.equals(COL_YEAR)) {
            temp = et.dayMonthArray(mm, Integer.parseInt(sYear));
        } else {
            temp = et.dayMonthArray(mm, 0);
        }
        dateList.add(COL_DATE);
        dateList.addAll(temp);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, simple_list_item_1, dateList);
        spnDate.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideKey(this);
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
                    actFind.setText(str);
                    dialog.cancel();
                }
            });
        } catch (Exception e) {
            error(activity, e);
        }
    }
}