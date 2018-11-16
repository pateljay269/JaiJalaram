package patel.jay.jaijalaram.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;

import patel.jay.jaijalaram.Adapter.CatAdAdapter;
import patel.jay.jaijalaram.Adapter.ItemAdAdapter;
import patel.jay.jaijalaram.Adapter.ItemAdapter;
import patel.jay.jaijalaram.Adapter.MyPagerAdapter;
import patel.jay.jaijalaram.Activity.AdminDashActivity;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.Activity.DashActivity;
import patel.jay.jaijalaram.ModelClass.Customer;
import patel.jay.jaijalaram.R;

public class SignActivity extends AppCompatActivity implements ServerCall.OnAsyncResponse {

    public static ViewPager viewPager;
    public static Customer customer;
    TabLayout tabLayout;
    MyPagerAdapter adapter;
    Activity activity = SignActivity.this;

    public static void updateUserData(Activity activity, String response) {
        String user = Customer.toGson(activity, response);
        MyConst.putIntoPref(activity, PrefConst.USERDATA_S, user);
        customer = Customer.fromGson(activity);

        assert customer != null;
        switch (customer.getType()) {
            case "A":
                activity.startActivity(new Intent(activity, AdminDashActivity.class));
                break;

            case "M":
                activity.startActivity(new Intent(activity, AdminDashActivity.class));
                break;

            case "U":
                DashActivity.isFirstLoad = true;
                activity.startActivity(new Intent(activity, DashActivity.class));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_tablayout);

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

        } catch (Exception e) {
            MyConst.toast(activity, e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ItemAdapter.ITEMS = null;
        ItemAdAdapter.ITEM_ADMIN = null;
        CatAdAdapter.CATEGORY_ADMIN = null;

        checkLogin();
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            switch (id) {
                case R.id.action_home:
                    if (viewPager.getCurrentItem() == 1)
                        if (!ServerCall.BASE_URL.equals("")) {
                            startActivity(new Intent(getApplicationContext(), AdminDashActivity.class));
                        } else {
                            MyConst.toast(activity, "Set IP First");
                        }
                    else
                        viewPager.setCurrentItem(1, true);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkLogin() {
        customer = Customer.fromGson(activity);
        if (customer != null) {
            String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
            HashMap<String, String> hm = new HashMap<>();
            hm.put("mobile", customer.getMobile() + "");
            hm.put("pass_word", customer.getPassword() + "");
            hm.put("action", "Login");
            ServerCall asyncTask = new ServerCall(this, url, hm, MyConst.INSERT);
            asyncTask.execute();
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case MyConst.INSERT:
                if (response.length() > 0) {
                    updateUserData(activity, response);
                } else {
                    MyConst.toast(activity, "Invalid Username or Password!!!");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        MyConst.backClick(activity);
    }
}