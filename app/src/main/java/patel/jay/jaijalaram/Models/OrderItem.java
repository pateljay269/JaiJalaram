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
 * Created by Jay on 21-Feb-18.
 */

public class OrderItem implements Serializable {

    private int oiId, oId, iId, qty, price;
    private String dsc;
    private int time, day, week, date, month, year;

    public OrderItem(int oiId, int oId, int iId, int qty, int price, String dsc,
                     int time, int day, int week, int date, int month, int year) {
        this(iId, qty, price, dsc);
        this.oId = oId;
        this.oiId = oiId;
        this.day = day;
        this.time = time;
        this.week = week;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public OrderItem(int iId, int qty, int price, String dsc) {
        this.iId = iId;
        this.qty = qty;
        this.price = price;
        this.dsc = dsc;
    }

    @Nullable
    public static OrderItem jsonToOrdItem(JSONObject jsonObject) {
        try {
            int oiId = jsonObject.getInt(MYSQL.OrderItem.OIID);
            int oId = jsonObject.getInt(MYSQL.OrderItem.OID);
            int iId = jsonObject.getInt(MYSQL.OrderItem.IID);
            int qty = jsonObject.getInt(MYSQL.OrderItem.QTY);
            int price = jsonObject.getInt(MYSQL.OrderItem.PRICE);
            String dsc = jsonObject.getString(MYSQL.OrderItem.DSC);

            int time = jsonObject.getInt(MYSQL.TIME);
            int day = jsonObject.getInt(MYSQL.DAY);
            int week = jsonObject.getInt(MYSQL.WEEK);
            int date = jsonObject.getInt(MYSQL.DATE);
            int month = jsonObject.getInt(MYSQL.MONTH);
            int year = jsonObject.getInt(MYSQL.YEAR);

            return new OrderItem(oiId, oId, iId, qty, price, dsc, time, day, week, date, month, year);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<OrderItem> allOrderItem(Activity activity, String json) {
        ArrayList<OrderItem> orderItems = new ArrayList<>();

        try {
            JSONArray jArr = new JSONObject(json).getJSONArray(PrefConst.ORDERITEM);

            for (int i = 0; i < jArr.length(); i++) {
                orderItems.add(jsonToOrdItem(jArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return orderItems;
    }

    //region Getter Setter

    public String getDsc() {
        return dsc;
    }

    public int getOiId() {
        return oiId;
    }

    public int getoId() {
        return oId;
    }

    public int getiId() {
        return iId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public int getWeek() {
        return week;
    }

    public int getTime() {
        return time;
    }

    //endregion

}
