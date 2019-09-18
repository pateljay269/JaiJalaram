package patel.jay.jaijalaram.Panel.Customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import patel.jay.jaijalaram.Constants.MYSQL;
import patel.jay.jaijalaram.Constants.MyConst;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.Panel.Customer.Fragments.CartFragment;
import patel.jay.jaijalaram.Panel.Customer.Fragments.FavouriteFragment;
import patel.jay.jaijalaram.Panel.Customer.Fragments.HomeFragment;
import patel.jay.jaijalaram.Panel.Customer.Fragments.MenuFragment;
import patel.jay.jaijalaram.Panel.Customer.Fragments.OrdersFragment;
import patel.jay.jaijalaram.Panel.Customer.Fragments.ProfileFragment;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.putIntoPref;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;

public class CustDashActivity extends AppCompatActivity implements ServerCall.OnAsyncResponse,
        NavigationView.OnNavigationItemSelectedListener {

    private static boolean isFirst = true;
    private static TextView tvNoti;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    NavigationView navView;
    DrawerLayout drawer;
    Toolbar toolbar;
    Activity activity = CustDashActivity.this;
    Context context = CustDashActivity.this;
    FragmentTransaction trans;

    HashMap<String, String> hm;
    String url = "";

    @SuppressLint("SetTextI18n")
    public static void setupBadge() {
        if (tvNoti != null) {
            int count = CartFragment.cartItems.size();
            if (count == 0) {
                tvNoti.setVisibility(View.GONE);
            } else {
                tvNoti.setText(Math.min(count, 99) + "");
                tvNoti.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        setTitle("Dashboard");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        if (isNetAvail(activity)) {
            refreshData();

        } else {
            setFragments();
        }

        //region UnUsed
/*
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        toggle.setHomeAsUpIndicator(android.R.drawable.arrow_down_float);
*/
        //endregion
    }

    int backPress = 0;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            backPress = 0;
        } else {
            backPress++;

            View view = findViewById(R.id.coordinator);
            Snackbar snackbar = Snackbar
                    .make(view, "Press Again To Exit", Snackbar.LENGTH_LONG)
                    .setAction("Logout", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            logOut(activity);
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

            if (backPress > 1) {
                finish();
                MyConst.closeApp(activity);
            }
        }
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        tvNoti = actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        backPress = 0;
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut(activity);
                break;

            case R.id.action_cart:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, new CartFragment()).commit();
                titleSet(this, getString(R.string.cart));
                break;

            case R.id.action_refresh:
                refreshData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        backPress = 0;
        String title = "";

        try {

            trans = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_home:
                    trans.replace(R.id.framelayout, new HomeFragment());
                    title = getString(R.string.home);
                    break;

                case R.id.nav_menu:
                    trans.replace(R.id.framelayout, new MenuFragment());
                    title = getString(R.string.menu);
                    break;

                case R.id.nav_order:
                    trans.replace(R.id.framelayout, new OrdersFragment());
                    title = getString(R.string.orders);
                    break;

                case R.id.nav_fav:
                    trans.replace(R.id.framelayout, new FavouriteFragment());
                    title = getString(R.string.favorites);
                    break;

                case R.id.nav_profile:
                    trans.replace(R.id.framelayout, new ProfileFragment());
                    title = getString(R.string.profile);
                    break;

                case R.id.nav_cart:
                    trans.replace(R.id.framelayout, new CartFragment());
                    title = getString(R.string.cart);
                    break;

                case R.id.nav_logout:
                    logOut(activity);
                    break;
            }
            trans.commit();
        } catch (Exception e) {
            MyConst.toast(activity, e.getMessage());
        }

        titleSet(this, title);

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void refreshData() {

        url = ServerCall.BASE_URL + ServerCall.LIST + "homeItems";
        new ServerCall(activity, url, null, 1103).execute();

        url = ServerCall.BASE_URL + ServerCall.ITEMS + "allCat";
        new ServerCall(activity, url, null, 1101).execute();

        url = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
        new ServerCall(activity, url, null, 1102).execute();

        hm = new HashMap<>();
        hm.put(MYSQL.Customer.CUSTID, SignActivity.customer.getCustId() + ""); //For Next Both

        url = ServerCall.BASE_URL + ServerCall.LIST + "favItems";
        new ServerCall(activity, url, hm, 1104).execute();

        url = ServerCall.BASE_URL + ServerCall.ORDER + "allCustOrd";
        new ServerCall(context, url, hm, MyConst.SELECT).execute();

    }

    private void setFragments() {
        try {
            trans = getSupportFragmentManager().beginTransaction();

            if (isFirst) {
                trans.add(R.id.framelayout, new HomeFragment()).commit();
                isFirst = false;
            } else {
                trans.replace(R.id.framelayout, new HomeFragment()).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            toast(activity, e.getMessage());
            refreshData();
        }
    }

    @Override
    public void getResponse(String response, int flag) {
        if (response.length() == 0) {
            return;
        }

        switch (flag) {
            case 1101:
                putIntoPref(activity, PrefConst.CATEGORY_S, response);
                break;

            case 1102:
                putIntoPref(activity, PrefConst.ITEM_S, response);
                break;

            case 1103:
                putIntoPref(activity, PrefConst.HOMEITEM, response);
                setFragments();
                break;

            case 1104:
                putIntoPref(activity, PrefConst.FAVITEM, response);
                break;

            case MyConst.INSERT:
                startActivity(new Intent(activity, SignActivity.class));
                finish();
                break;

            case MyConst.SELECT:
                if (response.length() > 0) {
                    MyConst.putIntoPref(activity, PrefConst.ORDER_S, response);
                }
                break;
        }
    }

    public static void logClear(Activity activity) {
        for (String str : PrefConst.SHARED)
            putIntoPref(activity, str, "");
    }

    public static void logOut(Activity activity) {
        logClear(activity);
//        activity.startActivity(new Intent(activity, SignActivity.class));
//        activity.finish();

        String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
        HashMap<String, String> hm = new HashMap<>();
        hm.put(MYSQL.Customer.MOBILE, SignActivity.customer.getMobile() + "");
        hm.put(MYSQL.Customer.PASSWORD, "");
        hm.put(MYSQL.Customer.ACTION, "Logout");
        new ServerCall(activity, url, hm, MyConst.INSERT).execute();
    }

}