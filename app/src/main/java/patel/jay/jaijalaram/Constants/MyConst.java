package patel.jay.jaijalaram.Constants;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jay on 20-Jan-18.
 */

public class MyConst {

    public static final int ROWCOUNT = 2;
    public static final int INSERT = 101, UPDATE = 102, DELETE = 103, SELECT = 104;

    public static final String CATEGORY = "Category", ITEM = "Item", sDetail = "detail", sOrder = "order", sItems = "items";

    public static final String FIREBASE_IMG = "https://firebasestorage.googleapis.com/v0/b/jaijalaram-e003a.appspot.com/o/";

    public static void toast(Context activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static String getPrefData(Context activity, String pref) {
        SharedPreferences preferences = activity.getSharedPreferences(PrefConst.PREF_FILE, MODE_PRIVATE);
        return preferences.getString(pref, "");
    }

    public static void putIntoPref(Context activity, String pref, String data) {
        SharedPreferences preferences = activity.getSharedPreferences(PrefConst.PREF_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(pref, data);
        editor.apply();
    }

    //region Return String
    @NonNull
    @Contract(pure = true)
    public static String numberOf(int number) {
        if (number >= 0 && number <= 9) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }

    @Contract(pure = true)
    public static String dayName(int day) {
        String weekDay = "";
        switch (day) {
            case 1:
                weekDay = "SUN";
                break;
            case 2:
                weekDay = "MON";
                break;
            case 3:
                weekDay = "TUE";
                break;
            case 4:
                weekDay = "WED";
                break;
            case 5:
                weekDay = "THU";
                break;
            case 6:
                weekDay = "FRI";
                break;
            case 7:
                weekDay = "SAT";
                break;
        }
        return weekDay;
    }

    @Contract(pure = true)
    public static String weekdaySuper(int day) {
        String sup;
        switch (day) {
            case 1:
            case 21:
            case 31:
                sup = "st";
                break;
            case 2:
            case 22:
                sup = "nd";
                break;
            case 3:
            case 23:
                sup = "rd";
                break;
            default:
                sup = "th";
                break;

        }
        return day + sup;
    }

    @Contract(pure = true)
    public static String monthName(int month) {
        String monthName = "";
        switch (month) {
            case 0:
                monthName = "JAN";
                break;
            case 1:
                monthName = "FEB";
                break;
            case 2:
                monthName = "MAR";
                break;
            case 3:
                monthName = "APR";
                break;
            case 4:
                monthName = "MAY";
                break;
            case 5:
                monthName = "JUN";
                break;
            case 6:
                monthName = "JUL";
                break;
            case 7:
                monthName = "AUG";
                break;
            case 8:
                monthName = "SEP";
                break;
            case 9:
                monthName = "OCT";
                break;
            case 10:
                monthName = "NOV";
                break;
            case 11:
                monthName = "DEC";
                break;
        }
        return monthName;
    }

    //endregion

    public static void titleSet(AppCompatActivity activity, String title) {
        try {
            ActionBar ab = activity.getSupportActionBar();

            TextView tv = new TextView(activity);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            tv.setLayoutParams(lp);
            tv.setText(title);
            tv.setTextSize(20);
            tv.setTextColor(Color.WHITE);
            tv.setTypeface(Typeface.DEFAULT_BOLD);

            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(tv);
        } catch (Exception e) {
            e.printStackTrace();
            activity.setTitle(title);
        }

    }

    public static void etBlankCheck(EditText et) {
        if (etBlank(et)) {
            et.setError("Required");
        }
    }

    public static boolean etBlank(EditText et) {
        return et.getText().toString().trim().isEmpty();
    }

    public static void clearEdittext(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            } else if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearEdittext((ViewGroup) view);
        }
    }

    public static String getPath(Activity activity, Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        assert cursor != null;
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public static void copy2Clip(Activity activity, String str) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", str);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        toast(activity, "Copied");
    }

    public static boolean isNetAvail(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetAvail(final Activity activity, View view) {
        boolean netAvail = isNetAvail(activity);

        if (!netAvail) {
            Snackbar sb = Snackbar
                    .make(view, "There Is No Internet Connection...!!!" +
                            "\nYour Device Is Offline...!!!", Snackbar.LENGTH_LONG)//;
                    .setAction("Exit", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            closeApp(activity);
                        }
                    });

            sb.setActionTextColor(Color.RED);
            View sbView = sb.getView();
            TextView tv = sbView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextSize(17);
            tv.setTextColor(Color.YELLOW);
            sb.show();
        }

        return netAvail;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeApp(Activity activity) {
        activity.finishAffinity();
        System.exit(0);
    }

}