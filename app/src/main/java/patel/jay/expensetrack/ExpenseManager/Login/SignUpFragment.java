package patel.jay.expensetrack.ExpenseManager.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlUser;
import patel.jay.expensetrack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    EditText etPin, etUser, etPass, etCPass, etAcNo;
    Button btnRegister;
    View view;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        etUser = (EditText) view.findViewById(R.id.etUser);
        etPin = (EditText) view.findViewById(R.id.etPin);
        etPass = (EditText) view.findViewById(R.id.etPass);
        etCPass = (EditText) view.findViewById(R.id.etCPass);
        etAcNo = (EditText) view.findViewById(R.id.etAcNo);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        String user = etUser.getText().toString();
        String pass = etPass.getText().toString();
        String cPass = etCPass.getText().toString();
        String acNo = etAcNo.getText().toString();
        String pinCode = etPin.getText().toString();
        if (user.isEmpty() || pass.isEmpty() || cPass.isEmpty() || pinCode.isEmpty() || acNo.isEmpty()) {
            if (TextUtils.isEmpty(etUser.getText().toString())) {
                etUser.setError("Required");
            }
            if (TextUtils.isEmpty(etPass.getText().toString())) {
                etPass.setError("Required");
            }
            if (TextUtils.isEmpty(etCPass.getText().toString())) {
                etCPass.setError("Required");
            }
            if (TextUtils.isEmpty(etAcNo.getText().toString())) {
                etAcNo.setError("Required");
            }
            if (TextUtils.isEmpty(etPin.getText().toString())) {
                etPin.setError("Required");
            }
        } else if (pass.equals(cPass)) {
            SqlUser sqlUser = new SqlUser(user, pass, Integer.parseInt(pinCode), acNo);
            long val = sqlUser.insert(getActivity());
            if (val > 0) {
                HomeActivity.viewPager.setCurrentItem(0);
            } else {
                MyConstants.toast(getActivity(), "Failed");
            }
        } else {
            etCPass.setError("Enter Correct Password");
        }

    }
}
