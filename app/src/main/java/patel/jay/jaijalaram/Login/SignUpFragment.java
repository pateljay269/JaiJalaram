package patel.jay.jaijalaram.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.clearEdittext;
import static patel.jay.jaijalaram.Constants.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Login.SignActivity.customer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener, ServerCall.OnAsyncResponse {

    EditText etFname, etLname, etMob, etEmail, etPass, etRePass, etAddress;
    Button btnSignUp, btnClear;
    ViewGroup layout;
    Spinner spnType;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_user_profile, container, false);

        layout = view.findViewById(R.id.layoutSignUp);

        spnType = view.findViewById(R.id.spnUserType);
        etFname = view.findViewById(R.id.etFname);
        etLname = view.findViewById(R.id.etLname);
        etAddress = view.findViewById(R.id.etAddress);
        etMob = view.findViewById(R.id.etMobile);
        etEmail = view.findViewById(R.id.etEmail);
        etPass = view.findViewById(R.id.etPass);
        etRePass = view.findViewById(R.id.etRePass);

        btnClear = view.findViewById(R.id.btnClear);
        btnSignUp = view.findViewById(R.id.btnSignUp);

        btnClear.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        if (customer != null) {
            ArrayList<String> array = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.userType)));
            spnType.setVisibility(View.VISIBLE);
            if (customer.getType().equals("M")) {
                array.remove(getString(R.string.admin));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
            spnType.setAdapter(adapter);
        } else {
            view.findViewById(R.id.tvMsg).setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnClear:
                    clearEdittext(layout);
                    break;

                case R.id.btnSignUp:
                    if (!isNetAvail(getActivity(), view)) {
                        return;
                    }

                    String fname = etFname.getText().toString().trim();
                    String lname = etLname.getText().toString().trim();
                    String mobile = etMob.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String pass = etPass.getText().toString().trim();
                    String cPass = etRePass.getText().toString().trim();
                    String address = etAddress.getText().toString().trim();
                    String type = spnType.getSelectedItem().toString().trim().charAt(0) + "";

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
                            hm.put(MYSQL.Customer.TYPE, type.toUpperCase());
                            new ServerCall(SignUpFragment.this, getActivity(), url, hm, MyConst.INSERT)
                                    .execute();
                        } else {
                            etRePass.setError("Enter Correct Password");
                        }
                    }
                    break;
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

                    if (customer == null)
                        SignActivity.viewPager.setCurrentItem(2, true);
                } else {
                    Toast.makeText(getActivity(), "Fail:" + response, Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}