package patel.jay.jaijalaram.ModelClass;

import android.app.Activity;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;

/**
 * Created by Jay on 26-Jan-18.
 */

public class LogData implements Serializable {

    private int logId, custId, datetime;
    private String name, lodDate, logTime, state;

    public LogData(int logId, int custId, int datetime, String name, String lodDate, String logTime, String state) {
        this.logId = logId;
        this.custId = custId;
        this.datetime = datetime;
        this.name = name;
        this.lodDate = lodDate;
        this.logTime = logTime;
        this.state = state;
    }

    @Nullable
    public static LogData setLog(Activity activity, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray(PrefConst.USERLOG_S);
            jsonObject = jsonArray.getJSONObject(0);

            int logId = jsonObject.getInt("logId");
            int custId = jsonObject.getInt("custId");
            int datetime = jsonObject.getInt("datetime");
            String name = jsonObject.getString("name");
            String lDate = jsonObject.getString("log_date");
            String logTime = jsonObject.getString("log_time");
            String state = jsonObject.getString("state");


            return new LogData(logId, custId, datetime, name, lDate, logTime, state);
        } catch (JSONException e) {
            e.printStackTrace();
            MyConst.toast(activity, e.getMessage());
        }
        return null;
    }

    //region Getter Setter
    public int getLogId() {
        return logId;
    }

    public int getCustId() {
        return custId;
    }

    public int getDatetime() {
        return datetime;
    }

    public String getName() {
        return name;
    }

    public String getLodDate() {
        return lodDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public String getState() {
        return state;
    }
    //endregion

}