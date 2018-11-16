package patel.jay.jaijalaram.ModelClass;

import android.app.Activity;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.Contract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;

/**
 * Created by Jay on 26-Jan-18.
 */

public class Categorys implements Serializable {

    private int cId;
    private String name, imgSrc;

    public Categorys(int cId, String name, String imgSrc) {
        this.name = name;
        this.imgSrc = imgSrc;
        this.cId = cId;
    }

    @Nullable
    public static Categorys jsonToCat(JSONObject jsonObject) {
        try {
            int cId = jsonObject.getInt(MYSQL.Category.CID);
            String cName = jsonObject.getString(MYSQL.Category.NAME);
            String imgSrc = jsonObject.getString(MYSQL.IMG);

            return new Categorys(cId, cName, imgSrc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Categorys> allCategory(Activity activity) {
        ArrayList<Categorys> categorys = new ArrayList<>();

        try {
            String data = MyConst.getPrefData(activity, PrefConst.CATEGORY_S);
            JSONArray jArr = new JSONObject(data).getJSONArray(PrefConst.CATEGORY_S);

            for (int i = 0; i < jArr.length(); i++) {
                categorys.add(jsonToCat(jArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return categorys;
    }

    @Nullable
    public static ArrayList<String> allCatStr(Activity activity) {
        try {
            ArrayList<Categorys> categorys = allCategory(activity);
            ArrayList<String> stringList = new ArrayList<>();
            stringList.add("Select Category");

            for (Categorys cat : categorys) {
                stringList.add(cat.getName());
            }
            return stringList;
        } catch (Exception e) {
            MyConst.toast(activity, e.getMessage());
        }
        return null;
    }

    @Contract(pure = true)
    public static boolean isCategoryAvail(Activity activity, String catName) {
        ArrayList<String> categorys = allCatStr(activity);
        for (String cat : categorys) {
            if (cat.equalsIgnoreCase(catName)) {
                return true;
            }
        }
        return false;
    }

    public static String getCatName(Activity activity, int cId) {
        ArrayList<Categorys> allCategory = allCategory(activity);

        for (Categorys item : allCategory) {
            if (item.getcId() == cId) {
                return item.getName();
            }
        }

        return "";
    }

    //region Getter Setter
    public String getImgSrc() {
        return imgSrc;
    }

    public int getcId() {
        return cId;
    }

    public String getName() {
        return name;
    }

    //endregion

}