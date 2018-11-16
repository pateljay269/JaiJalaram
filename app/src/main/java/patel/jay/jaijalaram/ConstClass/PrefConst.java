package patel.jay.jaijalaram.ConstClass;

/**
 * Created by Jay on 25-Feb-18.
 */

public class PrefConst {

    //All SharedPrefrences Files & Json ArrayName
    public static final String PREF_FILE = "FASTFOOD PREF",
            USERDATA_S = "USERDATA", USERLOG_S = "USERLOG",
            ITEM_S = "ITEMS", CATEGORY_S = "CATEGORYS",
            ORDER_S = "ORDERS",
            AORDER_S = "AORDERS", AUSER_S = "USERS";

    //All Json ArrayName
    public static final String ORDERITEM = "ORDERITEMS", CART_ITEM = "CARTITEMS";

    //All Shared Files Which Are Clear At LogOut
    public static final String[] SHARED = {USERDATA_S, USERLOG_S, ORDER_S, AORDER_S, AUSER_S};
}
