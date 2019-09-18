package patel.jay.jaijalaram.Constants;

/**
 * Created by Jay on 24-Feb-18.
 */

public class MYSQL {
    public static final String IMG = "imgSrc";
    public static final String TIME = "time";
    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String DATE = "date";
    public static final String MONTH = "month";
    public static final String YEAR = "year";

    public class Attend {
        public static final String CURTIME = "curtime";
        public static final String WID = "wid";
        public static final String STATUS = "status";
        public static final String DD = "dd";
        public static final String MM = "mm";
        public static final String YY = "yy";

    }

    public class Customer {
        public static final String CUSTID = "custId";
        public static final String FNAME = "fname";
        public static final String LNAME = "lname";
        public static final String MOBILE = "mobile";
        public static final String PASSWORD = "pass_word";
        public static final String EMAIL = "email";
        public static final String ADDRESS = "address";
        public static final String TYPE = "type";
        public static final String ACTION = "action";
    }

    public class Items {
        public static final String IID = "iId";
        public static final String NAME = "iName";
        public static final String PRICE = "price";
        public static final String CID = "cId";
        public static final String RATE = "rate";
        public static final String TOTAL = "total";
    }

    public class Category {
        public static final String CID = "cId";
        public static final String NAME = "cName";
    }

    public class OrderItem {
        public static final String OIID = "oiid";
        public static final String OID = "oId";
        public static final String IID = "iId";
        public static final String QTY = "qty";
        public static final String PRICE = "price";
        public static final String DSC = "dsc";
        public static final String CID = "cId";
    }

    public class Order {
        public static final String OID = "oId";
        public static final String CUSTID = "custId";
        public static final String PRICE = "price";
        public static final String CURTIME = "currentTime";
        public static final String CONFIRM = "confirm";
        public static final String ISDELIVER = "isDeliver";
        public static final String IID = "iId";
    }

    public class Worker {
        public static final String WID = "wid";
        public static final String NAME = "name";
        public static final String MOBILE = "mobile";
        public static final String GENDER = "gender";
        public static final String ADDRESS = "address";
        public static final String JOINDT = "joindt";
        public static final String CURTIME = "curtime";
        public static final String ADDEDON = "addedOn";
    }


}
