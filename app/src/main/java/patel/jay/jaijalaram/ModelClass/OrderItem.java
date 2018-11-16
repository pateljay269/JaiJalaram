package patel.jay.jaijalaram.ModelClass;

import android.app.Activity;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;

/**
 * Created by Jay on 21-Feb-18.
 */

public class OrderItem implements Serializable {

    private int oiId, oId, iId, qty, price;
    private String dsc;


    public OrderItem(int oiId, int oId, int iId, int qty, int price, String dsc) {
        this(iId, qty, price, dsc);
        this.oId = oId;
        this.oiId = oiId;
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

            return new OrderItem(oiId, oId, iId, qty, price, dsc);
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

    //endregion

}
