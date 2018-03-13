package patel.jay.expensetrack.ExpenseManager.Find;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ExpenseManager.Adapters.ExTransAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

public class ExCatFindActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    static RecyclerView rvDetail;
    static ArrayList<SqlExTrans> accountArrayList;
    static ArrayList<SqlExTrans> tempArrayList;
    static ExTransAdapter accountAdapter;
    static String sCatType, sOrder = "", accNum = "", catNum = "";
    static TextView tvTotal;
    EditText etFind;
    LinearLayout linearLayout;
    SwitchButton sbtnCaType, sbtnOrder;
    Spinner spnCategory, spnAccount;
    FloatingActionButton fab;
    ArrayList<String> catString, accString;
    ArrayAdapter<String> catAdapter, accAdapter;

    public static void setRv(Activity activity) {
        if (accNum.equalsIgnoreCase(SQLiteHelper.CAT_AC) && catNum.equalsIgnoreCase(SQLiteHelper.COL_CATYPE)) {
            accountArrayList = SqlExTrans.allAcc(activity, sCatType, sOrder);
        } else if (accNum.equalsIgnoreCase(SQLiteHelper.CAT_AC) && !catNum.equalsIgnoreCase(SQLiteHelper.COL_CATYPE)) {
            accountArrayList = SqlExTrans.allCat(activity, catNum, sCatType, sOrder);
        } else if (catNum.equalsIgnoreCase(SQLiteHelper.COL_CATYPE)) {
            accountArrayList = SqlExTrans.allAcc(activity, accNum, sCatType, sOrder);
        } else {
            accountArrayList = SqlExTrans.allAccCat(activity, accNum, catNum, sOrder);
        }

        rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        accountAdapter = new ExTransAdapter(accountArrayList, activity, true);
        rvDetail.setAdapter(accountAdapter);
        tempArrayList = accountArrayList;

        tvTotal.setText(MyConstants.calExTotal(sCatType, accountArrayList));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_find);
        setTitle("Category Wise Find");

        spnCategory = (Spinner) findViewById(R.id.spnCatFind);
        spnAccount = (Spinner) findViewById(R.id.spnAccFind);

        sbtnCaType = (SwitchButton) findViewById(R.id.sbtnCaType);
        sbtnOrder = (SwitchButton) findViewById(R.id.sbtnOrder);

        linearLayout = (LinearLayout) findViewById(R.id.linearFind);
        etFind = (EditText) findViewById(R.id.etFind);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        rvDetail = (RecyclerView) findViewById(R.id.recyclerView);

//        rvDetail.setLayoutManager(new LinearLayoutManager(this));
        accountArrayList = new ArrayList<>();

//        fab.setImageResource(R.drawable.ic_find);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyConstants.AddExpense(ExCatFindActivity.this);
            }
        });

        sCatType = SQLiteHelper.CAT_EX;
        sbtnOrder.setText("DESC", "ASC");

        sbtnCaType.setText(SQLiteHelper.CAT_IN, SQLiteHelper.CAT_EX);

        sbtnCaType.setOnCheckedChangeListener(this);
        sbtnOrder.setOnCheckedChangeListener(this);

        //region TextWatcher

        etFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //region TextChangedArrayList
                try {
                    String cName = etFind.getText().toString();
                    if (cName.isEmpty()) {
                        accountArrayList = tempArrayList;
                    } else {
                        //region Search
                        accountArrayList = new ArrayList<>();
                        for (SqlExTrans sqlAccount : tempArrayList) {
                            if (sqlAccount.getDescription().contains(cName)) {
                                accountArrayList.add(sqlAccount);
                            }
                        }
                        //endregion
                    }
                    rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    accountAdapter = new ExTransAdapter(accountArrayList, ExCatFindActivity.this);
                    rvDetail.setAdapter(accountAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //endregion
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion

    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trans_action, menu);
        return true;
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_add) {
                finish();
//                MyConstants.goToHomeExpense(this);
                return true;
            } else if (id == R.id.action_show) {
                if (linearLayout.getVisibility() == View.GONE) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillAcc();
    }

    private void fillAcc() {
        try {
            accString = SqlExAccount.allAccString(this);
            accString.set(0, SQLiteHelper.CAT_AC);

            accAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, accString);
            spnAccount.setAdapter(accAdapter);
//        spnAccount.setSelection(1);

            spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        accNum = accString.get(position);
                        fillCat(position);

                        setRv(ExCatFindActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillCat(int position) {
        try {
            if (position != 0) {
                catString = SqlExCategory.allCategoryString(ExCatFindActivity.this, sCatType, accNum);
            } else {
                catString = SqlExCategory.allCategoryString(ExCatFindActivity.this, sCatType);
            }

            catString.set(0, SQLiteHelper.COL_CATYPE);

            catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, catString);
            spnCategory.setAdapter(catAdapter);
//        spnCategory.setSelection(1);

            spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        catNum = catString.get(position);
                        setRv(ExCatFindActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //SwitchButton Event
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sbtnOrder:
                if (isChecked) {
                    sOrder = "DESC";
                } else {
                    sOrder = "";
                }
                setRv(ExCatFindActivity.this);
                break;

            case R.id.sbtnCaType:
                if (isChecked) {
                    sCatType = SQLiteHelper.CAT_IN;
                } else {
                    sCatType = SQLiteHelper.CAT_EX;
                }
                fillCat(0);
                break;
        }
    }

}