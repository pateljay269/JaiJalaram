package patel.jay.jaijalaram.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import patel.jay.jaijalaram.Adapter.CatAdAdapter;
import patel.jay.jaijalaram.Adapter.ItemAdAdapter;
import patel.jay.jaijalaram.Adapter.MyPagerAdapter;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Models.Customer;
import patel.jay.jaijalaram.Panel.Admin.AdminDashActivity;
import patel.jay.jaijalaram.Panel.Customer.CustDashActivity;
import patel.jay.jaijalaram.Panel.ShowItem.ItemDscActivity;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.putIntoPref;

public class SignActivity extends AppCompatActivity implements ServerCall.OnAsyncResponse, ViewPager.OnPageChangeListener {

    public static Customer customer;
    public static ViewPager viewPager;
    TabLayout tabLayout;
    MyPagerAdapter adapter;
    Activity activity = SignActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_tablayout);

            getSupportActionBar().hide();

            tabLayout = findViewById(R.id.tablayout);
            viewPager = findViewById(R.id.viewpager);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setVisibility(View.GONE);

            adapter = new MyPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new SignUpFragment(), "Sign Up");
            adapter.addFragment(new MainFragment(), "Home Screen");
            adapter.addFragment(new SignInFragment(), "Sign In");

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(1, true);

            viewPager.addOnPageChangeListener(this);

        } catch (Exception e) {
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ItemDscActivity.ITEMS = null;
        ItemAdAdapter.ITEM_ADMIN = null;
        CatAdAdapter.CATEGORY_ADMIN = null;

        checkData(activity);
//        checkLogin(); //For Online Re-checking

    }

    //region Unused
/*
    private void checkLogin() {
        customer = Customer.fromGson(activity);

        if (customer != null) {
            String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
            HashMap<String, String> hm = new HashMap<>();
            hm.put("mobile", customer.getMobile() + "");
            hm.put("pass_word", customer.getPassword() + "");
            hm.put(MYSQL.Customer.ACTION, "Re Login");
            new ServerCall(activity, url, hm, MyConst.INSERT).execute();
        }
    }
*/
    //endregion

    public static void checkData(Activity activity) {
        try {
            customer = Customer.fromGson(activity);
            if (customer != null) {

                switch (customer.getType()) {
                    case "A":
                    case "M":
                        activity.startActivity(new Intent(activity, AdminDashActivity.class));
                        break;

                    case "U":
                        activity.startActivity(new Intent(activity, CustDashActivity.class));
                        break;
                }
            }
        } catch (Exception e) {
            customer = null;
            MyConst.toast(activity, e.getMessage());
        }
    }

    public static void updateData(Activity activity, String response, boolean move2Dash) {
        try {
            String user = Customer.toGson(activity, response);
            putIntoPref(activity, PrefConst.USERDATA_S, user);

            if (move2Dash) {
                checkData(activity);
            } else {
                customer = Customer.fromGson(activity);
            }
        } catch (Exception e) {
            customer = null;
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.INSERT:
                if (response.length() > 0) {
                    try {
                        String user = Customer.toGson(activity, response);
                        if (user.equalsIgnoreCase("null") || user.trim().equals("") || user == null) {
                            return;
                        }

                        updateData(activity, response, true);
                    } catch (Exception e) {
                        customer = null;
                        MyConst.toast(activity, e.getMessage());
                    }
                } else
                    MyConst.toast(activity, "Invalid Username or Password!!!");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        MyConst.closeApp(activity);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        MyConst.hideKeyboard(activity);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}