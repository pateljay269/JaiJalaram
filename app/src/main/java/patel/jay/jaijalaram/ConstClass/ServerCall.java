package patel.jay.jaijalaram.ConstClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerCall extends AsyncTask<String, Void, String> {

    public static final String BASE_URL1 = "http://192.168.1.104:80/fastfood/";
    public static final String BASE_URL2 = "http://192.168.43.191:80/fastfood/";
    public static final String BASE_URL3 = "http://www.jayhpatel.tk/fastfood/";

    private static final String PHP = ".php?";
    public static final String ITEMS = "items" + PHP;
    public static final String CUST = "cust" + PHP;
    public static final String ORDER = "orders" + PHP;

    public static final String AALL = "aAll" + PHP;
    public static final String ACUST = "acust" + PHP;

    public static String BASE_URL = BASE_URL3;

    @SuppressLint("StaticFieldLeak")
    private Activity activity;
    private String url = "";
    private HashMap<String, String> hashMap = null;
    private int flag;
    private OnAsyncResponse caller;
    private ProgressDialog pDialog = null;
//    private JSONObject jsonObject;

    public ServerCall(Fragment fragment, Activity activity, String url,
                      HashMap<String, String> hashMap, int flag) {
        caller = (OnAsyncResponse) fragment;
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
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
            if (hashMap != null) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                // Add Params to Builder
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    formBodyBuilder.add(entry.getKey(), entry.getValue());
                }
                RequestBody formBody = formBodyBuilder.build();
                builder.post(formBody);
            }
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

/*    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void showToast(Context context, String msg) {
        if (msg == null)
            msg = "No Internet Connection!!!";
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }*/

}