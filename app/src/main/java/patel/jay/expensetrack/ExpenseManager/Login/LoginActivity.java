package patel.jay.expensetrack.ExpenseManager.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.DashBoard.HomeDashActivity;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlUser;
import patel.jay.expensetrack.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnEnter;
    TextView tvPass, tvShow;
    String nPass = "";
    AlphaAnimation alpha;

    int total = 12;
    int column = 3;
    int row = total / column;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvPass = (TextView) findViewById(R.id.tvPass);
        tvShow = (TextView) findViewById(R.id.tvShow);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        tvPass.setOnClickListener(this);
        btnEnter.setOnClickListener(this);

        alpha = new AlphaAnimation(0.0f, 0.0f);
        alpha.setDuration(20);
        alpha.setStartOffset(20);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.addView(setFGrid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvShow.setText("");
    }

    @SuppressLint("SetTextI18n")
    public GridLayout setFGrid() {
        GridLayout gridLayout = new GridLayout(this);

        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        Button tvNum;
        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }
            tvNum = new Button(this);
            tvNum.setBackground(getDrawable(R.drawable.round_boarder));
            String temp = "";
            switch (i) {
                //region Switch
                case 0:
                    temp = "1\n";
                    break;
                case 1:
                    temp = "2\nABC";
                    break;
                case 2:
                    temp = "3\nDEF";
                    break;
                case 3:
                    temp = "4\nGHI";
                    break;
                case 4:
                    temp = "5\nJKL";
                    break;
                case 5:
                    temp = "6\nMNO";
                    break;
                case 6:
                    temp = "7\nPQRS";
                    break;
                case 7:
                    temp = "8\nTUV";
                    break;
                case 8:
                    temp = "9\nWXYZ";
                    break;
                case 9:
                    temp = "0";
                    break;
                case 10:
                    temp = "0";
                    break;
                case 11:
                    temp = "0";
                    break;
                //endregion
            }
            tvNum.setText(temp + "");
            tvNum.setGravity(Gravity.CENTER);

            tvNum.setTextSize(20);
            tvNum.setTypeface(null, Typeface.BOLD);
            final int finalI = i + 1;
            final Button finalTvNum = tvNum;
            tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvShow.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    tvShow.setTextColor(Color.GRAY);
                    finalTvNum.startAnimation(alpha);
                    if (finalI < 10) {
                        nPass += finalI;
                        tvShow.setText(nPass);
                    } else if (finalI == 11) {
                        nPass += "0";
                        tvShow.setText(nPass);
                    }
                }
            });

            if (i < 9 || i == 10) {
                gridLayout.addView(tvNum, i);
            } else {
                tvNum.setVisibility(View.INVISIBLE);
                gridLayout.addView(tvNum, i);
            }

            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setMargins(10, 10, 10, 10);

            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            tvNum.setLayoutParams(param);
        }
        return gridLayout;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPass:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;

            case R.id.btnEnter:
                //region UserLogin
                SqlUser sqlAdmin = SqlUser.checkAdminPin(LoginActivity.this, nPass);
                SqlUser sqlUser = SqlUser.checkUserPin(LoginActivity.this, nPass);
                if (sqlAdmin != null) {
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                } else if (sqlUser != null) {
                    startActivity(new Intent(LoginActivity.this, HomeDashActivity.class));
                } else {
                    tvShow.setInputType(InputType.TYPE_CLASS_TEXT);
                    tvShow.setTextColor(Color.RED);
                    tvShow.setText("Enter Correct Details");
                }
                nPass = "";
                //endregion
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyConstants.backClick(this);
    }
}
