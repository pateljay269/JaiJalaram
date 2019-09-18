package patel.jay.jaijalaram.Models;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;

/**
 * Created by Jay on 25-Jan-18.
 */

public class Customer implements Serializable {

    private int custId, password;
    private String fname, lname, mobile, email, address, type;

    public Customer(int custId, String fname, String lname, String mobile,
                    int password, String email, String address, String type) {
        this(custId, fname, lname, mobile, email, address, type);
        this.password = password;
    }

    public Customer(int custId, String fname, String lname, String mobile,
                    String email, String address, String type) {
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
//            int password = jObj.getInt(MYSQL.Customer.PASSWORD);
            String email = jObj.getString(MYSQL.Customer.EMAIL);
            String address = jObj.getString(MYSQL.Customer.ADDRESS);
            String type = jObj.getString(MYSQL.Customer.TYPE).toUpperCase();
            int cid = jObj.getInt(MYSQL.Customer.CUSTID);

            return new Customer(cid, fname, lname, mobile, email, address, type);
//            return new Customer(cid, fname, lname, mobile, password, email, address, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    public static Customer curCustToJson(Context activity, String data) {
        try {
            JSONObject jObj = new JSONObject(data);
            jObj = jObj.getJSONArray(PrefConst.USERDATA_S).getJSONObject(0);

            return jsonToCust(jObj);

        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }

        return null;
    }

    @Nullable
    public static ArrayList<Customer> allCust(Context activity) {
        ArrayList<Customer> customers = adminAllCust(activity);
        ArrayList<Customer> arrayList = new ArrayList<>();

        try {
            for (Customer customer : customers) {
                if (customer.toString().toLowerCase().contains("admin") ||
                        customer.toString().toLowerCase().contains("manager")) {
                } else
                    arrayList.add(customer);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return new ArrayList<>();
    }

    @Nullable
    public static ArrayList<Customer> adminAllCust(Context activity) {
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
        return new ArrayList<>();
    }

    @Nullable
    public static ArrayList<String> allCustStr(Context activity) {
        ArrayList<Customer> customers = allCust(activity);
        ArrayList<String> list = new ArrayList<>();

        for (Customer customer : customers) {
            list.add(customer.toString());
        }

        return list;
    }

    public static String custName(Context activity, int custId) {
        for (Customer cust : allCust(activity))
            if (custId == cust.getCustId())
                return cust.toString();
        return "";
    }

    public static Customer customer(Context activity, int custId) {
        try {
            for (Customer cust : allCust(activity))
                if (custId == cust.getCustId())
                    return cust;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toGson(Context context, String response) {
        Gson gson = new Gson();
        Customer customer = curCustToJson(context, response);

        return gson.toJson(customer);
    }

    @Nullable
    public static Customer fromGson(Context activity) {
        try {
            String json = MyConst.getPrefData(activity, PrefConst.USERDATA_S);
            if (json.equals("") || json.toUpperCase().equals("NULL")) {
                return null;
            }
            return new Gson().fromJson(json, Customer.class);
        } catch (Exception e) {
            MyConst.toast(activity, e.getMessage());
            return null;
        }
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
        if (fname.length() > 1)
            return fname.substring(0, 1).toUpperCase() + fname.substring(1);
        else
            return fname;
    }

    public String getLname() {
        if (lname.length() > 1)
            return lname.substring(0, 1).toUpperCase() + lname.substring(1);
        else
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
        return getFname() + " " + getLname();
    }
}