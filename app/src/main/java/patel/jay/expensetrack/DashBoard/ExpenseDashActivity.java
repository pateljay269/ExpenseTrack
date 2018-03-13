package patel.jay.expensetrack.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import patel.jay.expensetrack.Calender.CalenderActivity;
import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ExpenseManager.AllTransaction.ExTransactionActivity;
import patel.jay.expensetrack.ExpenseManager.Category.CategoryActivity;
import patel.jay.expensetrack.ExpenseManager.Find.ExCatFindActivity;
import patel.jay.expensetrack.ExpenseManager.Find.ExDateFindActivity;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.FindTotal.PieChartActivity;

public class ExpenseDashActivity extends AppCompatActivity implements View.OnClickListener {

    PopupMenu popup;
    ImageButton btnTrans, btnCalender, btnAcc, btnFind, btnPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        setTitle("Dashboard");

        btnTrans = (ImageButton) findViewById(R.id.btnTrans);
        btnAcc = (ImageButton) findViewById(R.id.btnAcc);
        btnCalender = (ImageButton) findViewById(R.id.btnCalender);
        btnFind = (ImageButton) findViewById(R.id.btnFind);
        btnPie = (ImageButton) findViewById(R.id.btnPie);

        btnTrans.setOnClickListener(this);
        btnAcc.setOnClickListener(this);
        btnCalender.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnPie.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        MyConstants.DASH_CLICK = MyConstants.EXPENSE;

        switch (v.getId()) {
            case R.id.btnAcc:
                startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                break;

            case R.id.btnCalender:
                startActivity(new Intent(this, CalenderActivity.class));
                break;

            case R.id.btnTrans:
                startActivity(new Intent(getApplicationContext(), ExTransactionActivity.class));
                break;

            case R.id.btnPie:
                startActivity(new Intent(getApplicationContext(), PieChartActivity.class));
                break;

            case R.id.btnFind:
                popup = new PopupMenu(ExpenseDashActivity.this, btnFind);
                popup.getMenuInflater().inflate(R.menu.popup_find, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cat:
                                startActivity(new Intent(getApplicationContext(), ExCatFindActivity.class));
                                break;

                            case R.id.action_date:
                                startActivity(new Intent(getApplicationContext(), ExDateFindActivity.class));
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
                break;
        }
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_home) {
                finish();
//                startActivity(new Intent(getApplicationContext(), HomeDashActivity.class));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

}
