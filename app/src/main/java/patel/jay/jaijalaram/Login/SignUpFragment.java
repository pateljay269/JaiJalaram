package patel.jay.jaijalaram.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.ConstClass.MyConst.clearEdittext;
import static patel.jay.jaijalaram.ConstClass.MyConst.etBlankCheck;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener, ServerCall.OnAsyncResponse {

    EditText etFname, etLname, etMob, etEmail, etPass, etRePass, etAddress;
    Button btnSignUp, btnClear;
    ViewGroup layout;
    View view;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_profile, container, false);

        layout = view.findViewById(R.id.layoutSignUp);
        etFname = view.findViewById(R.id.etFname);
        etLname = view.findViewById(R.id.etLname);
        etAddress = view.findViewById(R.id.etAddress);
        etEmail = view.findViewById(R.id.etEmail);
        etMob = view.findViewById(R.id.etMobile);
        etPass = view.findViewById(R.id.etPass);
        etRePass = view.findViewById(R.id.etRePass);

        btnClear = view.findViewById(R.id.btnClear);
        btnSignUp = view.findViewById(R.id.btnSignUp);

        btnClear.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        try {
            if (!ServerCall.BASE_URL.equals("")) {
                switch (view.getId()) {
                    case R.id.btnClear:
                        clearEdittext(layout);
                        break;

                    case R.id.btnSignUp:
                        String fname = etFname.getText().toString().trim();
                        String lname = etLname.getText().toString().trim();
                        String mobile = etMob.getText().toString().trim();
                        String email = etEmail.getText().toString().trim();
                        String pass = etPass.getText().toString().trim();
                        String cPass = etRePass.getText().toString().trim();
                        String address = etAddress.getText().toString().trim();

                        if (fname.isEmpty() || lname.isEmpty() || address.isEmpty() || email.isEmpty()
                                || pass.isEmpty() || cPass.isEmpty() || mobile.isEmpty()) {

                            etBlankCheck(etFname);
                            etBlankCheck(etLname);
                            etBlankCheck(etMob);
                            etBlankCheck(etEmail);
                            etBlankCheck(etPass);
                            etBlankCheck(etRePass);
                            etBlankCheck(etAddress);

                        } else {
                            if (pass.equals(cPass)) {
                                String url = ServerCall.BASE_URL + ServerCall.CUST + "insertUser";
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put(MYSQL.Customer.FNAME, fname);
                                hm.put(MYSQL.Customer.LNAME, lname);
                                hm.put(MYSQL.Customer.EMAIL, email);
                                hm.put(MYSQL.Customer.MOBILE, mobile);
                                hm.put(MYSQL.Customer.PASSWORD, pass);
                                hm.put(MYSQL.Customer.ADDRESS, address);
                                hm.put(MYSQL.Customer.TYPE, "U");
                                new ServerCall(SignUpFragment.this, getActivity(), url, hm, MyConst.INSERT)
                                        .execute();
                            } else {
                                etRePass.setError("Enter Correct Password");
                            }
                        }
                        break;
                }
            } else {
                MyConst.toast(getActivity(), "Set IP First");
                SignActivity.viewPager.setCurrentItem(1, true);
            }
        } catch (Exception e) {
            MyConst.toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.INSERT:
                if (response.trim().equals("1")) {
                    SignInFragment.user = etMob.getText().toString();
                    SignInFragment.pass = etPass.getText().toString();

                    MyConst.toast(getActivity(), "Registration Complete..");
                    SignActivity.viewPager.setCurrentItem(2, true);
                } else {
                    Toast.makeText(getActivity(), "Fail:" + response, Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}