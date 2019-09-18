package patel.jay.jaijalaram.Models;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;

/**
 * Created by Jack on 01-Apr-18.
 */

public class Worker {
    private int wid;
    private String name, address, gender, joinDt, mobile, addedOn;

    public Worker(int wid, String name, String mobile, String gender, String address, String joinDt, String addedOn) {
        this.wid = wid;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.joinDt = joinDt;
        this.mobile = mobile;
        this.addedOn = addedOn;
    }

    //region Getters

    public int getWid() {
        return wid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getJoinDt() {
        return joinDt;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddedOn() {
        return addedOn;
    }

    //endregion

    public static Worker jsonToWorker(JSONObject jsonObject) {
        try {
            int wid = jsonObject.getInt(MYSQL.Worker.WID);
            String name = jsonObject.getString(MYSQL.Worker.NAME);
            String address = jsonObject.getString(MYSQL.Worker.ADDRESS);
            String gender = jsonObject.getString(MYSQL.Worker.GENDER);
            String joinDt = jsonObject.getString(MYSQL.Worker.JOINDT);
            String addedOn = jsonObject.getString(MYSQL.Worker.ADDEDON);
            String mobile = jsonObject.getString(MYSQL.Worker.MOBILE);

            return new Worker(wid, name, mobile, gender, address, joinDt, addedOn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Worker> allWorker(Activity activity) {
        ArrayList<Worker> workers = new ArrayList<>();

        try {
            String data = MyConst.getPrefData(activity, PrefConst.WORKER_S);
            JSONArray jArr = new JSONObject(data).getJSONArray(PrefConst.WORKER_S);

            for (int i = 0; i < jArr.length(); i++) {
                workers.add(jsonToWorker(jArr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return workers;
    }

    public static ArrayList<String> allWorkerStr(Activity activity) {
        ArrayList<Worker> workers = allWorker(activity);
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            for (Worker worker : workers) {
                arrayList.add(worker.getName());
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return null;
    }

    public static ArrayList<String> allWorkerIds(Activity activity) {
        ArrayList<Worker> workers = allWorker(activity);
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            for (Worker worker : workers) {
                arrayList.add(worker.getWid() + "");
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return null;
    }

    public static String workerName(Activity activity, int wid) {
        ArrayList<Worker> all = allWorker(activity);

        for (Worker worker : all) {
            if (worker.getWid() == wid) {
                return worker.getName();
            }
        }
        return "";
    }
}
