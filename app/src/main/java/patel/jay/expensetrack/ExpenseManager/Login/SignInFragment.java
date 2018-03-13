package patel.jay.expensetrack.ExpenseManager.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.DashBoard.ExpenseDashActivity;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlUser;
import patel.jay.expensetrack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener {

    public static String userName = "";
    EditText etPass, etUser;
    EditText etAcNo;
    Button btnLogin, btnForgot, btnClear;
    TextView tvForgot, tvPass;
    LinearLayout layout;
    View view;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        etUser = (EditText) view.findViewById(R.id.etUser);
        etPass = (EditText) view.findViewById(R.id.etPass);
        etAcNo = (EditText) view.findViewById(R.id.etAcNo);

        tvPass = (TextView) view.findViewById(R.id.tvPass);
        layout = (LinearLayout) view.findViewById(R.id.linearForgot);
        layout.setVisibility(View.GONE);

        tvForgot = (TextView) view.findViewById(R.id.tvForgot);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnForgot = (Button) view.findViewById(R.id.btnForgot);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);

        tvForgot.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        etUser.setText(userName);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:

                if (etUser.getText().toString().isEmpty() || etPass.getText().toString().isEmpty()) {
                    if (TextUtils.isEmpty(etUser.getText().toString())) {
                        etUser.setError("Required");
                    }
                    if (TextUtils.isEmpty(etPass.getText().toString())) {
                        etPass.setError("Required");
                    }
                } else {
                    String user = etUser.getText().toString();
                    String pass = etPass.getText().toString();

                    SqlUser sqlAdmin = SqlUser.checkAdminPin(getActivity(), pass);
                    if (sqlAdmin != null) {
                        startActivity(new Intent(getActivity(), AdminActivity.class));
                    } else {
                        SqlUser sqlUser = SqlUser.checkUser(getActivity(), user, pass);
                        if (sqlUser != null) {
                            startActivity(new Intent(getActivity(), ExpenseDashActivity.class));
                        } else {
                            MyConstants.toast(getActivity(), "Enter Correct Details");
                        }
                    }
                }
                break;

            case R.id.btnClear:
                etUser.setText("");
                etPass.setText("");
                break;

            case R.id.btnForgot:
                if (etAcNo.getText().toString().isEmpty()) {
                    if (TextUtils.isEmpty(etAcNo.getText().toString())) {
                        etAcNo.setError("Required");
                    }
                } else {
                    try {
                        String acNo = etAcNo.getText().toString();
                        String[] pass = SqlUser.findAcPass(getActivity(), acNo);
                        tvPass.setText("Username :- " + pass[0] + "\nPassword :- " + pass[1] + "\nPin :- " + pass[2]);
                        layout.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.tvForgot:
                if (layout.getVisibility() == View.GONE) {
                    layout.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.GONE);
                }
                break;
        }
    }
}
