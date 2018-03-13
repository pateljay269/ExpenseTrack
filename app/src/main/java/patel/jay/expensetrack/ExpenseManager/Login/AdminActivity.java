package patel.jay.expensetrack.ExpenseManager.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlUser;
import patel.jay.expensetrack.R;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spnUsers;
    Button btnFind, btnUpdate;
    LinearLayout linearUpdate;

    EditText etPin, etUser, etPass, etAcNo;

    ArrayList<String> custString;
    ArrayAdapter<String> adapter;
    SqlUser sqlUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setTitle("Admin Dashboard");

        linearUpdate = (LinearLayout) findViewById(R.id.linearUpdate);
        spnUsers = (Spinner) findViewById(R.id.spnUsers);

        etUser = (EditText) findViewById(R.id.etUser);
        etPin = (EditText) findViewById(R.id.etPin);
        etPass = (EditText) findViewById(R.id.etPass);
        etAcNo = (EditText) findViewById(R.id.etAcNo);

        btnFind = (Button) findViewById(R.id.btnFind);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        linearUpdate.setVisibility(View.GONE);

        custString = SqlUser.allUserString(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, custString);
        spnUsers.setAdapter(adapter);

        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPass.setText(etPin.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.logout));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_add) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
        switch (v.getId()) {
            case R.id.btnUpdate:
                updateNewInfo();
                break;

            case R.id.btnFind:
                int idd = spnUsers.getSelectedItemPosition();
                if (spnUsers.getSelectedItem().toString().equals(MyConstants.ADMIN)) {
                    etUser.setEnabled(false);
                    etPass.setEnabled(false);
                } else {
                    etUser.setEnabled(true);
                    etPass.setEnabled(true);
                }
                if (idd > 0) {
                    linearUpdate.setVisibility(View.VISIBLE);
                    sqlUser = SqlUser.findDetail(this, spnUsers.getSelectedItem().toString());
                    etUser.setText(sqlUser.getUser());
                    etPass.setText(sqlUser.getPass());
                    etAcNo.setText(sqlUser.getAcNo());
                    etPin.setText(sqlUser.getPin() + "");
                } else {
                    ((TextView) spnUsers.getSelectedView()).setError("Required");
                }
                break;
        }
    }

    private void updateNewInfo() {
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();
        String acNo = etAcNo.getText().toString();
        String pinCode = etPin.getText().toString();
        if (user.isEmpty() || pass.isEmpty() || pinCode.isEmpty() || acNo.isEmpty()) {
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                etUser.setError("Required");
            }
            if (TextUtils.isEmpty(etPass.getText().toString())) {
                etPass.setError("Required");
            }

            if (TextUtils.isEmpty(etAcNo.getText().toString())) {
                etAcNo.setError("Required");
            }
            if (TextUtils.isEmpty(etPin.getText().toString())) {
                etPin.setError("Required");
            }
        } else {
            SqlUser sqlUserN = new SqlUser(sqlUser.getUid(), user, pass, Integer.parseInt(pinCode), acNo);
            long val = 0;
            val = sqlUserN.update(this);

            if (val > 0) {
                linearUpdate.setVisibility(View.GONE);
                MyConstants.toast(this, "Updated");
            } else {
                MyConstants.toast(this, "Try Another Details");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyConstants.back2Login(this);
    }
}
