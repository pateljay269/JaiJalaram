package patel.jay.jaijalaram.Models;

import android.app.Activity;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;

/**
 * Created by Jay on 26-Jan-18.
 */

public class Items implements Serializable {

    private int iId, cId, price, rate, total;
    private String name, imgSrc;

    public Items(int iId, String name, int cId, int price, String imgSrc, int rate, int total) {
        this.iId = iId;
        this.cId = cId;
        this.price = price;
        this.rate = rate;
        this.total = total;
        this.name = name;
        this.imgSrc = imgSrc;
    }

    @Nullable
    public static Items jsonToItem(JSONObject jsonObject) {
        try {
            String name = jsonObject.getString(MYSQL.Items.NAME);
            int cId = jsonObject.getInt(MYSQL.Items.CID);
            int iId = jsonObject.getInt(MYSQL.Items.IID);
            int price = jsonObject.getInt(MYSQL.Items.PRICE);
            int rate = jsonObject.getInt(MYSQL.Items.RATE);
            int total = jsonObject.getInt(MYSQL.Items.TOTAL);
            String imgSrc = jsonObject.getString(MYSQL.IMG);

            return new Items(iId, name, cId, price, imgSrc, rate, total);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static ArrayList<Items> allItems(Activity activity) {
        ArrayList<Items> itemList = new ArrayList<>();

        try {
            String data = MyConst.getPrefData(activity, PrefConst.ITEM_S);
            JSONArray jArr = new JSONObject(data).getJSONArray(PrefConst.ITEM_S);
            for (int i = 0; i < jArr.length(); i++) {
                itemList.add(jsonToItem(jArr.getJSONObject(i)));
            }

            return itemList;
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return null;
    }

    public static ArrayList<Items> favItems(Activity activity, String pref) {
        ArrayList<Items> favItems = new ArrayList<>();
        ArrayList<Items> all = allItems(activity);
        Random rand = new Random();
        try {
            String data = MyConst.getPrefData(activity, pref);
            JSONArray jArr = new JSONObject(data).getJSONArray(pref);
            for (int i = 0; i < 5; i++) {
                try {
                    JSONObject jsonObject = jArr.getJSONObject(i);
                    int iid = jsonObject.getInt("iid");
                    favItems.add(getItem(activity, iid));
                } catch (JSONException e) {
                    favItems.add(all.get(rand.nextInt(all.size() - 1)));
                }
            }
        } catch (JSONException e) {
            favItems = new ArrayList<>();
            for (int i = 0; i < 5; i++)
                favItems.add(all.get(rand.nextInt(all.size() - 1)));
        }
        return favItems;
    }

    public static ArrayList<Items> allItems(Activity activity, int cId) {
        ArrayList<Items> itemList = allItems(activity);
        ArrayList<Items> catItemList = new ArrayList<>();

        for (Items items : itemList) {
            if (cId == items.getcId()) {
                catItemList.add(items);
            }
        }
        return catItemList;
    }

    @Nullable
    public static ArrayList<String> allItemStr(Activity activity) {
        try {
            ArrayList<Items> items = allItems(activity);
            ArrayList<String> stringList = new ArrayList<>();
            stringList.add("Select Category");

            for (Items item : items) {
                stringList.add(item.getName());
            }
            return stringList;
        } catch (Exception e) {
            MyConst.toast(activity, e.getMessage());
        }
        return null;
    }

    @Contract(pure = true)
    public static boolean isItemAvail(Activity activity, String itemName) {
        ArrayList<String> items = allItemStr(activity);
        for (String item : items) {
            if (item.equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Items getItem(Activity activity, int iId) {
        ArrayList<Items> items = allItems(activity);
        for (Items item : items) {
            if (item.getiId() == iId) {
                return item;
            }
        }
        return null;
    }

    //region Getter Setter
    public String getImgSrc() {
        return imgSrc;
    }

    public int getPrice() {
        return price;
    }

    public int getiId() {
        return iId;
    }

    public int getcId() {
        return cId;
    }

    public String getName() {
        return name;
    }

    public int getRate() {
        return rate;
    }

    public int getTotal() {
        return total;
    }

//endregion

}