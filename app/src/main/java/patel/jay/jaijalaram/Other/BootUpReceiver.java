package patel.jay.jaijalaram.Other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Customer;

import static patel.jay.jaijalaram.Constants.MyConst.putIntoPref;
import static patel.jay.jaijalaram.Login.SignActivity.customer;

public class BootUpReceiver extends BroadcastReceiver implements ServerCall.OnAsyncResponse {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        try {
            customer = Customer.fromGson(context);
            if (customer != null && customer.getType().equals("M")) {
                String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
                HashMap<String, String> hm = new HashMap<>();
                hm.put("mobile", customer.getMobile() + "");
                hm.put("pass_word", customer.getPassword() + "");
                hm.put("action", "Login");
                new ServerCall(context, url, hm, MyConst.INSERT).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            String user = Customer.toGson(context, response);
            putIntoPref(context, PrefConst.USERDATA_S, user);

            customer = Customer.fromGson(context);
            if (customer != null) {
                Intent myIntent = new Intent(context, BackgroundService.class);
                context.startService(myIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
