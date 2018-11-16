package patel.jay.jaijalaram.ConstClass;

/**
 * Created by Jay on 24-Feb-18.
 */

public class MYSQL {
    public static final String IMG = "imgSrc";

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
    }

    public class Order {
        public static final String OID = "oId";
        public static final String CUSTID = "custId";
        public static final String DATE = "date";
        public static final String PRICE = "price";
        public static final String TIME = "currentTime";
        public static final String IID = "iId";
    }

}
