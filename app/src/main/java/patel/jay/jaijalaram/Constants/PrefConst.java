package patel.jay.jaijalaram.Constants;

/**
 * Created by Jay on 25-Feb-18.
 */

public class PrefConst {

    //All SharedPrefrences Files & Json ArrayName
    public static final String PREF_FILE = "FASTFOOD PREF",
            USERDATA_S = "USERDATA",
            ITEM_S = "ITEMS", CATEGORY_S = "CATEGORYS",
            ORDER_S = "ORDERS", WORKER_S = "WORKERS",
            AORDER_S = "AORDERS", AUSER_S = "USERS",
            HOMEITEM = "HOMEITEM", FAVITEM = "FAVITEM",
            MAXMIN = "MAXMIN", MAXMINATTEND = "MAXMINATTEND",
            HOMEIMAGE = "HOMEIMAGE";

    //All Json ArrayName
    public static final String ORDERITEM = "ORDERITEMS",
            REPORT = "REPORT", DATES = "DATES", MOBILE = "MOBILE", PASSWORD = "PASSWORD";

    //All Shared Files Which Are Clear At LogOut
    public static final String[] SHARED = {USERDATA_S, ORDER_S, AORDER_S, AUSER_S,
            WORKER_S, MAXMIN, MAXMINATTEND}; //, HOMEITEM, FAVITEM

}
