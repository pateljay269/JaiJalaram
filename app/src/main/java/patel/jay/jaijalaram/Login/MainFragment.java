package patel.jay.jaijalaram.Login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    Button btnSignIn, btnSignUp, btnIP;
    Spinner spnIp;
    View view;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        spnIp = view.findViewById(R.id.spnIp);

        btnSignIn = view.findViewById(R.id.btnSignIn);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnIP = view.findViewById(R.id.btnIP);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = "Selected Ip Set For Database";
                switch (spnIp.getSelectedItemPosition()) {
                    case 1:
                        ServerCall.BASE_URL = ServerCall.BASE_URL1;
                        break;

                    case 2:
                        ServerCall.BASE_URL = ServerCall.BASE_URL2;
                        break;

                    case 3:
                        ServerCall.BASE_URL = ServerCall.BASE_URL3;
                        break;

                    default:
                        temp = "Set IP ";
                        ServerCall.BASE_URL = "";
                        if (spnIp.getSelectedItemPosition() == 0) {
                            ((TextView) spnIp.getSelectedView()).setError("Field Required");
                        }
                        break;
                }
                MyConst.toast(getActivity(), temp);
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        if (!ServerCall.BASE_URL.equals("")) {
            switch (view.getId()) {
                case R.id.btnSignUp:
                    SignActivity.viewPager.setCurrentItem(0, true);
                    break;

                case R.id.btnSignIn:
                    SignActivity.viewPager.setCurrentItem(2, true);
                    break;
            }
        } else {
            if (spnIp.getSelectedItemPosition() == 0) {
                ((TextView) spnIp.getSelectedView()).setError("Field Required");
            }
            MyConst.toast(getActivity(), "Set IP First");
        }
    }
}