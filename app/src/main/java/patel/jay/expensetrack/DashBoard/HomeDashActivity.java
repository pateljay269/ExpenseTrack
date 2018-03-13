package patel.jay.expensetrack.DashBoard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.ExportBalData;
import patel.jay.expensetrack.ConstClass.ExportExData;
import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

public class HomeDashActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int STORAGE_PERMISSION_CODE = 101, FILE_SELECT_CODE = 102;
    TextView tvPath;
    ImageButton btnBalance, btnRecharge, btnExpense, btnExport;
    Activity activity = HomeDashActivity.this;
    //region Grid View For Year
    GridView spnView;
    GridLayout layoutSpn;
    GridView.LayoutParams spnParam;
    AlertDialog gridDialog;
    ArrayList<String> arrayList;
    private int check = 0;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dash);
        setTitle("Home");
        MyConstants.DASH_CLICK = "";

        tvPath = findViewById(R.id.tvPath);
        tvPath.setText("");

        btnBalance = findViewById(R.id.btnBalance);
        btnRecharge = findViewById(R.id.btnRecharge);
        btnExpense = findViewById(R.id.btnExpense);
        btnExport = findViewById(R.id.btnExport);

        btnRecharge.setOnClickListener(this);
        btnBalance.setOnClickListener(this);
        btnExpense.setOnClickListener(this);
//        btnExport.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBalance:
                MyConstants.DASH_CLICK = MyConstants.BAL;
                startActivity(new Intent(getApplicationContext(), BalanceDashActivity.class));
                break;

            case R.id.btnRecharge:
                MyConstants.DASH_CLICK = MyConstants.RECHARGE;
                startActivity(new Intent(getApplicationContext(), RechargeDashActivity.class));
                break;

            case R.id.btnExpense:
                MyConstants.DASH_CLICK = MyConstants.EXPENSE;
                startActivity(new Intent(getApplicationContext(), ExpenseDashActivity.class));
                break;

            case R.id.btnExport:
                break;
        }
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_logout:
//                    MyConstants.back2Login(this);
                    MyConstants.backClick(this);
                    break;

                case R.id.action_info:
                    alertView();
                    break;

                case R.id.action_import:
                    check = 1;
                    requestStoragePermission();
                    break;

                case R.id.action_exCur:
                    check = 2;
                    requestStoragePermission();
                    break;

                case R.id.action_exYear:
                    check = 3;
                    requestStoragePermission();
                    break;

                case R.id.action_exAll:
                    check = 4;
                    requestStoragePermission();
                    break;

                case R.id.action_exDb:
                    check = 5;
                    requestStoragePermission();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region Export

    private void checkTest() {

        switch (check) {
            case 1:
                showFileChooser();
                break;

            case 2:
                TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
                exportExCurrent(tc.getYy());
                exportTotal();
                exportRM();
                break;

            case 3:
                alertYears();
                break;

            case 4:
                exportData();
                break;

            case 5:
                exportDb();
                break;
        }
//        check = 0;
    }

    private void exportData() {
        check = 0;

        exportRM();
        exportExAll();
        exportDb();
        exportTotal();
    }

    private void exportTotal() {
        try {
            ExportBalData tot = new ExportBalData(activity, MyConstants.Total);
            tot.write();
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(activity, e.getMessage());
        }
    }

    private void exportDb() {
        //.db File Backup
        MyConstants.createBackup(this);

        TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
        tvPath.setText(tc.getFile_Path());
    }

    private void exportRM() {
        try {
            //Recharge BackUp
            ExportBalData eBal = new ExportBalData(this, MyConstants.BAL);
            eBal.write();
            ExportBalData eRec = new ExportBalData(this, MyConstants.RECHARGE);
            eRec.write();
            TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
            tvPath.setText(tc.getFile_Path());
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(this, e.getMessage());
        }

    }

    private void exportExAll() {
        //Expense BackUp
        try {
            ArrayList<String> years = SqlExTrans.yearArray(this);
            years.remove(0);

            for (String year : years) {
                int yr = Integer.parseInt(year);
                ExportExData mon = new ExportExData(this, MyConstants.Month, yr);
                mon.write();

                ExportExData acc = new ExportExData(this, MyConstants.Account, yr);
                acc.write();
            }
            TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
            tvPath.setText(tc.getFile_Path());
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(this, e.getMessage());
        }

    }

    //endregion

    //region ImportData

    private void showFileChooser() {
        try {
            check = 0;

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            startActivityForResult(Intent.createChooser(intent, "Select a File"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Install File Manager.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    private void exportExCurrent(int year) {
        //Expense Current Year BackUp
        try {
            ExportExData mon = new ExportExData(this, MyConstants.Month, year);
            mon.write();

            ExportExData acc = new ExportExData(this, MyConstants.Account, year);
            acc.write();

            TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
            tvPath.setText(tc.getFile_Path());
            exportDb();
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(this, e.getMessage());
        }

    }

    //region Request Permissions
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            checkTest();
            return;
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Checking the request code of our request
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                //If permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkTest();
                } else {
                    finish();
                    //Displaying another toast if permission is not granted
                    MyConstants.toast(this, "Give Permission From Settings");
                }
                break;
        }
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case FILE_SELECT_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        MyConstants.copyFile(new File(uri.getPath()), new File(this.getDatabasePath(SQLiteHelper.DBNAME).toString()));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Info Data
    private void alertView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(" If You Want To Any Changes Contact Me.\n"
                + "\n Email :- " + MyConstants.EMAILID + " \n");

        builder.setTitle("Contact Us")
                .setCancelable(true)
                .setNeutralButton("Follow On FB", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MyConstants.F_URL));
                        startActivity(browserIntent);
                    }
                })
                .setPositiveButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void sendEmail() {
        String[] TO = {MyConstants.EMAILID};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(HomeDashActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private GridLayout categoryDialog() {
        try {
            //region Category
            spnView = new GridView(this);
            layoutSpn = new GridLayout(this);
            spnParam = new GridView.LayoutParams(
                    GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.MATCH_PARENT);

            spnView.setLayoutParams(spnParam);
            spnView.setNumColumns(2);

            arrayList = SqlExTrans.yearArray(this);
            arrayList.remove(0);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

            spnView.setAdapter(arrayAdapter);
            layoutSpn.addView(spnView);

            //endregion
        } catch (Exception e) {
            e.printStackTrace();
        }

        return layoutSpn;
    }

    private void alertYears() {
        gridDialog = new AlertDialog.Builder(this)
                .setView(categoryDialog())
                .setTitle("Select Any")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        gridDialog.show();

        spnView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    int yr = Integer.parseInt(arrayList.get(position));
                    exportExCurrent(yr);
                } catch (Exception e) {
                    MyConstants.toast(activity, e.getMessage());
                }
                gridDialog.dismiss();
            }
        });
    }

    //endregion

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        MyConstants.back2Login(this);
        MyConstants.backClick(this);
    }

}
