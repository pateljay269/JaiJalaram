package patel.jay.jaijalaram.Login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.ConstClass.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.Login.SignActivity.updateUserData;

public class SignInFragment extends Fragment implements View.OnClickListener, ServerCall.OnAsyncResponse {

    public static String user = "", pass = "";
    EditText etPass, etMob;
    Button btnLogin, btnClear;
    TextView tvForgot;
    View view;

    public SignInFragment() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        tvForgot = view.findViewById(R.id.tvForgot);
        etMob = view.findViewById(R.id.etMobile);
        etPass = view.findViewById(R.id.etPass);

        btnClear = view.findViewById(R.id.btnClear);
        btnLogin = view.findViewById(R.id.btnLogin);

        tvForgot.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        etMob.setText("12345");
        etPass.setText("1236");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!user.isEmpty() && !pass.isEmpty()) {
            etMob.setText(user);
            etPass.setText(pass);
        }
    }

    @Override
    public void onClick(View view) {
        if (!ServerCall.BASE_URL.equals("")) {
            switch (view.getId()) {
                case R.id.btnLogin:
                    String mobile = etMob.getText().toString();
                    String pass = etPass.getText().toString();

                    if (mobile.isEmpty() || pass.isEmpty()) {
                        etBlankCheck(etMob);
                        etBlankCheck(etPass);
                    } else {
                        String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put(MYSQL.Customer.MOBILE, mobile);
                        hm.put(MYSQL.Customer.PASSWORD, pass);
                        hm.put("action", "Login");
                        new ServerCall(SignInFragment.this, getActivity(), url, hm, MyConst.INSERT)
                                .execute();
                    }
                    break;

                case R.id.btnClear:
                    etMob.setText("");
                    etPass.setText("");
                    break;

                case R.id.tvForgot:
                    break;
            }
        } else {
            MyConst.toast(getActivity(), "Set IP First");
            SignActivity.viewPager.setCurrentItem(1, true);
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            switch (flag) {
                case MyConst.INSERT:
                    if (response.length() > 0) {
                        updateUserData(getActivity(), response);
                    } else {
                        MyConst.toast(getActivity(), "Invalid Username or Password!!!");
                    }
                    break;
            }
        } catch (Exception e) {
            MyConst.toast(getActivity(), e.getMessage());
        }
    }
}