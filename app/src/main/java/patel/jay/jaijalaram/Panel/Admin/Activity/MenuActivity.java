package patel.jay.jaijalaram.Panel.Admin.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import patel.jay.jaijalaram.Adapter.CatAdAdapter;
import patel.jay.jaijalaram.Adapter.ItemAdAdapter;
import patel.jay.jaijalaram.Adapter.MyPagerAdapter;
import patel.jay.jaijalaram.Constants.PrefConst;
import patel.jay.jaijalaram.Constants.ServerCall;
import patel.jay.jaijalaram.Panel.Admin.Fragments.CategoryFragment;
import patel.jay.jaijalaram.Panel.Admin.Fragments.ItemsFragment;
import patel.jay.jaijalaram.R;

import static patel.jay.jaijalaram.Constants.MyConst.isNetAvail;
import static patel.jay.jaijalaram.Constants.MyConst.putIntoPref;
import static patel.jay.jaijalaram.Constants.MyConst.titleSet;
import static patel.jay.jaijalaram.Constants.MyConst.toast;

public class MenuActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        ServerCall.OnAsyncResponse {

    ViewPager viewPager;
    TabLayout tabLayout;
    Activity activity = MenuActivity.this;

    private static final int ITEM = 1001, CATEGORY = 1002;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tablayout);

        titleSet(this, "Menu");

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(View.GONE);

        viewPager.addOnPageChangeListener(this);


        setViewPager();
    }

    private void setViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CategoryFragment(), PrefConst.CATEGORY_S);
        adapter.addFragment(new ItemsFragment(), PrefConst.ITEM_S);

        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CatAdAdapter.CATEGORY_ADMIN = null;
        ItemAdAdapter.ITEM_ADMIN = null;
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                finish();
                break;

            case R.id.action_refresh:
                refreshData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void refreshData() {
        if (isNetAvail(activity, findViewById(R.id.linear))) {
            url = ServerCall.BASE_URL + ServerCall.ITEMS + "allCat";
            new ServerCall(activity, url, null, CATEGORY).execute();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                titleSet(this, "Category Menu");
                break;

            case 1:
                titleSet(this, "Item Menu");
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void getResponse(String response, int flag) {
        try {
            switch (flag) {
                case CATEGORY:
                    if (response.length() > 0) {
                        putIntoPref(activity, PrefConst.CATEGORY_S, response);

                        url = ServerCall.BASE_URL + ServerCall.ITEMS + "allItem";
                        new ServerCall(activity, url, null, ITEM).execute();
                    } else
                        toast(activity, "Error:" + response);
                    break;

                case ITEM:
                    if (response.length() > 0) {
                        putIntoPref(activity, PrefConst.ITEM_S, response);

                        setViewPager();
                    } else
                        toast(activity, "Error:" + response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }
    }
}