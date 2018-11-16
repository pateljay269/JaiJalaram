package patel.jay.jaijalaram.ModelClass;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;

/**
 * Created by Jay on 25-Jan-18.
 */

public class Customer implements Serializable {

    private int custId, password;
    private String fname, lname, mobile, email, address, type;

    public Customer(int custId, String fname, String lname, String mobile,
                    int password, String email, String address, String type) {
        this.password = password;
        this.mobile = mobile;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.address = address;
        this.type = type;
        this.custId = custId;
    }

    @Nullable
    public static Customer jsonToCust(JSONObject jObj) {
        try {
            String fname = jObj.getString(MYSQL.Customer.FNAME);
            String lname = jObj.getString(MYSQL.Customer.LNAME);
            String mobile = jObj.getString(MYSQL.Customer.MOBILE);
            int password = jObj.getInt(MYSQL.Customer.PASSWORD);
            String email = jObj.getString(MYSQL.Customer.EMAIL);
            String address = jObj.getString(MYSQL.Customer.ADDRESS);
            String type = jObj.getString(MYSQL.Customer.TYPE);
            int cid = jObj.getInt(MYSQL.Customer.CUSTID);

            return new Customer(cid, fname, lname, mobile, password, email, address, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    public static Customer curCustToJson(Activity activity, String data) {
        try {
            JSONObject jObj = new JSONObject(data);
            jObj = jObj.getJSONArray(PrefConst.USERDATA_S).getJSONObject(0);

            return jsonToCust(jObj);

        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.error(activity, e);
        }

        return null;
    }

    @Nullable
    public static ArrayList<Customer> allCust(Activity activity) {
        ArrayList<Customer> customers = new ArrayList<>();

        try {
            String data = MyConst.getPrefData(activity, PrefConst.AUSER_S);
            JSONArray jArr = new JSONObject(data).getJSONArray(PrefConst.AUSER_S);

            for (int i = 0; i < jArr.length(); i++) {
                customers.add(jsonToCust(jArr.getJSONObject(i)));
            }

            return customers;
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return null;
    }

    public static String toGson(Activity context, String response) {
        Gson gson = new Gson();
        Customer customer = Customer.curCustToJson(context, response);

        return gson.toJson(customer);
    }

    @Nullable
    public static Customer fromGson(Activity activity) {
        String json = MyConst.getPrefData(activity, PrefConst.USERDATA_S);
        Gson gson = new Gson();
        if (json.equals("")) {
            return null;
        }
        return gson.fromJson(json, Customer.class);
    }

    //region Getter Setter

    public int getCustId() {
        return custId;
    }

    public int getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    //endregion

    @Override
    public String toString() {
        return fname + " " + lname;
    }
}