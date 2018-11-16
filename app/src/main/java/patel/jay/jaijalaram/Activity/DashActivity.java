package patel.jay.jaijalaram.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

import patel.jay.jaijalaram.ConstClass.MYSQL;
import patel.jay.jaijalaram.ConstClass.MyConst;
import patel.jay.jaijalaram.ConstClass.PrefConst;
import patel.jay.jaijalaram.ConstClass.ServerCall;
import patel.jay.jaijalaram.Fragments.CartFragment;
import patel.jay.jaijalaram.Fragments.FavouriteFragment;
import patel.jay.jaijalaram.Fragments.HomeFragment;
import patel.jay.jaijalaram.Fragments.MenuFragment;
import patel.jay.jaijalaram.Fragments.OrdersFragment;
import patel.jay.jaijalaram.Fragments.ProfileFragment;
import patel.jay.jaijalaram.Login.SignActivity;
import patel.jay.jaijalaram.R;

public class DashActivity extends AppCompatActivity implements ServerCall.OnAsyncResponse,
        NavigationView.OnNavigationItemSelectedListener {

    public static boolean isFirstLoad = true;
    private static TextView tvNoti;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    NavigationView navView;
    DrawerLayout drawer;
    Toolbar toolbar;
    Activity activity = DashActivity.this;
    FragmentTransaction trans;

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

        String url = ServerCall.BASE_URL + ServerCall.ITEMS + "allCat";
        new ServerCall(activity, url, new HashMap<String, String>(), 1101).execute();

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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut(activity);
                break;

            case R.id.action_cart:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayout, new CartFragment()).commit();
                break;

            case R.id.action_notify:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START))
                drawer.closeDrawer(GravityCompat.START);

            trans = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_home:
                    trans.replace(R.id.framelayout, new HomeFragment());
                    break;

                case R.id.nav_menu:
                    trans.replace(R.id.framelayout, new MenuFragment());
                    break;

                case R.id.nav_order:
                    trans.replace(R.id.framelayout, new OrdersFragment());
                    break;

                case R.id.nav_fav:
                    trans.replace(R.id.framelayout, new FavouriteFragment());
                    break;

                case R.id.nav_profile:
                    trans.replace(R.id.framelayout, new ProfileFragment());
                    break;

                case R.id.nav_cart:
                    trans.replace(R.id.framelayout, new CartFragment());
                    break;

                case R.id.nav_logout:
                    logOut(activity);
                    break;
            }
            trans.commit();
        } catch (Exception e) {
            MyConst.toast(activity, e.getMessage());
        }

        return true;
    }

    @Override
    public void getResponse(String response, int flag) {
        switch (flag) {
            case 1101:
                if (response.length() > 0) {
                    MyConst.putIntoPref(activity, PrefConst.CATEGORY_S, response);
                }
                String url1 = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
                new ServerCall(activity, url1, new HashMap<String, String>(), 1102).execute();
                break;

            case 1102:
                if (response.length() > 0) {
                    MyConst.putIntoPref(activity, PrefConst.ITEM_S, response);

                    if (isFirstLoad) {
                        trans = getSupportFragmentManager().beginTransaction();
                        trans.add(R.id.framelayout, new MenuFragment());
                        trans.commit();
                        isFirstLoad = false;
                    }
                }
                break;

            case MyConst.INSERT:
                if (logClear(activity, response)) {
                    startActivity(new Intent(activity, SignActivity.class));
                    finish();
                }
                break;
        }
    }

    public static boolean logClear(Activity activity, String response) {
        if (response.trim().equals("1")) {
            for (String str : PrefConst.SHARED) {
                MyConst.putIntoPref(activity, str, "");
            }
            return true;
        } else {
            MyConst.toast(activity, response);
            return false;
        }
    }

    public static void logOut(Activity activity) {
        String url = ServerCall.BASE_URL + ServerCall.CUST + "loginVerify";
        HashMap<String, String> hm = new HashMap<>();
        hm.put(MYSQL.Customer.MOBILE, SignActivity.customer.getMobile() + "");
        hm.put(MYSQL.Customer.PASSWORD, SignActivity.customer.getPassword() + "");
        hm.put(MYSQL.Customer.ACTION, "Logout");
        new ServerCall(activity, url, hm, MyConst.INSERT).execute();
    }

}