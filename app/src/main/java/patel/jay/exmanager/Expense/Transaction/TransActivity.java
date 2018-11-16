package patel.jay.exmanager.Expense.Transaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Collections;

import patel.jay.exmanager.Adapter.TransAdapter;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Trans;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static java.lang.System.gc;
import static patel.jay.exmanager.Utility.MyConst.hideKey;

public class TransActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Activity activity = TransActivity.this;

    private static boolean isDsc;
    private static RecyclerView rvCat;
    private static AutoCompleteTextView actFind;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Object obj : all) {
            obj = null;
        }

        gc();
        all = null;
    }

    private Object[] all = new Object[]{activity, rvCat, actFind};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);

        setTitle("Transaction");

        rvCat = findViewById(R.id.recyclerView);
        findViewById(R.id.fab).setOnClickListener(this);

        actFind = findViewById(R.id.actFind);
        actFind.addTextChangedListener(textWatcher);
        actFind.setOnItemClickListener(this);

        findViewById(R.id.btnClear).setVisibility(View.VISIBLE);
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actFind.setText("");
                hideKey(activity);
            }
        });

        isDsc = false;
        setExList(activity);
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
        switch (id) {
            case 0:
            case R.id.action_show:
                isDsc = !isDsc;
                setExList(activity);
                break;

            case R.id.action_add:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TransAdapter adapter = (TransAdapter) rvCat.getAdapter();
            adapter = new TransAdapter(addTextExTrans(s, adapter.getTransList()), activity);

            rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
            rvCat.setAdapter(adapter);
            rvCat.scrollToPosition(adapter.getTransList().size() - 1);

            if (s.toString().equals("")) {
                setExList(activity);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    public ArrayList<Trans> addTextExTrans(CharSequence s, ArrayList<Trans> accList) {

        try {
            ArrayList<Trans> tempArrayList = accList;
            String cName = s + "";
            if (!cName.isEmpty()) {
                accList = new ArrayList<>();
                for (Trans sqlAccount : tempArrayList) {
                    if (sqlAccount.getDescription().contains(cName) ||
                            (sqlAccount.getEx_rs() + "/-").contains(cName)) {
                        accList.add(sqlAccount);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accList;
    }

    public static void setExList(Activity context) {
        Trans ex = new Trans(context);

        ArrayList<Trans> accList = ex.allCatTrans();

        if (accList.size() < 10) {
            accList = ex.lastTrans();
            Collections.reverse(accList);
        }

        rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
        TransAdapter adapter = new TransAdapter(accList, context, isDsc);
        rvCat.setAdapter(adapter);
        rvCat.scrollToPosition(adapter.getTransList().size() - 1);

        actFind = ex.getDscAdapter(context, adapter, actFind);
    }

    @Override
    public void onClick(View v) {
        TransDialog transDialog = new TransDialog(activity);
        transDialog.alertAddCat();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideKey(activity);
    }
}