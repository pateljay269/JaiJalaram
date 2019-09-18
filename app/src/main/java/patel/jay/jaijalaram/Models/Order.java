package patel.jay.jaijalaram.Models;

import android.app.Activity;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;

/**
 * Created by Jay on 24-Feb-18.
 */

public class Order implements Serializable {

    private int oId, custId, price, iIdFirst;
    private boolean isConfirm, isDeliver;
    private int time, day, date, week, month, year;
    private long currentTime;

    public Order(int oId, int custId, int price, long currentTime, int iIdFirst, boolean isConfirm, boolean isDeliver,
                 int time, int day, int week, int date, int month, int year) {
        this.oId = oId;
        this.custId = custId;
        this.price = price;
        this.iIdFirst = iIdFirst;
        this.isConfirm = isConfirm;
        this.isDeliver = isDeliver;
        this.day = day;
        this.time = time;
        this.week = week;
        this.date = date;
        this.month = month;
        this.year = year;
        this.currentTime = currentTime;
    }

    public static ArrayList<Order> allOrder(Activity activity, boolean isAdmin) {
        ArrayList<Order> orders = new ArrayList<>();

        try {
            String data;
            if (isAdmin) {
                data = MyConst.getPrefData(activity, PrefConst.AORDER_S);
            } else {
                data = MyConst.getPrefData(activity, PrefConst.ORDER_S);
            }
            JSONArray jArr = new JSONObject(data).getJSONArray(PrefConst.ORDER_S);

            for (int i = 0; i < jArr.length(); i++) {
                orders.add(jsonToOrd(jArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return orders;
    }

    public static ArrayList<Order> allOrder(Activity activity, String data) {
        ArrayList<Order> orders = new ArrayList<>();

        try {
            JSONArray jArr = new JSONObject(data).getJSONArray(PrefConst.ORDER_S);

            for (int i = 0; i < jArr.length(); i++) {
                orders.add(jsonToOrd(jArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return orders;
    }

    @Nullable
    public static Order jsonToOrd(JSONObject jsonObject) {
        try {
            int oId = jsonObject.getInt(MYSQL.Order.OID);
            int iId = jsonObject.getInt(MYSQL.Order.IID);
            int custId = jsonObject.getInt(MYSQL.Order.CUSTID);
            int price = jsonObject.getInt(MYSQL.Order.PRICE);
            long currentTime = Long.parseLong(jsonObject.getString(MYSQL.Order.CURTIME));
            boolean confirm = jsonObject.getInt(MYSQL.Order.CONFIRM) == 1;
            boolean isDeliver = jsonObject.getInt(MYSQL.Order.ISDELIVER) == 1;

            int time = jsonObject.getInt(MYSQL.TIME);
            int day = jsonObject.getInt(MYSQL.DAY);
            int week = jsonObject.getInt(MYSQL.WEEK);
            int date = jsonObject.getInt(MYSQL.DATE);
            int month = jsonObject.getInt(MYSQL.MONTH);
            int year = jsonObject.getInt(MYSQL.YEAR);

            return new Order(oId, custId, price, currentTime, iId, confirm, isDeliver, time, day, week, date, month, year);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    //region Getter

    public int getiIdFirst() {
        return iIdFirst;
    }

    public int getPrice() {
        return price;
    }

    public int getoId() {
        return oId;
    }

    public int getCustId() {
        return custId;
    }

    public int getDate() {
        return date;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public boolean isDeliver() {
        return isDeliver;
    }

    public int getTime() {
        return time;
    }

    public int getDay() {
        return day;
    }

    public int getWeek() {
        return week;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    //endregion
}
