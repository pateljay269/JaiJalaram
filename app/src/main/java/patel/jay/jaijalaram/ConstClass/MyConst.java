package patel.jay.jaijalaram.ConstClass;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jay on 20-Jan-18.
 */

public class MyConst {

    public static final int ROWCOUNT = 2;
    public static final int INSERT = 101, UPDATE = 102, DELETE = 103, SELECT = 104;

    public static final String CATEGORY = "Category", ITEM = "Item";

    public static final String FIREBASE_IMG = "https://firebasestorage.googleapis.com/v0/b/jaijalaram-e003a.appspot.com/o/";

    public static void toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static void error(Activity activity, Exception e) {
        toast(activity, Arrays.toString(e.getStackTrace()));
    }

    public static String getPrefData(Activity activity, String pref) {
        SharedPreferences preferences = activity.getSharedPreferences(PrefConst.PREF_FILE, MODE_PRIVATE);
        return preferences.getString(pref, "");
    }

    public static void putIntoPref(Activity activity, String pref, String data) {
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
    public static String dayName(int dayOfWeek) {
        String weekDay = "";
        switch (dayOfWeek) {
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
    public static String weekSuper(int day) {
        String superScript;
        switch (day) {
            case 1:
            case 21:
            case 31:
                superScript = "st";
                break;
            case 2:
            case 22:
                superScript = "nd";
                break;
            case 3:
            case 23:
                superScript = "rd";
                break;
            default:
                superScript = "th";
                break;

        }
        return superScript;
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

    @NonNull
    @Contract(pure = true)
    public static String dayGujName(int dayOfWeek) {
        String weekDay = "";
        switch (dayOfWeek) {
            case 1:
                weekDay = "રવિ";
                break;
            case 2:
                weekDay = "સોમ";
                break;
            case 3:
                weekDay = "મંગળ";
                break;
            case 4:
                weekDay = "બુધ";
                break;
            case 5:
                weekDay = "ગુરૂ";
                break;
            case 6:
                weekDay = "શુક્ર";
                break;
            case 7:
                weekDay = "શનિ";
                break;
        }
        return weekDay + "વાર";
    }

    @Contract(pure = true)
    public static String monthGujName(String month) {
        String monthName = "";
        switch (month) {
            case "JAN":
                monthName = "જાન્યુઆરી";
                break;
            case "FEB":
                monthName = "ફેબ્રુઆરી";
                break;
            case "MAR":
                monthName = "માર્ચ";
                break;
            case "APR":
                monthName = "એપ્રિલ";
                break;
            case "MAY":
                monthName = "મે";
                break;
            case "JUN":
                monthName = "જૂન";
                break;
            case "JUL":
                monthName = "જુલાઈ";
                break;
            case "AUG":
                monthName = "ઑગસ્ટ";
                break;
            case "SEP":
                monthName = "સપ્ટેમ્બર";
                break;
            case "OCT":
                monthName = "ઑક્ટોબર";
                break;
            case "NOV":
                monthName = "નવેમ્બર";
                break;
            case "DEC":
                monthName = "ડિસેમ્બર";
                break;
        }
        return monthName;
    }
    //endregion

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

    public static void backClick(Activity activity) {
        /*
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        activity.finish();
        activity.startActivity(intent);
//        activity.finishAndRemoveTask();
        */
        activity.finishAffinity();
        System.exit(0);
    }

}