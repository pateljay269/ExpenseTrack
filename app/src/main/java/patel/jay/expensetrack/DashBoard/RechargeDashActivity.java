package patel.jay.expensetrack.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import patel.jay.expensetrack.Calender.CalenderActivity;
import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.FindTotal.PieChartActivity;
import patel.jay.expensetrack.RechargeManager.Recharge.RechargeActivity;

public class RechargeDashActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnCalender, btnRecharge, btnPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_dash);

        btnRecharge = (ImageButton) findViewById(R.id.btnRecharge);
        btnCalender = (ImageButton) findViewById(R.id.btnCalender);
        btnPie = (ImageButton) findViewById(R.id.btnPie);

        btnRecharge.setOnClickListener(this);
        btnCalender.setOnClickListener(this);
        btnPie.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        MyConstants.DASH_CLICK = MyConstants.RECHARGE;

        switch (v.getId()) {
            case R.id.btnRecharge:
                startActivity(new Intent(getApplicationContext(), RechargeActivity.class));
                break;

            case R.id.btnCalender:
                startActivity(new Intent(getApplicationContext(), CalenderActivity.class));
                break;

            case R.id.btnPie:
                startActivity(new Intent(getApplicationContext(), PieChartActivity.class));
                break;
        }
    }

}