package patel.jay.exmanager.Expense;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import patel.jay.exmanager.Adapter.RepoAdapter;
import patel.jay.exmanager.Calender.CalenderActivity;
import patel.jay.exmanager.Expense.Category.CategoryActivity;
import patel.jay.exmanager.Expense.Find.CatFindActivity;
import patel.jay.exmanager.Expense.Find.DateFindActivity;
import patel.jay.exmanager.Expense.Find.MonFindActivity;
import patel.jay.exmanager.Expense.Transaction.TransActivity;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Repo;
import patel.jay.exmanager.SQL.Trans;
import patel.jay.exmanager.Utility.CompressFiles;
import patel.jay.exmanager.Utility.Export.AccExport;
import patel.jay.exmanager.Utility.Export.MonExport;
import patel.jay.exmanager.Utility.Export.onExport;
import patel.jay.exmanager.Utility.TimeConvert;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.CATEGORY_OPENABLE;
import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.createChooser;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.net.Uri.parse;
import static android.os.Environment.getExternalStorageDirectory;
import static java.lang.Integer.parseInt;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.gc;
import static java.lang.System.runFinalization;
import static patel.jay.exmanager.Calender.CalenderActivity.isRepo;
import static patel.jay.exmanager.SQL.SQL.DBNAME;
import static patel.jay.exmanager.Utility.CompressFiles.FILE_PATH;
import static patel.jay.exmanager.Utility.CompressFiles.PATH;
import static patel.jay.exmanager.Utility.MyConst.error;
import static patel.jay.exmanager.Utility.MyConst.setPath;
import static patel.jay.exmanager.Utility.MyConst.snack;
import static patel.jay.exmanager.Utility.MyConst.toast;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener, onExport,
        CompressFiles.zipComplete {

    private static final int PERM_STORAGE = 101, PERM_FILE_SELECT = 102;
    Activity activity = DashBoardActivity.this;
    boolean isAccExport = false;
    private int check = 0;

    int counter = 0;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        findViewById(R.id.btnTrans).setOnClickListener(this);
        findViewById(R.id.btnCalender).setOnClickListener(this);
        findViewById(R.id.btnFind).setOnClickListener(this);
        findViewById(R.id.btnAcc).setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gc();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gc();
        System.runFinalization();

//        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Repo> accList = new Repo(activity).getReport();

        RepoAdapter adapter = new RepoAdapter(accList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAcc:
                startActivity(new Intent(activity, CategoryActivity.class));
                break;

            case R.id.btnCalender:
                isRepo = false;
                startActivity(new Intent(activity, CalenderActivity.class));
                break;

            case R.id.btnTrans:
                startActivity(new Intent(activity, TransActivity.class));
                break;

            case R.id.btnFind:
                PopupMenu popup = new PopupMenu(activity, v);
                popup.getMenuInflater().inflate(R.menu.popup_find, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cat:
                                startActivity(new Intent(activity, CatFindActivity.class));
                                break;

                            case R.id.action_date:
                                startActivity(new Intent(activity, DateFindActivity.class));
                                break;

                            case R.id.action_month:
                                startActivity(new Intent(activity, MonFindActivity.class));
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
        getMenuInflater().inflate(R.menu.dash_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            counter = 0;
            int id = item.getItemId();

            switch (id) {
                case R.id.action_info:
                    alertView();
                    break;

                case R.id.action_repo:
                    isRepo = true;
                    startActivity(new Intent(activity, CalenderActivity.class));
                    break;

                case R.id.action_acc_export:
                    isAccExport = !item.isChecked();
                    item.setChecked(isAccExport);
                    break;

                case R.id.action_import:
                    check = 1;
                    requestStoragePermission();
                    break;

                case R.id.action_exAll:
                    check = 2;
                    requestStoragePermission();
                    break;

                case R.id.action_current:
                    check = 3;
                    requestStoragePermission();
                    break;

                case R.id.action_db:
                    check = 4;
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
                importFileChooser();
                break;

            case 2:
                exportAll();
                break;

            case 3:
                try {
                    File file = new File(getExternalStorageDirectory(), FILE_PATH);
                    rmDir(file);

                    counter = isAccExport ? 2 : 1;
                    counter += 1;
                    int yr = timeMilies(currentTimeMillis()).getYy();
                    exportExYr(yr);
                    createBackup(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                File file = new File(getExternalStorageDirectory(), FILE_PATH);
                rmDir(file);
                counter = 1;
                createBackup(activity);
                break;
        }
        ((TextView) findViewById(R.id.tvPath)).setText("");
    }

    private void exportAll() {
        File file = new File(getExternalStorageDirectory(), FILE_PATH);
        rmDir(file);


        try {
            ArrayList<String> years = new Trans(this).yearArray();
            years.remove(0);

            int temp = isAccExport ? 2 : 1;
            counter = years.size() * temp;
            counter += 1;

            createBackup(activity);

            for (String year : years) {
                try {
                    int yr = parseInt(year);
                    exportExYr(yr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            error(activity, e);
        }
    }

    private void createBackup(Activity context) {
        String inFile, outFile;

        outFile = setPath() + "/" + DBNAME + ".db";
        inFile = context.getDatabasePath(DBNAME).toString();

        try {
            FileInputStream fis = new FileInputStream(new File(inFile));
            OutputStream output = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            fis.close();

            snack(context.findViewById(R.id.linearMain), "Database Exported");

            onComplete("db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportExYr(int year) {
        try {
            MonExport mon = new MonExport(activity, year);
            mon.write();

            if (isAccExport) {
                AccExport acc = new AccExport(activity, year);
                acc.write();
            }

        } catch (Exception e) {
            error(activity, e);
        }

    }

    //endregion

    //region Request Permissions
    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            checkTest();
            return;
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(activity, new String[]{READ_EXTERNAL_STORAGE}, PERM_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERM_STORAGE:
                //If permission is granted
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    checkTest();
                } else {
                    finish();
                    //Displaying another toast if permission is not granted
                    toast(this, "Give Permission From Settings");
                }
                break;
        }
    }
    //endregion

    private void importFileChooser() {
        try {
            check = 0;

            Intent intent = new Intent(ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(CATEGORY_OPENABLE);

            startActivityForResult(createChooser(intent, "Select a File"), PERM_FILE_SELECT);
        } catch (android.content.ActivityNotFoundException ex) {
            toast(activity, "Install File Manager.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case PERM_FILE_SELECT:
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        copyFile(new File(uri.getPath()), new File(getDatabasePath(DBNAME).toString()));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File src, File dst) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];

        int content;
        while ((content = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, content);
        }

        fis.close();
        fos.close();
    }

    //region Info Data
    private void alertView() {

        Builder builder = new Builder(activity);
        final String EMAILID = "pateljay269@yahoo.com";
        builder.setMessage(" If You Want To Any Changes Contact Me.\n"
                + "\n Email :- " + EMAILID + " \n");

        builder.setTitle("Contact Us")
                .setCancelable(true)
                .setNeutralButton("Follow On FB", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        String F_URL = "https://www.facebook.com/pateljay269";
                        Intent browserIntent = new Intent(ACTION_VIEW, parse(F_URL));
                        startActivity(browserIntent);
                    }
                })
                .setPositiveButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] TO = {EMAILID};
                        Intent emailIntent = new Intent(ACTION_SEND);

                        emailIntent.setData(parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(EXTRA_EMAIL, TO);
                        emailIntent.putExtra(EXTRA_SUBJECT, "Subject:");
                        emailIntent.putExtra(EXTRA_TEXT, "");

                        try {
                            startActivity(createChooser(emailIntent, "Send mail..."));
                            finish();
                        } catch (android.content.ActivityNotFoundException ex) {
                            toast(activity, "There is no email client installed.");
                        }
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //endregion

    public void backClick() {
        finishAffinity();
        System.exit(0);
        gc();
        runFinalization();
    }

    boolean isExit = false;

    @Override
    public void onBackPressed() {
        View view = findViewById(R.id.linearMain);
        Snackbar snackbar = Snackbar.make(view, "Press Again To Exit", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        textView.setTextSize(20);
        snackbar.show();

        if (isExit) {
            finish();
            backClick();
            return;
        }

        isExit = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isExit = false;
            }
        }, 2000);

    }

    //region Zip Create
    @Override
    public void onComplete(String flag) {
        counter--;

        if (counter == 0) {
            ArrayList<String> arFiles = new ArrayList<>();
            for (File file : setPath().listFiles()) {
                arFiles.add(file.getAbsolutePath());
            }

            long time = currentTimeMillis();
            String dt = zipPath(time);

            CompressFiles mCompressFiles = new CompressFiles(activity, dt, arFiles);
            mCompressFiles.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onZipComplete() {
        long time = currentTimeMillis();
        String path = "Path: " + PATH + zipPath(time);
        ((TextView) findViewById(R.id.tvPath)).setText(path);

        File file = new File(getExternalStorageDirectory(), timeMilies(time).getFile_Path());
        rmDir(file);

        final Intent i = getPackageManager().getLaunchIntentForPackage("patel.jay.uptodate");
        if (i != null) {
            findViewById(R.id.layoutUpload).setVisibility(View.VISIBLE);
            findViewById(R.id.btnUpload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(i);
                }
            });
        }
    }

    public String zipPath(long timeInMilies) {
        TimeConvert tc = timeMilies(timeInMilies);
        return "Ex_" + tc.getYy() + "_(" + tc.numberOf(tc.getMm() + 1) + " " + tc.getMMM() + ")_" + tc.numberOf(tc.getDd()) + "_"
                + tc.numberOf(tc.getHh24()) + "H.zip";
    }
    //endregion

    private void rmDir(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                rmDir(child);

        fileOrDirectory.delete();

    }

}
