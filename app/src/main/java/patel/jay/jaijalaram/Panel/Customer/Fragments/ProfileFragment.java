package patel.jay.jaijalaram.Panel.Customer.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.Constants.MyConst.getPrefData;
import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Login.SignActivity.updateData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, ServerCall.OnAsyncResponse {

    EditText etFname, etLname, etMob, etEmail, etPass, etRePass, etAddress;
    Button btnSignUp, btnClear;
    ViewGroup layout;
    Spinner spnType;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_user_profile, container, false);

        spnType = view.findViewById(R.id.spnUserType);

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

        btnSignUp.setOnClickListener(this);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            }
        });
        etMob.setEnabled(false);
        etEmail.setEnabled(false);

        btnSignUp.setText(getResources().getString(R.string.update));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (!isNetAvail(getActivity(), view)) {
            return;
        }

        try {
            int custId = SignActivity.customer.getCustId();
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
                    String url = ServerCall.BASE_URL + ServerCall.CUST + "updateUser";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put(MYSQL.Customer.CUSTID, custId + "");
                    hm.put(MYSQL.Customer.FNAME, fname);
                    hm.put(MYSQL.Customer.LNAME, lname);
                    hm.put(MYSQL.Customer.EMAIL, email);
                    hm.put(MYSQL.Customer.MOBILE, mobile);
                    hm.put(MYSQL.Customer.PASSWORD, pass);
                    hm.put(MYSQL.Customer.ADDRESS, address);
                    hm.put(MYSQL.Customer.TYPE, type);
                    new ServerCall(ProfileFragment.this, getActivity(), url, hm, MyConst.UPDATE)
                            .execute();
                } else {
                    etRePass.setError("Enter Correct Password");
                }
            }
        } catch (Exception e) {
            MyConst.toast(getActivity(), e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.UPDATE:
                if (response.trim().equals("1")) {
                    MyConst.toast(getActivity(), "Data Updated..");

                    String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put(MYSQL.Customer.MOBILE, etMob.getText().toString().trim());
                    hm.put(MYSQL.Customer.PASSWORD, "");
                    hm.put(MYSQL.Customer.ACTION, "Profile Update");
                    ServerCall asyncTask = new ServerCall(ProfileFragment.this, getActivity(), url, hm, MyConst.INSERT);
                    asyncTask.execute();
                } else {
                    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                }
                break;

            case MyConst.INSERT:
                if (response.length() > 0) {
                    updateData(getActivity(), response, false);
                    setData();
                } else {
                    MyConst.toast(getActivity(), "Invalid Username or Password!!!");
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        try {
            etMob.setTextColor(Color.BLACK);
            etEmail.setTextColor(Color.BLACK);
            etFname.setText(SignActivity.customer.getFname());
            etLname.setText(SignActivity.customer.getLname());
            etMob.setText(SignActivity.customer.getMobile() + "");
            etEmail.setText(SignActivity.customer.getEmail());
            etAddress.setText(SignActivity.customer.getAddress());

            String pass = getPrefData(getActivity(), PrefConst.PASSWORD);
            etPass.setText(pass);
            etRePass.setText(pass);
        } catch (Exception e) {
            MyConst.toast(getActivity(), e.getMessage());
        }
    }
}