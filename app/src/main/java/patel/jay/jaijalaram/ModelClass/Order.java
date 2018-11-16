package patel.jay.jaijalaram.ModelClass;

import android.app.Activity;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;

/**
 * Created by Jay on 24-Feb-18.
 */

public class Order {

    private int oId, custId, price, iIdFirst;
    private String date;
    private long currentTime;

    public Order(int oId, int custId, int price, String date, long currentTime, int iIdFirst) {
        this.oId = oId;
        this.iIdFirst = iIdFirst;
        this.custId = custId;
        this.price = price;
        this.date = date;
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

    @Nullable
    public static Order jsonToOrd(JSONObject jsonObject) {
        try {
            int oId = jsonObject.getInt(MYSQL.Order.OID);
            int iId = jsonObject.getInt(MYSQL.Order.IID);
            int custId = jsonObject.getInt(MYSQL.Order.CUSTID);
            int price = jsonObject.getInt(MYSQL.Order.PRICE);
            String date = jsonObject.getString(MYSQL.Order.DATE);
            long currentTime = Long.parseLong(jsonObject.getString(MYSQL.Order.TIME));

            return new Order(oId, custId, price, date, currentTime, iId);
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

    public String getDate() {
        return date;
    }

    public long getCurrentTime() {
        return currentTime;
    }
    //endregion
}
