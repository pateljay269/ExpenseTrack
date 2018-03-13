package patel.jay.expensetrack.RechargeManager.Customer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Adapters.BalCustomerAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;

public class CustomersActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView recyclerView;
    static ArrayList<SqlCustomer> customerArrayList;
    static BalCustomerAdapter balCustomerAdapter;
    FloatingActionButton fab;
    String mobileNo = "", name = "";
    SetCustLayout layout = new SetCustLayout(CustomersActivity.this);

    public static void setRv(Activity activity) {
        try {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            customerArrayList = SqlCustomer.allCustomers(activity);
            balCustomerAdapter = new BalCustomerAdapter(customerArrayList, activity);
            recyclerView.setAdapter(balCustomerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);
        setTitle("Customer");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(this);
        setRv(this);
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category, menu);
        return true;
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_home) {
                finish();
//                MyConstants.goToHomeBalance(this);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, MyConstants.PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyConstants.PICK_CONTACT && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();

                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                /*ArrayList allS=new ArrayList<>();
                    HashMap<String,String> map=new HashMap<>();
                    for (int i=1;i<cursor.getColumnCount();i++){
                        String key=cursor.getColumnName(i);
                        String val=cursor.getString(cursor.getColumnIndex(cursor.getColumnName(i)));
                        map.put(key,val);
                    }
                    allS.add(map);*/
                mobileNo = cursor.getString(phoneIndex);
                name = cursor.getString(nameIndex);

                for (String aSpecial : MyConstants.SPECIAL_CHAR) {
                    name = name.replace(aSpecial, "");
                }

                for (String aSpecial : MyConstants.NUM_CHAR) {
                    mobileNo = mobileNo.replace(aSpecial, "");
                }

                if (mobileNo.length() > 10) {
                    mobileNo = mobileNo.substring(1, mobileNo.length());
                }
                cursor.close();
                layout.alertAddCat(name, mobileNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        try {
            PopupMenu popup = new PopupMenu(CustomersActivity.this, fab);
            popup.getMenuInflater().inflate(R.menu.popup_add_contact, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_contact:
                            pickContact();
                            break;

                        case R.id.action_manually:
                            layout.alertAddCat("", "");
                            break;
                    }
                    return true;
                }
            });

            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
