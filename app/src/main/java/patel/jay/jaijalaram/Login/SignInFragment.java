package patel.jay.jaijalaram.Login;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.Constants.MyConst.hideKeyboard;
import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.putIntoPref;
import static patel.jay.jaijalaram.Login.SignActivity.updateData;

public class SignInFragment extends Fragment implements View.OnClickListener, ServerCall.OnAsyncResponse {

    public static String user = "", pass = "";
    EditText etPass, etMob;
    Button btnLogin, btnClear;
    TextView tvForgot;
    View view;

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
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etMob.setText("");
                etPass.setText("");
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        String mob, pass;
        mob = MyConst.getPrefData(getActivity(), PrefConst.MOBILE);
        pass = MyConst.getPrefData(getActivity(), PrefConst.PASSWORD);

        etMob.setText(mob);
        etPass.setText(pass);

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
        if (!isNetAvail(getActivity(), view)) {
            return;
        }

        String mobile = etMob.getText().toString();
        String pass = etPass.getText().toString();

        switch (view.getId()) {
            case R.id.btnLogin:

                if (mobile.isEmpty() || pass.isEmpty()) {
                    etBlankCheck(etMob);
                    etBlankCheck(etPass);
                } else {
                    String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put(MYSQL.Customer.MOBILE, mobile);
                    hm.put(MYSQL.Customer.PASSWORD, pass);
                    hm.put(MYSQL.Customer.ACTION, "Login");
                    new ServerCall(SignInFragment.this, getActivity(), url, hm, MyConst.INSERT)
                            .execute();
                }
                break;

            case R.id.tvForgot:

                if (mobile.isEmpty()) {
                    etBlankCheck(etMob);
                } else {
                    hideKeyboard(getActivity());

                    String url = ServerCall.BASE_URL + ServerCall.OTP + "forgot";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put(MYSQL.Customer.MOBILE, mobile);
                    new ServerCall(SignInFragment.this, getActivity(), url, hm, MyConst.SELECT)
                            .execute();
                }
                break;
        }
    }

    @Override
    public void getResponse(final String response, int flag) {
        switch (flag) {
            case MyConst.INSERT:
                if (response.length() > 0) {
                    putIntoPref(getActivity(), PrefConst.MOBILE, etMob.getText().toString());
                    putIntoPref(getActivity(), PrefConst.PASSWORD, etPass.getText().toString());
                    updateData(getActivity(), response, true);
                } else {
                    MyConst.toast(getActivity(), "Invalid Username or Password!!!");
                }
                break;

            case MyConst.SELECT:
                Snackbar sb;
                if (response.length() > 0) {
                    sb = Snackbar
                            .make(view, "Your Password Is Sent On Your Mobile No.", Snackbar.LENGTH_INDEFINITE);
                            /*.setAction("Copy", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    copy2Clip(getActivity(), response);
                                }
                            }
                            )*/
                } else {
                    sb = Snackbar.make(view, "Invalid Mobile No!!!", Snackbar.LENGTH_INDEFINITE);
                }

                sb.setActionTextColor(Color.RED);
                TextView tv = sb.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.YELLOW);
                sb.show();
                break;
        }

    }
}