package patel.jay.jaijalaram.Fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.ConstClass.MyConst.etBlankCheck;
import static patel.jay.jaijalaram.Login.SignActivity.updateUserData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, ServerCall.OnAsyncResponse {

    EditText etFname, etLname, etMob, etEmail, etPass, etRePass, etAddress;
    Button btnSignUp, btnClear;
    ViewGroup layout;
    View view;

    public ProfileFragment() {
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
        setData();
    }

    @Override
    public void onClick(View view) {
        try {
            int custId = SignActivity.customer.getCustId();
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
                    String url = ServerCall.BASE_URL + ServerCall.CUST + "updateUser";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("cust_id", custId + "");
                    hm.put("fname", fname);
                    hm.put("lname", lname);
                    hm.put("email", email);
                    hm.put("mobile", mobile);
                    hm.put("pass_word", pass);
                    hm.put("address", address);
                    ServerCall asyncTask = new ServerCall(ProfileFragment.this, getActivity(), url, hm, MyConst.UPDATE);
                    asyncTask.execute();
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
                    hm.put("mobile", etMob.getText().toString().trim());
                    hm.put("pass_word", etPass.getText().toString().trim());
                    hm.put("action", "Profile Update");
                    ServerCall asyncTask = new ServerCall(ProfileFragment.this, getActivity(), url, hm, MyConst.INSERT);
                    asyncTask.execute();
                } else {
                    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                }
                break;

            case MyConst.INSERT:
                if (response.length() > 0) {
                    updateUserData(getActivity(), response);
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
            etMob.setTextColor(Color.GRAY);
            etEmail.setTextColor(Color.GRAY);
            etFname.setText(SignActivity.customer.getFname());
            etLname.setText(SignActivity.customer.getLname());
            etMob.setText(SignActivity.customer.getMobile() + "");
            etEmail.setText(SignActivity.customer.getEmail());
            etPass.setText(SignActivity.customer.getPassword() + "");
            etRePass.setText(SignActivity.customer.getPassword() + "");
            etAddress.setText(SignActivity.customer.getAddress());
        } catch (Exception e) {
            MyConst.toast(getActivity(), e.getMessage());
        }
    }
}