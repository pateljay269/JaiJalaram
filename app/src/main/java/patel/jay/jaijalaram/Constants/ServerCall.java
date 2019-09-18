package patel.jay.jaijalaram.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("StaticFieldLeak")
public class ServerCall extends AsyncTask<String, Void, String> {

    //private static final String BASE_URL1 = "http://192.168.1.104:80/fastfood/";
    //private static final String BASE_URL2 = "http://192.168.43.191:80/fastfood/";
    //private static final String BASE_URL3 = "http://192.168.225.47:80/fastfood/";

    private static final String BASE_URL4 = "http://www.jayhpatel.tk/fastfood/";

    public static String BASE_URL = BASE_URL4;

    private static final String PHP = ".php?";
    public static final String ITEMS = "items" + PHP;
    public static final String CUST = "cust" + PHP;
    public static final String OTP = "otp" + PHP;
    public static final String WORKER = "worker" + PHP;
    public static final String ORDER = "orders" + PHP;

    public static final String LIST = "arraylist" + PHP;
    public static final String REORTS = "reports" + PHP;
    public static final String AALL = "aAll" + PHP;

    public static final String ACUST = "acust" + PHP;

    private Activity activity;
    private Context context;
    private String url = "";
    private HashMap<String, String> hashMap = null;
    private int flag;
    private OnAsyncResponse caller;
    private ProgressDialog pDialog = null;
//    private JSONObject jsonObject;

    public ServerCall(Fragment frag, Activity activity, String url, HashMap<String, String> hashMap, int flag) {
        caller = (OnAsyncResponse) frag;
        this.activity = activity;
        this.url = url;
        this.hashMap = hashMap;
        this.flag = flag;
    }

    public ServerCall(Activity activity, String url, HashMap<String, String> hashMap, int flag) {
        caller = (OnAsyncResponse) activity;
        this.activity = activity;
        this.url = url;
        this.hashMap = hashMap;
        this.flag = flag;
    }

    public ServerCall(Context context, String url, HashMap<String, String> hashMap, int flag) {
        caller = (OnAsyncResponse) context;
        this.context = context;
        activity = null;
        this.url = url;
        this.hashMap = hashMap;
        this.flag = flag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (activity != null) {
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected String doInBackground(String[] params) {
        return getJsonFromUrl();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        caller.getResponse(response.trim(), flag);
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    private String getJsonFromUrl() {
        try {
            OkHttpClient client = new OkHttpClient();
            client.connectTimeoutMillis(); // connect timeout
            client.readTimeoutMillis();
            //client.readTimeout(30, TimeUnit.SECONDS);

            Request.Builder builder = new Request.Builder().url(url);
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (hashMap != null) {
                // Add Params to Builder
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }
            }
            RequestBody formBody = formBodyBuilder.build();
            builder.post(formBody);
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    public interface OnAsyncResponse {
        void getResponse(String response, int flag);
    }

}