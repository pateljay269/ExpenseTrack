package patel.jay.exmanager.Expense.Find;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import patel.jay.exmanager.Adapter.TransAdapter;
import patel.jay.exmanager.Expense.Transaction.TransDialog;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Account;
import patel.jay.exmanager.SQL.Category;
import patel.jay.exmanager.SQL.Trans;

import static android.R.layout.simple_list_item_1;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static java.lang.System.gc;
import static patel.jay.exmanager.SQL.SQL.CAT_AC;
import static patel.jay.exmanager.SQL.SQL.COL_ACID;
import static patel.jay.exmanager.SQL.SQL.COL_CID;
import static patel.jay.exmanager.Utility.MyConst.Total;
import static patel.jay.exmanager.Utility.MyConst.error;
import static patel.jay.exmanager.Utility.MyConst.hideKey;
import static patel.jay.exmanager.Utility.MyConst.toast;

public class CatFindActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private static ArrayList<Trans> accList = new ArrayList<>(), tempList = accList;
    private static String sTypeOrd = "Date", sOrd = "", accNum = "", catNum = "";
    private static AutoCompleteTextView actFind;
    private static RecyclerView rvDetail;

    Activity activity = CatFindActivity.this;

    LinearLayout linearLayout;
    SwitchButton sbtnTypeOrd, sbtnOrder;
    Spinner spnCategory, spnAccount;

    ArrayList<String> catString, accString;
    ArrayAdapter<String> catAdapter, accAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Object obj : all) {
            obj = null;
        }

        all = null;
        gc();
    }

    private Object[] all = new Object[]{actFind, accList, tempList, rvDetail};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accList = new ArrayList<>();
        tempList = accList;

        sTypeOrd = "Date";
        sOrd = "";
        accNum = "";
        catNum = "";

        setContentView(R.layout.activity_cat_find);
        setTitle("Category Wise Find");

        spnCategory = findViewById(R.id.spnCatFind);
        spnAccount = findViewById(R.id.spnAccFind);

        sbtnTypeOrd = findViewById(R.id.sbtnType);
        sbtnOrder = findViewById(R.id.sbtnOrder);

        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actFind.setText("");
                hideKey(activity);
            }
        });

        findViewById(R.id.btnFind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRv(activity);
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

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransDialog transDialog = new TransDialog(activity);
                transDialog.alertAddCat();
            }
        });

        sTypeOrd = "Date";
        sOrd = "";
        accNum = "";
        catNum = "";

        sbtnTypeOrd.setText("Amount", "Date");
        sbtnOrder.setText("DESC", "ASC");

        sbtnTypeOrd.setOnCheckedChangeListener(this);
        sbtnOrder.setOnCheckedChangeListener(this);

        spnAccount.setOnItemSelectedListener(this);
        spnCategory.setOnItemSelectedListener(this);

        catString = new Category().allCatStr(activity, accNum);

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

    }

    public static void setRv(Activity activity) {
        try {
            String COL_CATYPE = "Category";

            Trans e = new Trans(activity);
            String selection = "";
            int cid = new Category().catId(activity, catNum);
            int acid = new Account().accInt(activity, accNum);

            if (accNum.equals(CAT_AC) && catNum.equals(COL_CATYPE)) {
                selection = "";

            } else if (accNum.equals(CAT_AC) && !catNum.equals(COL_CATYPE)) {
                selection = COL_CID + "='" + cid + "'";

            } else if (catNum.equals(COL_CATYPE)) {
                selection = COL_ACID + "='" + acid + "'";

            } else {
                selection = COL_CID + "='" + cid + "' And " + COL_ACID + "='" + acid + "'";

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
        } catch (Exception e) {
            e.printStackTrace();
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

        accString = new Account().allAccString(activity);
        accString.set(0, CAT_AC);
//        yrList.remove(0);

        accAdapter = new ArrayAdapter<>(activity, simple_list_item_1, accString);
        spnAccount.setAdapter(accAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        accList.clear();
        TransAdapter adapter = new TransAdapter(accList, activity);
        rvDetail.setAdapter(adapter);
    }

    private void fillCat(int position) {
        if (position != 0) {
            catString = new Category().allCatStr(activity, accNum);
        } else {
            catString = new Category().allCatStr(activity);
        }

        String COL_CATYPE = "Category";
        catString.set(0, COL_CATYPE);

        catAdapter = new ArrayAdapter<>(activity, simple_list_item_1, catString);
        spnCategory.setAdapter(catAdapter);

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
                    catNum = catString.get(position);
                    break;

                case R.id.spnAccFind:
                    accNum = accString.get(position);
                    fillCat(position);
                    break;
            }

            hideKey(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
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