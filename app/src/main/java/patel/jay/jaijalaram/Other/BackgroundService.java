package patel.jay.jaijalaram.Other;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import patel.jay.jaijalaram.Constants.NotiFications;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Constants.TimeConvert;
import patel.jay.jaijalaram.Models.Order;

import static patel.jay.jaijalaram.Login.SignActivity.customer;

public class BackgroundService extends Service implements ServerCall.OnAsyncResponse {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    private static String today = "";
    private static int count = 0, hh = 0;
    private static ArrayList<Integer> oIdList;
    TimeConvert tc;

    private final String url = ServerCall.BASE_URL + ServerCall.ORDER + "newOrd";
    HashMap<String, String> hm;

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        try {
            if (customer.getType().equals("M")) {
//            toast(context, "Service Created!");
                oIdList = new ArrayList<>();
                handler = new Handler();

                runnable = new Runnable() {
                    public void run() {
                        checkOrd();
                    }
                };

                checkOrd();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkOrd() {
        try {
            int hhmin = TimeConvert.hourOfDay();
            if (hhmin > 1000 && hhmin < 2200) {
                hm = new HashMap<>();
                new ServerCall(context, url, hm, 101).execute();
                handler.postDelayed(runnable, 30 * 1000);       //30 Sec
            } else {
                handler.postDelayed(runnable, 30 * 60 * 1000);  //30 Min
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
//        handler.removeCallbacks(runnable);
//        toast(context, "Service stopped");
    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            if (response.trim().equals("")) {
                oIdList = new ArrayList<>();
                return;
            }

            JSONArray jArr = new JSONObject(response).getJSONArray("NEWORDER");

            tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
            today = tc.getDD_MMM_YY();

            if (hh != tc.getHh()) {
                hh = tc.getHh();
                oIdList = new ArrayList<>();
            }


            Outer:
            for (int i = 0; i < jArr.length(); i++) {
                Order order = Order.jsonToOrd(jArr.getJSONObject(i));
                for (int oId : oIdList) {
                    if (order.getoId() == oId) {
                        continue Outer;
                    }
                }

                tc = TimeConvert.timeMiliesConvert(order.getCurrentTime());
                if (today.equals(tc.getDD_MMM_YY())) {
                    oIdList.add(order.getoId());
                    NotiFications.orderNoti(context, order, count++);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
